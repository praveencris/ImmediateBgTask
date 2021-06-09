package com.example.jsonsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AlarmReceiver extends BroadcastReceiver {
    private final static String URL = "https://content.guardianapis.com/search?page=2&q=debate&api-key=test";

    @Override
    public void onReceive(Context context, Intent intent) {
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("TAG", "Response is: " + response.substring(0, 100));
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.pristine_609);
                        mediaPlayer.start(); // no need to call prepare(); create() does that for you
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
