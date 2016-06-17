package com.rcdvl.marvel.model.api;

import java.util.ArrayList;

/**
 * Created by renan on 13/06/16.
 */
public class MarvelData <T> {
    public int offset;
    public int limit;
    public int total;
    public int count;
    public ArrayList<T> results;
}
