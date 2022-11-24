package com.example.bt_quatrinh_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.VoiceInteractor;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<note> noteArrayList;
    private EditText titled, contented, timer;
    private Button btn;
    private ImageView imageView;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath = null;
    FirebaseFirestore db;
    StorageReference storageRef;
    private String downloadImageUrl = "";
    note n;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        titled = findViewById(R.id.title_a);
        contented = findViewById(R.id.content_a);
        timer =findViewById(R.id.timer_a);
        imageView = findViewById(R.id.anh);
        n = new note();
        Intent intent = getIntent();
        String a = intent.getStringExtra("title_b");
        String b = intent.getStringExtra("content_b");
        String c = intent.getStringExtra("timer_b");
        int d = getIntent().getExtras().getInt("gio");
        int e = getIntent().getExtras().getInt("phut");
        titled.setText(a);
        contented.setText(b);
        timer.setText(c);
        timer.setText((d +":"+ e));
        btn = findViewById(R.id.btn_a);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

            }
        });
        final Button btn = findViewById(R.id.btn_time);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NoteDetailActivity.this,TimePickerActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void setDatabase(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("note");
        note n = new note();
        String key = myRef.push().getKey();
        String date = DateFormat.getDateTimeInstance().format(new Date());
        n.setDate(date);
        n.setTimer(timer.getText().toString());
        n.setImg("img");
        n.setDescription(contented.getText().toString());
        n.setName(titled.getText().toString());
        n.setId(key);
        myRef.child(key).setValue(n).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {finish();}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater im = getMenuInflater();
        im.inflate(R.menu.option_menu_new,menu);
        MenuItem down = menu.findItem(R.id.delete_q);
        down.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(NoteDetailActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
        MenuItem save23 = menu.findItem(R.id.saved_q);
        save23.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setDatabase();
                return false;
            }
        });
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath));
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void UploadImage() {
        if (filePath != null) {

            final StorageReference childRef = storageRef.child("IMAGES").child(System.currentTimeMillis() + ".jpg");
            final UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageUrl = childRef.getDownloadUrl().toString();
                            return childRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Log.d("imagUrl", downloadImageUrl);
                                n.setImg(downloadImageUrl);
                                SaveInfoToDatabase();
                            }
                        }
                    });
                }
            });

        } else {
            Toast.makeText(NoteDetailActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }
    private void SaveInfoToDatabase() {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("note");
        note n = new note();
        String key = myRef.push().getKey();
        String date = DateFormat.getDateTimeInstance().format(new Date());
        n.setDate(date);
        n.setTimer(timer.getText().toString());
        n.setDescription(contented.getText().toString());
        n.setName(titled.getText().toString());
        n.setId(key);
        myRef.child(key).setValue(n).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {finish();}
        });
    }
}