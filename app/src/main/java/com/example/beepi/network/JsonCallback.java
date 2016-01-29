package com.example.beepi.network;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by larwang on 1/29/16.
 */
public abstract class JsonCallback<T> implements Callback {
    private final Handler mHandler;
    private final Class<T> mClass;

    public JsonCallback(Context context, Class<T> cls) {
        mHandler = new Handler(context.getMainLooper());
        mClass = cls;
    }

    protected abstract void onResult(T result);
    protected abstract void onFailure(IOException e);

    @Override
    public final void onResponse(final Call call, final Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        final T result = new Gson().fromJson(response.body().charStream(), mClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onResult(result);
            }
        });
    }

    @Override
    public final void onFailure(final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(e);
            }
        });
    }
}
