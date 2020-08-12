package com.schoolpathram.schoolpathramdotcom.helper;

import org.json.JSONArray;
import org.json.JSONException;

public interface VolleyCallback {
    void onSuccess(JSONArray result) throws JSONException;


    void onError(String result) throws Exception;
}