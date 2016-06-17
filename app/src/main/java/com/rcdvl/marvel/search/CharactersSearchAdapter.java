package com.rcdvl.marvel.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rcdvl.marvel.R;

/**
 * Created by renan on 16/06/16.
 */
public class CharactersSearchAdapter extends com.rcdvl.marvel.characterslist.CharactersAdapter {

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_character_search, parent, false);
        return new CharacterViewHolder(view);
    }
}
