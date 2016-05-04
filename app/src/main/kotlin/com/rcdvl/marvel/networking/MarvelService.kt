package com.rcdvl.marvel.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by renan on 3/17/16.
 */
interface MarvelService {

    @GET("/v1/public/characters")
    fun getCharactersList(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<MarvelResponse<MarvelCharacter>>

    @GET("/v1/public/characters")
    fun getCharactersList(@Query("offset") offset: Int, @Query("limit") limit: Int, @Query("nameStartsWith") name: String): Call<MarvelResponse<MarvelCharacter>>

    @GET("/v1/public/characters/{id}/{resource}")
    fun getCharacterResourceList(@Path("resource") res: String, @Path("id") id: Long,
                                 @Query("offset") offset: Int, @Query("limit") limit: Int): Call<MarvelResponse<MarvelResource>>

    @GET("/v1/public/{resource}/{id}")
    fun getResourceDetails(@Path("resource") res: String, @Path("id") id: String): Call<MarvelResponse<MarvelResource>>
}