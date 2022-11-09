package com.example.bt_quatrinh_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<note> noteArrayList;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAll();

    }



    private void initAll(){
        recyclerView = findViewById(R.id.list_note);
        noteArrayList = new ArrayList<note>();
        mAdapter = new NoteAdapter(noteArrayList,this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getDatabase();
    }
    private void getDatabase(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("note");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteArrayList.clear();
                for (DataSnapshot post : snapshot.getChildren()){
                    note n = post.getValue(note.class);
                    noteArrayList.add(n);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater im = getMenuInflater();
        im.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save){
            Intent intent = new Intent(this, NoteDetailActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.info){

        }
        return true;
    }


}