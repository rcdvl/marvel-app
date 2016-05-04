package com.rcdvl.marvel.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Created by renan on 3/17/16.
 * Check http://www.androidauthority.com/how-to-hide-your-api-key-in-android-600583/ for where to store api keys
 */
object MarvelServiceManager {

    val PRIVATE_KEY = "<private-key-here>"
    val PUBLIC_KEY = "<public-key-here>"

    val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->

        val timestamp = System.currentTimeMillis()
        val beforeHash: String = "$timestamp$PRIVATE_KEY$PUBLIC_KEY"
        val hash = BigInteger(1, MessageDigest.getInstance("MD5").digest(beforeHash.toByteArray())).toString(16)

        val url = chain?.request()?.url()?.newBuilder()?.addQueryParameter("ts", "$timestamp")
                ?.addQueryParameter("hash", hash)?.addQueryParameter("apikey", PUBLIC_KEY)?.build()
        val request = chain?.request()?.newBuilder()?.url(url)?.build()

        chain?.proceed(request)
    }.build()

    val marvelService: MarvelService = Retrofit.Builder()
            .baseUrl("http://gateway.marvel.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelService::class.java)

    fun buildUri(url: String): String {
        val timestamp = System.currentTimeMillis()
        val beforeHash: String = "$timestamp$PRIVATE_KEY$PUBLIC_KEY"
        val hash = BigInteger(1, MessageDigest.getInstance("MD5").digest(beforeHash.toByteArray())).toString(16)

        return "$url?ts=$timestamp&hash=$hash&apikey=$PUBLIC_KEY"
    }
}