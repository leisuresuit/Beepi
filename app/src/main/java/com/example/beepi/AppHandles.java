package com.example.beepi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by larwang on 7/13/15.
 */
public class AppHandles {

    private static AppHandles sInstance;

    public static AppHandles initInstance(Activity activity) {
        if (sInstance == null) {
            sInstance = new AppHandles(activity);
        }
        return sInstance;
    }

    public static AppHandles getInstance() {
        return sInstance;
    }

    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;

    private AppHandles(Activity activity) {
        Cache cache = new DiskBasedCache(activity.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void addToRequestQueue(Request request) {
        sInstance.getRequestQueue().add(request);
    }

}
