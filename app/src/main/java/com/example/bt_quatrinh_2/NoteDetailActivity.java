package com.example.bt_quatrinh_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<note> noteArrayList;
//    public EditText titled = findViewById(R.id.title_a);
//    public EditText contented = findViewById(R.id.content_a);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater im = getMenuInflater();
        im.inflate(R.menu.option_menu_new,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saved){
            Intent intent = new Intent(this, MainActivity.class);
 //           intent.putExtra("title", titled.getText().toString());
 //           intent.putExtra("content", contented.getText().toString());
 //           intent.putExtra("update", 1);
            startActivity(intent);
        }
        return true;
    }
}