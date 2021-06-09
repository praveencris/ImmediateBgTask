package com.example.jsonsample;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText numberEditText;
    private Button startServiceButton;
    private Button stopServiceButton;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

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
        if (v.getId() == R.id.startButton) {
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    registerForBackgroundCall();
                    //Optional if want to display something on main thread
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Timer triggered!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 0, delayInMillis, TimeUnit.MILLISECONDS);
        } else {
            executor.shutdown();
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