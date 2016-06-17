package com.rcdvl.marvel.model.api;

import org.parceler.Parcel;

@Parcel
public class MarvelThumbnail {
    public String path;
    public String extension;

    public String getImageUrl() {
        return path + "." + extension;
    }
}
