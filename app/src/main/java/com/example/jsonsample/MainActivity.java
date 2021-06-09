package com.example.jsonsample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText numberEditText;
    private Button startServiceButton;
    private Button stopServiceButton;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberEditText = findViewById(R.id.numberEt);
        startServiceButton = findViewById(R.id.startButton);
        stopServiceButton = findViewById(R.id.stopButton);

        startServiceButton.setOnClickListener(this);
        stopServiceButton.setOnClickListener(this);

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
    }


    @Override
    public void onClick(View v) {
        // Instantiate the RequestQueue.
        if (v.getId() == R.id.startButton) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
         /*   calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 30);*/
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), 20 * 1000, alarmIntent);
        } else {
            // If the alarm has been set, cancel it.
            if (alarmMgr != null) {
                alarmMgr.cancel(alarmIntent);
                Log.d("TAG","Alarm Cancelled!!");
            }
        }
    }
}