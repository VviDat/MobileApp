package com.example.bt1_giuaki;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txt1, txt2, txt3, txt4, txt5;
    SeekBar sliderChangeColor;
    private int color = 255;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void ChangeColor(TextView txt1, TextView txt2, TextView txt3, TextView txt4, TextView txt5, SeekBar sb){
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                color = i;
                txt1.setBackgroundColor(Color.rgb(color,100,200));
                txt2.setBackgroundColor(Color.rgb(111,color,255));
                txt3.setBackgroundColor(Color.rgb(color,220,color));
                txt4.setBackgroundColor(Color.rgb(color,color,111));
                txt5.setBackgroundColor(Color.rgb(121,133,color));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.linear:
                setContentView(R.layout.linear_layout);
                txt1 = (TextView) findViewById(R.id.tv1);
                txt2 = (TextView) findViewById(R.id.tv2);
                txt3 = (TextView) findViewById(R.id.tv3);
                txt4 = (TextView) findViewById(R.id.tv4);
                txt5 = (TextView) findViewById(R.id.tv5);
                sliderChangeColor = (SeekBar) findViewById(R.id.sliderChangeColor);
                ChangeColor(txt1,txt2,txt3,txt4,txt5,sliderChangeColor);
                Toast.makeText(this, ""+ "Linear", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relative:
                setContentView(R.layout.realtive_layout);
                txt1 = (TextView) findViewById(R.id.tv11);
                txt2 = (TextView) findViewById(R.id.tv22);
                txt3 = (TextView) findViewById(R.id.tv33);
                txt4 = (TextView) findViewById(R.id.tv44);
                txt5 = (TextView) findViewById(R.id.tv55);
                sliderChangeColor = (SeekBar) findViewById(R.id.sliderChangeColor1);
                ChangeColor(txt1,txt2,txt3,txt4,txt5,sliderChangeColor);
                Toast.makeText(this, ""+ "Relative", Toast.LENGTH_SHORT).show();
                break;
            case R.id.table:
                setContentView(R.layout.table_layout);
                txt1 = (TextView) findViewById(R.id.tv111);
                txt2 = (TextView) findViewById(R.id.tv222);
                txt3 = (TextView) findViewById(R.id.tv333);
                txt4 = (TextView) findViewById(R.id.tv444);
                txt5 = (TextView) findViewById(R.id.tv555);
                sliderChangeColor = (SeekBar) findViewById(R.id.sliderChangeColor2);
                ChangeColor(txt1,txt2,txt3,txt4,txt5,sliderChangeColor);
                Toast.makeText(this, ""+ "Table", Toast.LENGTH_SHORT).show();
                break;
            case R.id.message:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Nhóm bài tập mobile app gồm các thành viên:\n" +
                        "\t Vi Đào Tiến Đạt - 3120410125\n\t Phạm Văn Hưng - 3120410225\n\t Lưu Trường An - 3120410018\n\t Nguyễn Minh Tuấn - 3120410588");
                alertDialog.setPositiveButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog.setNegativeButton("More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);
        txt1 = (TextView) findViewById(R.id.tv1);
        txt2 = (TextView) findViewById(R.id.tv2);
        txt3 = (TextView) findViewById(R.id.tv3);
        txt4 = (TextView) findViewById(R.id.tv4);
        txt5 = (TextView) findViewById(R.id.tv5);
        sliderChangeColor = (SeekBar) findViewById(R.id.sliderChangeColor);
        ChangeColor(txt1,txt2,txt3,txt4,txt5,sliderChangeColor);

    }


}