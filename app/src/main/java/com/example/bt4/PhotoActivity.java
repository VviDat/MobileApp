package com.example.bt4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhotoActivity extends AppCompatActivity {
    Intent intent;

    LinearLayout photo;
    TextView chamber, politicianName, title;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        intent = getIntent();

        String data = intent.getStringExtra("title");
        JsonObject item = new Gson().fromJson(data,JsonObject.class);

        title = findViewById(R.id.stateTitle);
        title.setText(getAddress(item));

        photo = findViewById(R.id.photo);
        photo.setBackgroundColor(intent.getIntExtra("color", Color.WHITE));

        chamber = findViewById(R.id.chamberPhoto);
        chamber.setText(intent.getStringExtra("chamber"));

        politicianName = findViewById(R.id.politicianNamePhoto);
        politicianName.setText(intent.getStringExtra("politicianName"));

        avatar = findViewById(R.id.avatarPhoto);
        setAvatarImg(intent.getStringExtra("imgURL"));
    }

    private void setAvatarImg(String imgURL) {

        if(imgURL == null) {
            avatar.setImageResource(R.drawable.defaultavatar);
        }
        else {
            new GetImage().execute(imgURL);
        }
    }

    private class GetImage extends AsyncTask<String, Void, byte[]> {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        @Override
        protected void onPreExecute() {
            avatar.setImageResource(R.drawable.loadingscreen);
        }

        @Override
        protected byte[] doInBackground(String... strings) {
            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]);

            Request request = builder.build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().bytes();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            }

            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if(bytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                avatar.setImageBitmap(bitmap);
            }
        }
    }

    private String getAddress(JsonObject item) {
        if(item != null) {
            String line1 = item.get("line1") != null ? item.get("line1").getAsString() + " ": "";
            String line2 = item.get("line2") != null ? item.get("line2").getAsString() + " ": "";
            String line3 = item.get("line3") != null ? item.get("line3").getAsString() + " ": "";
            String city = item.get("city").getAsString();
            String state = item.get("state").getAsString();
            String zip = item.get("zip").getAsString();
            return line1 + line2 + line3 + city + ", " + state + " " + zip;
        }
        return "No Data For Location";
    }
}