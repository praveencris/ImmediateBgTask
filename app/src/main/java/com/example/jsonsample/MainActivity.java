package com.example.jsonsample;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText numberEditText;
    private Button startServiceButton;
    private Button stopServiceButton;

    private final Handler handler = new Handler(Looper.myLooper());
    private Runnable runnable;
    private final long delayInMillis = 20000;
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberEditText = findViewById(R.id.numberEt);
        startServiceButton = findViewById(R.id.startButton);
        stopServiceButton = findViewById(R.id.stopButton);

        startServiceButton.setOnClickListener(this);
        stopServiceButton.setOnClickListener(this);

        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onClick(View v) {
        // Instantiate the RequestQueue.
        if (v.getId() == R.id.startButton) {
            registerForBackgroundCall();
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    //Log.d("TAG", "Run after 5 seconds");
                    //Toast.makeText(MainActivity.this, "Run after 20 seconds", Toast.LENGTH_SHORT).show();
                    registerForBackgroundCall();
                    handler.postDelayed(runnable, delayInMillis);
                }
            }, delayInMillis);

        } else {
            if (runnable != null)
                handler.removeCallbacks(runnable);
            LocalBroadcastManager.getInstance(MainActivity.this)
                    .unregisterReceiver(alarmReceiver);
        }
    }

    private void registerForBackgroundCall() {
        Intent intent = new Intent();
        intent.setAction("action.CALL_SERVICE");
        LocalBroadcastManager.getInstance(MainActivity.this)
                .sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (runnable != null)
            handler.removeCallbacks(runnable);
        LocalBroadcastManager.getInstance(MainActivity.this)
                .unregisterReceiver(alarmReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MainActivity.this)
                .registerReceiver(alarmReceiver,
                        new IntentFilter("action.CALL_SERVICE"));
    }
}