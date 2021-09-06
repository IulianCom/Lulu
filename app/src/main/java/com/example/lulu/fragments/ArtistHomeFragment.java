package com.example.lulu.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lulu.R;
import com.example.lulu.classes.User;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.example.lulu.utils.FirebaseHelper.mStorageRef;

public class ArtistHomeFragment extends Fragment {

    private ImageView mArtistProfilePictureIv;
    private TextView mArtistNameTv;
    private TextView mArtistNumberOfAppreciationsTv;

    private User user;


    public ArtistHomeFragment() {
        // Required empty public constructor
    }

    public static ArtistHomeFragment newInstance(User user) {
        ArtistHomeFragment fragment = new ArtistHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_home, container, false);
        user = (User) getArguments().getSerializable("user");
        initializeViews(view);
        loadDataIntoFields();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void initializeViews(View view) {
        mArtistProfilePictureIv = view.findViewById(R.id.fragment_artist_home_iv_artist_image);
        mArtistNameTv = view.findViewById(R.id.fragment_artist_home_tv_artist_name);
        mArtistNumberOfAppreciationsTv = view.findViewById(R.id.fragment_artist_home_tv_artist_followers);
    }

    public void loadDataIntoFields() {
        StorageReference profileRef = mStorageRef.child("users/" + user.getUuid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(mArtistProfilePictureIv);
        });
        mArtistNameTv.setText("".equals(user.getName()) ? "" : user.getName());
        mArtistNumberOfAppreciationsTv.setText(String.valueOf(user.getAppreciations()));
    }

}