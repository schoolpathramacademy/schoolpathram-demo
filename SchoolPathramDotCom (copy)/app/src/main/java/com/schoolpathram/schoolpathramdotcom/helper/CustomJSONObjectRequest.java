package com.schoolpathram.schoolpathramdotcom.helper;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CustomJSONObjectRequest extends Request<JSONArray> {

    private Response.Listener<JSONArray> listener;
    private Map<String, String> params;
    Priority mPriority;

    public CustomJSONObjectRequest(int method, String url, Map<String, String> params,
                                   Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener) {
        super(method, url, (Response.ErrorListener) errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        String jsonString = "";
        try {
            jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
//            Gson gson = new Gson();
//            gson.fromJson(jsonString, JsonObject);

//            JSONArray obj = new JSONArray(response);


            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonString);
                return Response.success(jsonArray,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
                        return Response.error(new ParseError(je));


        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);

        Log.v("Response", response.toString());
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    public Priority getPriority() {
        // If we didn't use the setPriority method,
        // the priority is automatically set to NORMAL
        return mPriority != null ? mPriority : Priority.NORMAL;
    }

}