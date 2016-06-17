package com.rcdvl.marvel.model.api;

import org.parceler.Parcel;

/**
 * Created by renan on 17/06/16.
 */
@Parcel
public class MarvelResource {
    public int id;
    public String title;
    public MarvelThumbnail thumbnail;
    public String type;
}
