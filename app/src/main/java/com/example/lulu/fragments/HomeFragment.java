package com.example.lulu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.SingerAdapter;
import com.example.lulu.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.lulu.utils.FirebaseHelper.mAuth;
import static com.example.lulu.utils.FirebaseHelper.userDatabase;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private TextView welcomeTv;

    private User user;

    ArrayList<User> singers;

    public static HomeFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_home,container,false);
        view = itemView;
        user = (User) getArguments().getSerializable("user");
        initializeViews();
        if(!user.getName().isEmpty()) {
            welcomeTv.setText("Welcome, " + user.getName());
        }
        return itemView;
    }

    @Override
    public void onStart() {
        super.onStart();
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    singers = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (!user.getUserType().equals(User.ARTIST_USER_TYPE))
                            continue;
                        singers.add(user);
                    }
                    recyclerView.setAdapter(new SingerAdapter(singers, getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(mAuth.getUid() == null)
                    return;
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        welcomeTv = view.findViewById(R.id.welcome_tv);
        recyclerView = view.findViewById(R.id.home_rv);
    }
}
