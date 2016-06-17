package com.rcdvl.marvel.model.api;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class MarvelCharacter {
    public long id;
    public String name;
    public String description;
    public String modified;
    public MarvelThumbnail thumbnail;
    public List<MarvelUrl> urls;
}
