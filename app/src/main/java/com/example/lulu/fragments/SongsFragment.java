package com.example.lulu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.SongAdapter;
import com.example.lulu.classes.Song;
import com.example.lulu.classes.User;
import com.example.lulu.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.lulu.utils.FirebaseHelper.artistSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.likedSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.mAuth;
import static com.example.lulu.utils.FirebaseHelper.mStorageRef;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageView singerImage;
    private TextView singerName;
    private List<Song> list;

    private User singer;


    public SongsFragment() {
        // Required empty public constructor
    }

    public static SongsFragment newInstance(User singer) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putSerializable("artist", singer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        singer = (User) getArguments().getSerializable("artist");
        initializeViews(view);
        populateRecyclerView(artistSongsDatabase.child(singer.getUuid()));
        updateUI(FirebaseHelper.userDatabase.child(singer.getUuid()));
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rv_singer_songs);
        singerImage = view.findViewById(R.id.singer_image);
        singerName = view.findViewById(R.id.fragment_song_singer_name_tv);
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
                        SongAdapter songAdapter = new SongAdapter((ArrayList<Song>) list, getContext());
                        recyclerView.setAdapter(songAdapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void updateUI(DatabaseReference currentSingerBdReference) {
        singerName.setText(singer.getName());
        StorageReference ref = mStorageRef.child("users/" + singer.getUuid() + "/profile.jpg");
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get()
                .load(uri)
                .into(singerImage);
        }).addOnFailureListener(e -> {
            Log.println(Log.DEBUG,"ui update", "update ui" + e.getStackTrace().toString());
        });
    }
}
