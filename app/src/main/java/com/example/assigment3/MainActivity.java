package com.example.assigment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvTitle;
    ArrayList<String> arrayTitle,arrayLink;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayTitle = new ArrayList<>();
        arrayTitle.add("Vitamin");
        arrayTitle.add("Protein");
        arrayTitle.add("Amino Acid");
        arrayTitle.add("Grains and Starches");
        arrayTitle.add("Fibers and Legumes");
        arrayTitle.add("Minerals");


        arrayLink = new ArrayList<>();
        arrayLink.add("https://www.petfoodindustry.com/rss/topic/296-vitamins");
        arrayLink.add("https://www.petfoodindustry.com/rss/topic/292-proteins");
        arrayLink.add("https://www.petfoodindustry.com/rss/topic/293-amino-acids");
        arrayLink.add("https://www.petfoodindustry.com/rss/topic/294-grains-and-starches");
        arrayLink.add("https://www.petfoodindustry.com/rss/topic/295-fibers-and-legumes");
        arrayLink.add("https://www.petfoodindustry.com/rss/topic/297-minerals");


        lvTitle = (ListView) findViewById(R.id.list_view);

        adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,arrayTitle);
        lvTitle.setAdapter(adapter);

        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ListItemActivity.class);
                intent.putExtra("linkrss",arrayLink.get(position));
                startActivity(intent);

            }
        });
    }
}