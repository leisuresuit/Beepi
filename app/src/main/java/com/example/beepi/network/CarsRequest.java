package com.example.beepi.network;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.example.beepi.model.CarsResponse;

import org.json.JSONObject;

/**
 * Created by larwang on 12/8/15.
 */
public class CarsRequest extends GsonRequest<CarsResponse> {

    public CarsRequest(String query, Listener<CarsResponse> listener, Response.ErrorListener errorListener) {
        super(Method.POST, "https://www.beepi.com/v1/listings/carsSearch", CarsResponse.class, null, formatJsonRequest(query), listener, errorListener);
    }

    private static JSONObject formatJsonRequest(String query) {
        return new JSONObject();
    }

}
