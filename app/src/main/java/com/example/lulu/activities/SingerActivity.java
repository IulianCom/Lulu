package com.example.lulu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lulu.R;
import com.example.lulu.adapters.SingerAdapter;
import com.example.lulu.adapters.SongAdapter;
import com.example.lulu.classes.Singer;
import com.example.lulu.classes.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.lulu.FirebaseHelper.adminUUID;
import static com.example.lulu.FirebaseHelper.mAuth;
import static com.example.lulu.FirebaseHelper.mSingersImagesRef;
import static com.example.lulu.FirebaseHelper.singerDatabase;
import static com.example.lulu.FirebaseHelper.songsDatabase;

public class SingerActivity extends AppCompatActivity {
    private TextView singerName;
    private ImageView singerImage;
    private RecyclerView recyclerView;
    private LinearLayout adminLl;
    private EditText newSongNameEt;
    private EditText newSongLinkEt;
    private Button addBtn;
    private String singerUUID;
    private ArrayList<Song> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);

        Intent intent = this.getIntent();
        singerUUID = intent.getStringExtra("uuid");

        initializeViews();
        updateUI(singerDatabase.child(singerUUID));
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateRecyclerView(songsDatabase.child(singerUUID));

    }

    private void populateRecyclerView(DatabaseReference ref) {
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            list.add(ds.getValue(Song.class));
                        }
                        SongAdapter songAdapter = new SongAdapter(list, getApplicationContext());
                        recyclerView.setAdapter(songAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Toast.makeText(SearchFragment.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeViews() {
        singerName = findViewById(R.id.singer_name);
        singerImage = findViewById(R.id.singer_image);
        recyclerView = findViewById(R.id.rv_singer);

        if(mAuth.getCurrentUser().getUid().equals(adminUUID)) {
            adminLl = findViewById(R.id.ll_add_song);
            adminLl.setVisibility(View.VISIBLE);
            newSongNameEt = findViewById(R.id.et_song_name);
            newSongLinkEt = findViewById(R.id.et_song_link);
            addBtn = findViewById(R.id.btn_add_song);

            addBtn.setOnClickListener(view -> {
                completeForm(newSongNameEt.getText().toString(), newSongLinkEt.getText().toString());
            });
        }
    }

    private void completeForm(String songName, String songLink) {
        if(songName.isEmpty() || songLink.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Both fields must not be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            String randomUUID = UUID.randomUUID().toString();
            songsDatabase.child(singerUUID).child(randomUUID).setValue(new Song(songName, songLink, randomUUID));
            newSongNameEt.setText("");
            newSongLinkEt.setText("");
        }
    }

    private void updateUI(DatabaseReference currentSingerBdReference) {
        currentSingerBdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Singer currentSinger = snapshot.getValue(Singer.class);
                singerName.setText(currentSinger.getName());
                StorageReference ref = mSingersImagesRef.child(currentSinger.getPurl());
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(singerImage.getContext())
                                .load(uri)
                                .into(singerImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}