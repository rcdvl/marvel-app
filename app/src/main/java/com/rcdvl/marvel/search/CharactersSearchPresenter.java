package com.rcdvl.marvel.search;

import com.rcdvl.marvel.BaseModule;
import com.rcdvl.marvel.characterslist.CharactersPresenter;
import com.rcdvl.marvel.model.api.MarvelCharacter;
import com.rcdvl.marvel.model.api.MarvelResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by renan on 16/06/16.
 */
public class CharactersSearchPresenter extends CharactersPresenter {
    private String lastQuery = null;

    public void searchMoreCharacters(final boolean pullToRefresh, String query) {
        if (lastQuery == null || !lastQuery.equals(query)) {
            loadedCount = 0;
            isFirstLoad = true;
        }

        searchCharacters(pullToRefresh, loadedCount, 20, query);
    }

    private void searchCharacters(final boolean pullToRefresh, int start, int count, String query) {
        unsubscribe();

        subscriber = new Subscriber<MarvelResponse<MarvelCharacter>>() {
            @Override
            public void onCompleted() {
                CharactersSearchPresenter.this.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                CharactersSearchPresenter.this.unsubscribe();

                if (isViewAttached()) {
                    getView().showError(e, pullToRefresh);
                }
            }

            @Override
            public void onNext(MarvelResponse<MarvelCharacter> marvelCharacterMarvelResponse) {
                loadedCount += marvelCharacterMarvelResponse.data.results.size();
                maxCount = marvelCharacterMarvelResponse.data.total;
                isFirstLoad = false;
                if (isViewAttached()) {
                    getView().setData(marvelCharacterMarvelResponse.data.results);
                    getView().showContent();
                }
            }
        };

        BaseModule.marvelApi().getCharactersList(start, count, query).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        lastQuery = query;
    }

    public boolean isFirstLoad(String query) {
        return query != null && !query.equals(lastQuery);
    }
}
