package com.example.jsonsample;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
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

/**
 * Example implementation of a JobIntentService.
 */
public class NetworkJobIntentService extends JobIntentService {
    private final static String URL = "https://content.guardianapis.com/search?page=2&q=debate&api-key=test";
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private final long delayInMillis = 20000;

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NetworkJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent != null && intent.getIntExtra("EXTRA_FLAG", 0) == 1) {
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    toast("Timer Triggered!!");
                    performNetworkOperation(NetworkJobIntentService.this, URL);
                }
            }, 0, delayInMillis, TimeUnit.MILLISECONDS);
        } else if (intent != null && intent.getIntExtra("EXTRA_FLAG", 0) == 2) {
            executor.shutdown();
            toast("Service Stopped!!");
        }
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        toast("All work complete");
    }


    // Helper for showing tests
    void toast(final CharSequence text) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NetworkJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
