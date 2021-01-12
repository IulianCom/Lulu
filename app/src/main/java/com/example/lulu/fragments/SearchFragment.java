package com.example.lulu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.adapters.SingerAdapter;
import com.example.lulu.classes.Singer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.lulu.FirebaseHelper.singerDatabase;

public class SearchFragment extends Fragment {
    ArrayList<Singer> list;
    RecyclerView recyclerView;
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        recyclerView = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.sv);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(singerDatabase!=null){
            singerDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){

                            list.add(ds.getValue(Singer.class));
                        }
                        SingerAdapter singerAdapter =new SingerAdapter(list, getContext());
                        recyclerView.setAdapter(singerAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                   // Toast.makeText(SearchFragment.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        ArrayList<Singer> singerList = new ArrayList<>();
        for(Singer object : list){
            if(object.getName().toLowerCase().contains(string.toLowerCase())){
                singerList.add(object);
            }
        }
        SingerAdapter singerAdapter = new SingerAdapter(singerList, getContext());
        recyclerView.setAdapter(singerAdapter);
    }
}
