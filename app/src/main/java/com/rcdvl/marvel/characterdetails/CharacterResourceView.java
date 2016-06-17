package com.rcdvl.marvel.characterdetails;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.rcdvl.marvel.model.api.MarvelResource;

import java.util.List;

/**
 * Created by renan on 17/06/16.
 */
public interface CharacterResourceView extends MvpLceView<List<MarvelResource>> {

    void onEmptyResourcesList();
}
