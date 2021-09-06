package com.example.lulu.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lulu.R;
import com.example.lulu.adapters.ArtistSongAdapter;
import com.example.lulu.adapters.SongAdapter;
import com.example.lulu.classes.ArtistSong;
import com.example.lulu.classes.Song;
import com.example.lulu.classes.User;
import com.example.lulu.utils.FirebaseHelper;
import com.example.lulu.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.lulu.utils.FirebaseHelper.mAuth;

public class ArtistLibraryFragment extends Fragment {

    private static final int REQUEST_CODE_FOR_AUDIO_FILES = 9;

    private View view;
    private RecyclerView recyclerView;
    private Button mAddSongBtn;
    private User user;

    private ArrayList<ArtistSong> artistSongs;

    public static ArtistLibraryFragment newInstance(User user) {
        ArtistLibraryFragment fragment = new ArtistLibraryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist_library,container,false);
        user = (User) getArguments().getSerializable("user");
        initializeViews();
        return view;
    }

    private void initializeViews() {
        mAddSongBtn = view.findViewById(R.id.btn_add_song);
        recyclerView = view.findViewById(R.id.rv_fragment_artist_library_songs);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseHelper.artistSongsDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    artistSongs = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        artistSongs.add(ds.getValue(ArtistSong.class));
                    }
                    ArtistSongAdapter artistSongAdapter = new ArtistSongAdapter(artistSongs, view.getContext());
                    recyclerView.setAdapter(artistSongAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        getArtistSongs();
        mAddSongBtn.setOnClickListener(v -> openSongChoose());
    }

    private void openSongChoose() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "pick song"), REQUEST_CODE_FOR_AUDIO_FILES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_AUDIO_FILES && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                Uri audioFileUri = data.getData();
                uploadSong(audioFileUri);
            }
        }
    }

    private void uploadSong(Uri audioFileUri) {
        String fileName = Utils.getFileName(audioFileUri, getActivity());
        String songName = Utils.withoutFileExtension(fileName);
        String fileExtension = Utils.getFileExtension(fileName);
        String newRandomUuid = UUID.randomUUID().toString();
        Log.d("files", "uploadSong: " + songName + "    " + fileExtension);
        final StorageReference riversRef = FirebaseHelper.mSingersSongsRef.child(mAuth.getCurrentUser().getUid()).child(newRandomUuid + "/" + fileName);
        riversRef.putFile(audioFileUri).addOnSuccessListener(taskSnapshot -> {
            FirebaseHelper.artistSongsDatabase.child(mAuth.getCurrentUser().getUid()).child(newRandomUuid).setValue(new ArtistSong(songName, newRandomUuid, fileExtension, mAuth.getCurrentUser().getUid()));
            Toast.makeText(getContext(), "File uploaded.",Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed upload", Toast.LENGTH_SHORT).show());
    }



    private void getArtistSongs() {
        FirebaseHelper.mSingersSongsRef.child(mAuth.getCurrentUser().getUid()).listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        Log.d("songs", "getArtistSongs: " + item.getName());
                    }
                })
                .addOnFailureListener(e -> Log.e("songs err", "getArtistSongs: ", e));
    }
}