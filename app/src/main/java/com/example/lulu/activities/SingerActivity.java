package com.example.lulu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lulu.R;
import com.example.lulu.adapters.SongAdapter;
import com.example.lulu.classes.Singer;
import com.example.lulu.classes.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.lulu.utils.FirebaseHelper.likedSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.mAuth;
import static com.example.lulu.utils.FirebaseHelper.mSingersImagesRef;
import static com.example.lulu.utils.FirebaseHelper.artistSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.singerDatabase;
import static com.example.lulu.utils.FirebaseHelper.userDatabase;

public class SingerActivity extends AppCompatActivity {
    private TextView singerName;
    private ImageView singerImage;
    private RecyclerView recyclerView;

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
        populateRecyclerView(artistSongsDatabase.child(singerUUID));

    }

    private void populateRecyclerView(DatabaseReference ref) {
        ArrayList<Song> favoriteSongs = new ArrayList<>();
        likedSongsDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        favoriteSongs.add(ds.getValue(Song.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Song song = ds.getValue(Song.class);
                            if(favoriteSongs.contains(song))
                                song.setFavourite(true);
                            else song.setFavourite(false);
                            list.add(song);
                        }
                        SongAdapter songAdapter = new SongAdapter(list, getApplicationContext());
                        recyclerView.setAdapter(songAdapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void initializeViews() {
        singerName = findViewById(R.id.singer_name);
        singerImage = findViewById(R.id.singer_image);
        recyclerView = findViewById(R.id.rv_singer);
    }


    private void updateUI(DatabaseReference currentSingerBdReference) {
        currentSingerBdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Singer currentSinger = snapshot.getValue(Singer.class);
                singerName.setText(currentSinger.getName());
                StorageReference ref = mSingersImagesRef.child(currentSinger.getPurl());
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(singerImage.getContext())
                                .load(uri)
                                .into(singerImage);
                }).addOnFailureListener(e -> {
                    Log.println(Log.DEBUG,"pula", "update ui" + e.getStackTrace().toString());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}