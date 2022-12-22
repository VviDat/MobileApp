package com.example.bt_quatrinh_2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TimePickerActivity extends AppCompatActivity {
    private Button btn;
    private TimePicker timePicker;
    private Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker);
        btn = findViewById(R.id.btn_time);
        timePicker = (TimePicker) findViewById(R.id.timepicker_a);
        calendar = calendar.getInstance();
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();
                String x = getIntent().getStringExtra("title_b");
                String y = getIntent().getStringExtra("content_b");
                String z = gio + ":" + phut;
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Intent intent = new Intent(TimePickerActivity.this, NoteDetailActivity.class);
                intent.putExtra("title_b", x);
                intent.putExtra("content_b", y);
                intent.putExtra("timer_b", z);
                startActivity(intent);
                finish();
            }
        });
    }
}
