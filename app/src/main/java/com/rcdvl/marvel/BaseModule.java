package com.rcdvl.marvel;

import android.text.TextUtils;

import com.rcdvl.marvel.model.MarvelApi;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by renan on 13/06/16.
 */
public class BaseModule {
    private static String PRIVATE_KEY = "";
    private static String PUBLIC_KEY = "";
    private static MarvelApi marvelApi;

    public static MarvelApi marvelApi() {
        if (marvelApi != null) {
            return marvelApi;
        }

        if (TextUtils.isEmpty(PRIVATE_KEY)) {
            throw new RuntimeException("Please set the PRIVATE_KEY constant in BaseModule class");
        }

        if (TextUtils.isEmpty(PUBLIC_KEY)) {
            throw new RuntimeException("Please set the PUBLIC_KEY constant in BaseModule class");
        }

        OkHttpClient client = new Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long timestamp = System.currentTimeMillis();
                String beforeHash = "" + timestamp + PRIVATE_KEY + PUBLIC_KEY;
                String hash;
                try {
                    hash = new BigInteger(1, MessageDigest.getInstance("MD5").digest(beforeHash.getBytes())).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    hash = "";
                }

                HttpUrl url = chain.request().url().newBuilder().addQueryParameter("ts", String.valueOf(timestamp)).addQueryParameter("hash", hash).addQueryParameter("apikey", PUBLIC_KEY).build();
                Request request = chain.request().newBuilder().url(url).build();

                return chain.proceed(request);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl("http://gateway.marvel.com/").addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        marvelApi = retrofit.create(MarvelApi.class);
        return marvelApi;
    }
}
