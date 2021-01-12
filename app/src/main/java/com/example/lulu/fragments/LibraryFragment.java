package com.example.lulu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.SongAdapter;
import com.example.lulu.classes.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.lulu.FirebaseHelper.likedSongsDatabase;
import static com.example.lulu.FirebaseHelper.mAuth;

public class LibraryFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Song> favoriteSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library,container,false);
        initializeViews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        likedSongsDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    favoriteSongs = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        favoriteSongs.add(ds.getValue(Song.class));
                    }
                    SongAdapter songAdapter = new SongAdapter(favoriteSongs, view.getContext());
                    recyclerView.setAdapter(songAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initializeViews() {
        recyclerView = view.findViewById(R.id.rv_lib_songs);
    }
}
