package com.rcdvl.marvel.characterslist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.rcdvl.marvel.BaseModule;
import com.rcdvl.marvel.model.api.MarvelCharacter;
import com.rcdvl.marvel.model.api.MarvelResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by renan on 13/06/16.
 */
public class CharactersPresenter extends MvpBasePresenter<CharactersView> {

    protected Subscriber<MarvelResponse<MarvelCharacter>> subscriber;
    protected boolean isFirstLoad = true;
    protected int loadedCount = 0;
    protected int maxCount = -1;

    public void loadCharacters(final boolean pullToRefresh) {
        loadCharacters(pullToRefresh, 0, 20);
    }

    public void loadMoreCharacters(final boolean pullToRefresh) {
        loadCharacters(pullToRefresh, loadedCount, 20);
    }

    private void loadCharacters(final boolean pullToRefresh, int start, int count) {
        unsubscribe();

        subscriber = new Subscriber<MarvelResponse<MarvelCharacter>>() {
            @Override
            public void onCompleted() {
                isFirstLoad = false;
                CharactersPresenter.this.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                CharactersPresenter.this.unsubscribe();

                if (isViewAttached()) {
                    getView().showError(e, pullToRefresh);
                }
            }

            @Override
            public void onNext(MarvelResponse<MarvelCharacter> marvelCharacterMarvelResponse) {
                loadedCount += marvelCharacterMarvelResponse.data.results.size();
                maxCount = marvelCharacterMarvelResponse.data.total;
                if (isViewAttached()) {
                    getView().setData(marvelCharacterMarvelResponse.data.results);
                    getView().showContent();
                }
            }
        };

        BaseModule.marvelApi().getCharactersList(start, count).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
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
