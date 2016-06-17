package com.rcdvl.marvel.model;

import com.rcdvl.marvel.model.api.MarvelCharacter;
import com.rcdvl.marvel.model.api.MarvelResource;
import com.rcdvl.marvel.model.api.MarvelResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface MarvelApi {
    @GET("/v1/public/characters")
    Observable<MarvelResponse<MarvelCharacter>> getCharactersList(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/v1/public/characters")
    Observable<MarvelResponse<MarvelCharacter>> getCharactersList(@Query("offset") int offset, @Query("limit") int limit, @Query("nameStartsWith") String name);

    @GET("/v1/public/characters/{id}/{resource}")
    Observable<MarvelResponse<MarvelResource>> getCharacterResourceList(@Path("resource") String res, @Path("id") long characterId, @Query("offset") int offset, @Query("limit") int limit);
//
//    @GET("/v1/public/{resource}/{id}")
//    Call<MarvelResponse<>> getResourceDetails(@Path("resource") res: String, @Path("id") id: String): Call<MarvelResponse<MarvelResource>>
}
