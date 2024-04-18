package com.mini.program.miniTest;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mini.program.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecaseWeather {
    public static String    v_statusApi = "", v_apiresponse= "";
    static int      v_timeout = 2500 * 15, v_retries = 1;
    Context         context;
    JSONObject      jsonBody;

    public ForecaseWeather(Context context) {this.context = context;}

    public void loadGetAPI() {
        StringRequest sr = new StringRequest(Request.Method.GET,
                context.getString(R.string.URL_API),
                response -> {
                    try {
                        jsonBody = new JSONObject(response);
                        if (jsonBody.getString("cod").equals("200")) {
                            v_statusApi = "JSONOK";
                            v_apiresponse = response;
                        } else v_statusApi = "ERROR";
                    } catch (JSONException e) {
                        v_statusApi = "ERROR";
                    }
                },
                error -> {
                    v_statusApi = "ERROR";
                    Toast.makeText(context, "Unable to connect to server",
                            Toast.LENGTH_SHORT).show();
                });
        sr.setRetryPolicy(new DefaultRetryPolicy(v_timeout, v_retries, 1f));
        Volley.newRequestQueue(context).add(sr);
    }
}
