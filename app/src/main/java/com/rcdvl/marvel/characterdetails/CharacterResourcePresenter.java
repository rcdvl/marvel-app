package com.rcdvl.marvel.characterdetails;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.rcdvl.marvel.BaseModule;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.model.api.MarvelResource;
import com.rcdvl.marvel.model.api.MarvelResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by renan on 17/06/16.
 */
public class CharacterResourcePresenter extends MvpBasePresenter<CharacterResourceView> {

    public enum ResourceType {
        COMICS("comics", R.string.title_comics, R.string.empty_comics), SERIES("series", R.string.title_series, R.string.empty_series), STORIES("stories", R.string.title_stories,
                R.string.empty_stories), EVENTS("events", R.string.title_events, R.string.empty_events);

        private final int titleResId;
        private final int emptyResId;
        private String name;

        public int getTitleResId() {
            return titleResId;
        }

        ResourceType(String name, int title, int emptyId) {
            this.name = name;
            this.titleResId = title;
            this.emptyResId = emptyId;
        }

        public int getEmptyResId() {
            return emptyResId;
        }
    }

    private final long characterId;
    private final ResourceType resourceType;
    protected boolean isFirstLoad = true;
    protected int loadedCount = 0;
    protected int maxCount = -1;
    private Subscriber<MarvelResponse<MarvelResource>> subscriber;

    public CharacterResourcePresenter(long characterId, ResourceType type) {
        this.characterId = characterId;
        resourceType = type;
    }

    public void loadResource(final boolean pullToRefresh) {
        loadResource(pullToRefresh, loadedCount, 20);
    }

    private void loadResource(final boolean pullToRefresh, int start, int count) {
        unsubscribe();

        subscriber = new Subscriber<MarvelResponse<MarvelResource>>() {
            @Override
            public void onCompleted() {
                CharacterResourcePresenter.this.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                CharacterResourcePresenter.this.unsubscribe();
                Log.d("error", "loaded=" + loadedCount + ", max=" + maxCount + ", restype=" + resourceType);
                if (isViewAttached()) {
                    getView().showError(e, pullToRefresh);
                }
            }

            @Override
            public void onNext(MarvelResponse<MarvelResource> marvelResourceMarvelResponse) {
                loadedCount += marvelResourceMarvelResponse.data.results.size();
                maxCount = marvelResourceMarvelResponse.data.total;
                isFirstLoad = false;
                Log.d("counts", "loaded=" + loadedCount + ", max=" + maxCount + ", restype=" + resourceType);
                if (isViewAttached()) {
                    if (maxCount > 0) {
                        getView().setData(marvelResourceMarvelResponse.data.results);
                        getView().showContent();
                    } else {
                        getView().onEmptyResourcesList();
                    }
                }
            }
        };

        BaseModule.marvelApi().getCharacterResourceList(resourceType.name, characterId, start, count).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
        subscriber = null;
    }

    public boolean isLoading() {
        return subscriber != null;
    }

    public boolean hasLoadedAllItems() {
        if (isFirstLoad) {
            return false;
        }

        if (maxCount != -1 && loadedCount >= maxCount) {
            return true;
        }

        return false;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }
}
