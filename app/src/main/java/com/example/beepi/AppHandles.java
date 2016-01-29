package com.example.beepi;

import okhttp3.OkHttpClient;

/**
 * Created by larwang on 7/13/15.
 */
public class AppHandles {

    private static AppHandles sInstance;

    public static AppHandles initInstance() {
        if (sInstance == null) {
            sInstance = new AppHandles();
        }
        return sInstance;
    }

    public static AppHandles getInstance() {
        return sInstance;
    }

    private final OkHttpClient mHttpClient = new OkHttpClient();

    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }

}
