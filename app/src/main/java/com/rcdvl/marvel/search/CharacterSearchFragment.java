package com.rcdvl.marvel.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rcdvl.marvel.R;
import com.rcdvl.marvel.characterslist.CharactersListFragment;
import com.rcdvl.marvel.characterslist.CharactersPresenter;
import com.rcdvl.marvel.model.api.MarvelCharacter;

import java.util.List;

/**
 * Created by renan on 16/06/16.
 */
public class CharacterSearchFragment extends CharactersListFragment {

    public static final String EXTRA_QUERY = "extra-query";

    public static Fragment newInstance(String query) {
        CharacterSearchFragment fragment = new CharacterSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(CharacterSearchFragment.EXTRA_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new CharactersSearchAdapter();
        super.onViewCreated(view, savedInstanceState);

        loadData(false);
    }

    @NonNull
    @Override
    public CharactersPresenter createPresenter() {
        return new CharactersSearchPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        String query = getArguments().getString(EXTRA_QUERY);
        if (((CharactersSearchPresenter)presenter).isFirstLoad(query)) {
            animateLoadingViewIn();
        }

        hideErrors();

        ((CharactersSearchPresenter)presenter).searchMoreCharacters(false, query);
    }

    @Override
    public void setData(List<MarvelCharacter> data) {
        if (presenter.isFirstLoad()) {
            adapter.addCharacters(data, true);
        } else {
            adapter.addCharacters(data);
        }

        adapter.notifyDataSetChanged();
    }

    public void startNewSearch() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        loadData(false);
    }
}
