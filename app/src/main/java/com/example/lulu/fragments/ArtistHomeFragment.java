package com.example.lulu.fragments;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.PostsAdapter;
import com.example.lulu.classes.Post;
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

import static com.example.lulu.utils.FirebaseHelper.mStorageRef;

public class ArtistHomeFragment extends Fragment {

    private ImageView mArtistProfilePictureIv;
    private TextView mArtistNameTv;
    private TextView mArtistNumberOfAppreciationsTv;
    private Button mAddPostBtn;
    private EditText mPostTextEt;
    private RecyclerView recyclerView;

    private List<Post> list;

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
        mAddPostBtn = view.findViewById(R.id.btn_add_post);
        mPostTextEt = view.findViewById(R.id.activity_post_et);
        recyclerView = view.findViewById(R.id.rv_posts);
        mAddPostBtn.setOnClickListener(v -> {
            if(!mPostTextEt.getText().toString().isEmpty()) {
                Post newPost = new Post(mPostTextEt.getText().toString(), user.getUuid());
                FirebaseHelper.postsDatabase.child(newPost.getPosterUuid()).child(newPost.getUuid()).setValue(newPost);
                Toast.makeText(getContext(), "Post added", Toast.LENGTH_SHORT).show();
                mPostTextEt.setText("");
            }
        });
    }

    public void loadDataIntoFields() {
        StorageReference profileRef = mStorageRef.child("users/" + user.getUuid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(mArtistProfilePictureIv);
        });
        mArtistNameTv.setText("".equals(user.getName()) ? "" : user.getName());
        mArtistNumberOfAppreciationsTv.setText(String.valueOf(user.getAppreciations()));
        populateRecyclerView(FirebaseHelper.postsDatabase.child(user.getUuid()));
    }

    private void populateRecyclerView(DatabaseReference ref) {
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Post post = ds.getValue(Post.class);
                            list.add(post);
                        }
                        PostsAdapter postsAdapter = new PostsAdapter((ArrayList<Post>) list, getContext());
                        recyclerView.setAdapter(postsAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}