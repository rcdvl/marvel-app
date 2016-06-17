package com.rcdvl.marvel.model.api;

/**
 * Created by renan on 13/06/16.
 */
public class MarvelResponse <T> {
    public String code;
    public String status;
    public String copyright;
    public String attributionText;
    public String attributionHTML;
    public String etag;
    public MarvelData<T> data;
}
