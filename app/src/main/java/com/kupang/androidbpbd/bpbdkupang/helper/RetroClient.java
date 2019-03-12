package com.kupang.androidbpbd.bpbdkupang.helper;


import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.model.FileApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(3000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    public RetroClient() {

    }

    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(Constants.ROOT_URL_UPLOAD_IMAGE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static FileApi getApiService() {
        return getRetroClient().create(FileApi.class);
    }
}