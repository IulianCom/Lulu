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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    private User singer;
    private RecyclerView recyclerView;
    private ImageView mSingerImageIv;
    private TextView mSingerNameTv;

    private List<Post> list;

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance(User singer) {
        PostsFragment fragment = new PostsFragment();
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
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        singer = (User) getArguments().getSerializable("artist");
        Log.d("dsds", "onCreateView: " + singer);
        initializeViews(view);
        updateUI(FirebaseHelper.userDatabase.child(singer.getUuid()));
        populateRecyclerView(FirebaseHelper.postsDatabase.child(singer.getUuid()));
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rv_singe_posts);
        mSingerImageIv = view.findViewById(R.id.singer_image);
        mSingerNameTv = view.findViewById(R.id.fragment_posts_singer_name_tv);
    }

    private void updateUI(DatabaseReference currentSingerBdReference) {
        mSingerNameTv.setText(singer.getName());
        StorageReference ref = mStorageRef.child("users/" + singer.getUuid() + "/profile.jpg");
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get()
                    .load(uri)
                    .into(mSingerImageIv);
        }).addOnFailureListener(e -> {
            Log.println(Log.DEBUG,"update", "update ui" + e.getStackTrace().toString());
        });
    }

    private void populateRecyclerView(DatabaseReference ref) {
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()) {
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