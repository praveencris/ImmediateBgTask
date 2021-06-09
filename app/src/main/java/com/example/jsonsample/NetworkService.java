package com.example.jsonsample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkService extends Service {
    private final static String URL = "https://content.guardianapis.com/search?page=2&q=debate&api-key=test";
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private final long delayInMillis = 20000;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getIntExtra("EXTRA_FLAG", 0) == 1) {
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NetworkService.this, "Timer Triggered!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    performNetworkOperation(NetworkService.this, URL);
                }
            }, 0, delayInMillis, TimeUnit.MILLISECONDS);
        } else if (intent != null && intent.getIntExtra("EXTRA_FLAG", 0) == 2) {
            executor.shutdown();
            stopSelf();
            Toast.makeText(NetworkService.this, "Service Stopped!!", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void performNetworkOperation(Context context, String URL) {
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
