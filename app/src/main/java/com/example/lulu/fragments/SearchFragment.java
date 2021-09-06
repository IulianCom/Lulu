package com.example.lulu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.SingerAdapter;
import com.example.lulu.classes.Singer;
import com.example.lulu.classes.User;
import com.example.lulu.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.lulu.utils.FirebaseHelper.userDatabase;

public class SearchFragment extends Fragment {
    ArrayList<User> list;
    RecyclerView recyclerView;
    SearchView searchView;

    private User user;

    public static SearchFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable("user", user);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        user = (User) getArguments().getSerializable("user");
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.sv);
    }

    @Override
    public void onStart() {
        super.onStart();
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list = new ArrayList<>();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        User user = ds.getValue(User.class);
                        if(!User.ARTIST_USER_TYPE.equals(user.getUserType()))
                            continue;
                        list.add(user);
                    }
                    SingerAdapter singerAdapter = new SingerAdapter(list, getContext());
                    recyclerView.setAdapter(singerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String string){
        ArrayList<User> singerList = new ArrayList<>();
        for(User user : list){
            if(user.getName().toLowerCase().contains(string.toLowerCase())){
                singerList.add(user);
            }
        }
        SingerAdapter singerAdapter = new SingerAdapter(singerList, getContext());
        recyclerView.setAdapter(singerAdapter);
    }
}
