package com.example.bt4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bt4.API.GoogleAPI;
import com.example.bt4.Adapter.PoliticianAdapter;
import com.example.bt4.Model.Civic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    final String APIKey = "AIzaSyBbygnIEHrjTp90nMXmpx32iWPj868Ko5A";
    String zipcode;

    RecyclerView recyclerView;
    TextView title;

    private List<JsonObject> politicianList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.stateTitle);
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        callAPI("90001");
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.search_dialog, null);
                EditText input = alertLayout.findViewById(R.id.addressInput);

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Enter a City, State or a Zip Code");
                dialog.setView(alertLayout);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callAPI(input.getText().toString());
                    }
                });
                dialog.setNegativeButton("Cancel",null);

                dialog.create().show();
                break;
        }
        return true;
    }
    //API handle
    private void callAPI(String zipcode) {
        GoogleAPI.googleApi.getCivic(zipcode,APIKey).enqueue(new Callback<Civic>() {
            @Override
            public void onResponse(Call<Civic> call, Response<Civic> response) {
                Toast.makeText(MainActivity.this,"Call Complete",Toast.LENGTH_SHORT).show();

                Civic civic = response.body();
                title.setText(getAddress(civic.getNormalizedInput()));
                merge(civic);

                politicianList = convertJsonElementToJsonObject(civic.getOfficials().asList());

                PoliticianAdapter politicianAdapter = new PoliticianAdapter(politicianList);
                recyclerView.setAdapter(politicianAdapter);
            }

            @Override
            public void onFailure(Call<Civic> call, Throwable t) {
                Log.d("Test fail","no");
                Toast.makeText(MainActivity.this,"Can't connect API",Toast.LENGTH_SHORT).show();
            }
        });
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

    private void merge(Civic civic) {
        List<JsonObject> officesList = convertJsonElementToJsonObject(civic.getOffices().asList());
        List<JsonObject> officialList = convertJsonElementToJsonObject(civic.getOfficials().asList());

        for(JsonObject office: officesList) {
            List<JsonElement> indexList = office.get("officialIndices").getAsJsonArray().asList();
            for(JsonElement index: indexList) {
                officialList.get(index.getAsInt()).addProperty("office",office.get("name").getAsString());
                officialList.get(index.getAsInt()).add("normalizedInput",civic.getNormalizedInput());
            }
        }

//        Log.d("Object",officialList.toString());

        JsonElement element = new Gson().toJsonTree(officialList,new TypeToken<List<JsonObject>>() {}.getType());

        JsonArray official = element.getAsJsonArray();
        civic.setOfficials(official);
    }

    private List<JsonObject> convertJsonElementToJsonObject(List<JsonElement> oldList) {
        List<JsonObject> newList = new ArrayList<JsonObject>();

        for(JsonElement item : oldList) {
            newList.add(item.getAsJsonObject());
        }
        return newList;
    }

}