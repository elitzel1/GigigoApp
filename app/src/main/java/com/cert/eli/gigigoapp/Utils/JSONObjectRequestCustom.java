package com.cert.eli.gigigoapp.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cert.eli.gigigoapp.Utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eli on 05/08/15.
 */
public class JSONObjectRequestCustom extends JsonObjectRequest {

    public JSONObjectRequestCustom(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("Authorization", "Client-ID "+ Constants.CLIENT_ID);
        headers.put("Content-Type", "application/json; charset=utf-8");

        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return super.getRetryPolicy();
    }
}
