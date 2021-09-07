package com.example.lulu.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lulu.R;
import com.example.lulu.activities.MainActivity;
import com.example.lulu.classes.User;
import com.example.lulu.utils.FirebaseHelper;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.lulu.utils.FirebaseHelper.mAuth;
import static com.example.lulu.utils.FirebaseHelper.mStorageRef;

public class ArtistProfileFragment extends Fragment {
    private ImageView singerImage;
    View view;
    EditText nameEt;
    TextView emailTv;
    Button changePhoto;
    Button saveDataBtn;
    private Button mLogoutBtn;
    private UUID randomUUID;

    private User user;

    public ArtistProfileFragment() {
    }

    public static ArtistProfileFragment newInstance(User user) {
        ArtistProfileFragment fragment = new ArtistProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist_profile,container,false);
        user = (User) getArguments().getSerializable("user");
        initializeViews();
        changePhoto.setOnClickListener(v -> {
            addPhotoToDatabase();
        });
        saveDataBtn.setOnClickListener(v -> {
            String name = nameEt.getText().toString();
            if(!name.isEmpty()) {
                FirebaseHelper.artistSongsDatabase.child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);
            }
            else {
                Toast.makeText(view.getContext(), "Name field must not be empty", Toast.LENGTH_SHORT).show();
            }
        });
        mLogoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getContext(), MainActivity.class));
        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        emailTv.setText(user.getEmail());
        nameEt.setText(user.getName());
    }

    private void addPhotoToDatabase() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
        //    singerImage.setImageURI(imageUri);
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        final StorageReference riversRef = mStorageRef.child("users/" + mAuth.getCurrentUser().getUid()+ "/profile.jpg");
        riversRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                riversRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(singerImage);
                });
                Toast.makeText(getContext(), "Image uploaded.",Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed image", Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeViews() {
        nameEt = view.findViewById(R.id.activity_player_tv_song_name);
        emailTv = view.findViewById(R.id.tv_email);
        saveDataBtn = view.findViewById(R.id.btn_save_data);
        changePhoto = view.findViewById(R.id.changePhoto_btn);
        singerImage = view.findViewById(R.id.singer_image);
        mLogoutBtn = view.findViewById(R.id.btn_logout);

        StorageReference profileRef = mStorageRef.child("users/" + user.getUuid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(singerImage);
        });
    }


}