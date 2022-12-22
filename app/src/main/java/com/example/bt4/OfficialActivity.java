package com.example.bt4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OfficialActivity extends AppCompatActivity {
    LinearLayout official;
    TextView chamber, politicianName, contemporary, title;
    TextView addressContent, phoneContent, emailContent, websiteContent;
    ImageView avatar;
    ImageButton facebookBtn, twitterBtn, googleBtn, youtubeBtn;

    private final String DEFAULT_NO_DATA = "No Data Provided";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Data");
        JsonObject item = new Gson().fromJson(data,JsonObject.class);

        title = findViewById(R.id.stateTitle);
//        Log.d("object",item.get("normalizedInput").getAsString());
        title.setText(getAddress(item.get("normalizedInput").getAsJsonObject()));

        showPolitician(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showPolitician(JsonObject item) {
        chamber = findViewById(R.id.chamber);
        chamber.setText(item.get("office").getAsString());

        politicianName = findViewById(R.id.politicianName);
        politicianName.setText(item.get("name").getAsString());

        contemporary = findViewById(R.id.contemporary);
        contemporary.setText("(" + item.get("party").getAsString() + ")");

        official = findViewById(R.id.official);
        official.setBackgroundColor(setColor(item.get("party").getAsString()));

        avatar = findViewById(R.id.avatar);
        setAvatarImg(item.get("photoUrl"));

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(getApplicationContext(),PhotoActivity.class);
                photoIntent.putExtra("chamber",item.get("office").getAsString());
                photoIntent.putExtra("politicianName",item.get("name").getAsString());
                photoIntent.putExtra("color",setColor(item.get("party").getAsString()));
                photoIntent.putExtra("title",item.get("normalizedInput").toString());

                if(item.get("photoUrl") != null)
                    photoIntent.putExtra("imgURL",item.get("photoUrl").getAsString());

                startActivity(photoIntent);
            }
        });

        showInfoPolitician(item);

        facebookBtn = findViewById(R.id.facebookBtn);
        twitterBtn = findViewById(R.id.twitterBtn);
        googleBtn = findViewById(R.id.googleBtn);
        youtubeBtn = findViewById(R.id.youtubeBtn);

        if(item.get("channels") != null) {
            JsonArray channelList = item.get("channels").getAsJsonArray();
            String addressResult = "";
            for(int i = 0; i < channelList.size(); i++) {
                JsonObject channel = channelList.get(i).getAsJsonObject();
                switch (channel.get("type").getAsString()) {
                    case "GooglePlus":
                        googleBtn.setVisibility(View.VISIBLE);
                        googleBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = channel.get("id").getAsString();
                                Intent intent = null;
                                try {
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
                                    intent.putExtra("customAppUri", name);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
                                }
                            }
                        });
                        break;
                    case "Facebook":
                        facebookBtn.setVisibility(View.VISIBLE);
                        facebookBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String FACEBOOK_URL = "https://www.facebook.com/"  + channel.get("id").getAsString();
                                String urlToUse;
                                PackageManager packageManager = getPackageManager();
                                try {
                                    int versionCode = packageManager.getPackageInfo("com.facebook.katana",0).versionCode;
                                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                                }
                                catch (PackageManager.NameNotFoundException e) {
                                    urlToUse = FACEBOOK_URL; //normalweb url
                                }
                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                facebookIntent.setData(Uri.parse(urlToUse));
                                startActivity(facebookIntent);
                            }
                        });
                        break;
                    case "Twitter":
                        twitterBtn.setVisibility(View.VISIBLE);
                        twitterBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = null;
                                String name = channel.get("id").getAsString();
                                try {
                                    // get the Twitter app if possible
                                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                } catch (Exception e) {
                                    // no Twitter app, revert to browser
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
                                }
                                startActivity(intent);
                            }
                        });
                        break;
                    case "Youtube":
                        youtubeBtn.setVisibility(View.VISIBLE);
                        youtubeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = channel.get("id").getAsString();
                                Intent intent = null;
                                try {
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setPackage("com.google.android.youtube");
                                    intent.setData(Uri.parse("https://www.youtube.com/" + name)); startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
                                }
                            }
                        });
                        break;
                }
            }
        }
    }

    private void showInfoPolitician(JsonObject item) {
        //address
        addressContent = findViewById(R.id.addressContent);

        if(item.get("address") != null) {
            JsonArray addressList = item.get("address").getAsJsonArray();
            String addressResult = "";
            for(int i = 0; i < addressList.size(); i++) {
                addressResult += getAddress(addressList.get(i).getAsJsonObject()) + "\n" ;
            }
            addressContent.setText(addressResult);
        }

        //phone
        phoneContent = findViewById(R.id.phoneContent);
        if(item.get("phone") != null) {
            JsonArray phoneList = item.get("phone").getAsJsonArray();
            String phoneResult = "";
            for(int i = 0; i < phoneList.size(); i++) {
                phoneResult += phoneList.get(i).getAsString() + "\n" ;
            }
            phoneContent.setText(phoneResult);
        }

        //email
        emailContent = findViewById(R.id.emailContent);
        if(item.get("emails") != null) {
            JsonArray emailList = item.get("emails").getAsJsonArray();
            String emailResult = "";
            for(int i = 0; i < emailList.size(); i++) {
                emailResult += emailList.get(i).getAsString() + "\n" ;
            }
            emailContent.setText(emailResult);
        }

        //website
        websiteContent = findViewById(R.id.webstieContent);
        if(item.get("urls") != null) {
            JsonArray websiteList = item.get("urls").getAsJsonArray();
            String websiteResult = "";
            for(int i = 0; i < websiteList.size(); i++) {
                websiteResult += websiteList.get(i).getAsString() + "\n" ;
            }
            websiteContent.setText(websiteResult);
        }
    }

    private int setColor(String contemporary) {
        int color;
        switch (contemporary) {
            case "Republican Party":
                color = Color.RED;
                break;
            case "Democratic Party":
                color = Color.BLUE;
                break;
            default:
                color = Color.BLACK;
                break;
        }
        return color;
    }

    private void setAvatarImg(JsonElement imgURL) {
        if(imgURL == null) {
            avatar.setImageResource(R.drawable.defaultavatar);
        }
        else {
            new GetImage().execute(imgURL.getAsString());
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