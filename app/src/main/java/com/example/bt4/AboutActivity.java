package com.example.bt4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.time.Year;
import java.util.Calendar;

public class AboutActivity extends AppCompatActivity {

    TextView copyright, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        copyright = findViewById(R.id.copyright);
        copyright.setText("@" + Calendar.getInstance().get(Calendar.YEAR) + ", Team 18");

        version = findViewById(R.id.version);
        version.setText("Version 1.0");
    }
}