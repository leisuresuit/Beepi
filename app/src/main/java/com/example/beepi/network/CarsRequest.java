package com.example.beepi.network;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by larwang on 12/8/15.
 */
public class CarsRequest {
    private static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private CarsRequest() {}

    public static Request build(String query) {
        return new Request.Builder()
                .url("https://www.beepi.com/v1/listings/carsSearch")
                .post(RequestBody.create(MEDIA_TYPE_JSON, formatJsonRequest(query)))
                .build();
    }

    private static String formatJsonRequest(String query) {
        return new JSONObject().toString();
    }

}
