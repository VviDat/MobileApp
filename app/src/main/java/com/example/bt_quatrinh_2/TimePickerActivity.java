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
    private TextView textView;
    private TimePicker timePicker;
    private Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker);
        btn = findViewById(R.id.btn_time);
        textView = findViewById(R.id.textview);
        timePicker = (TimePicker) findViewById(R.id.timepicker_a);
        calendar = calendar.getInstance();
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        final Intent intent = new Intent(this, AlarmReceiver.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();

                textView.setText("Giờ bạn đặt là"+ gio +":"+ phut);
                pendingIntent = PendingIntent.getBroadcast(TimePickerActivity.this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Intent intent = new Intent(TimePickerActivity.this, NoteDetailActivity.class);
                intent.putExtra("gio",gio);
                intent.putExtra("phut",phut);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater im = getMenuInflater();
        im.inflate(R.menu.option_menu,menu);
        return true;
    }
}
