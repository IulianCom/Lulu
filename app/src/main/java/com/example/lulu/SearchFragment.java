package com.example.lulu;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    DatabaseReference ref;
    ArrayList<Singers> list;
    RecyclerView recyclerView;
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ref = FirebaseDatabase.getInstance().getReference().child("singers");
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        recyclerView = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.sv);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ref!=null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){

                            list.add(ds.getValue(Singers.class));
                        }
                        AdapterClass adapterClass =new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);
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
        ArrayList<Singers> singerList = new ArrayList<>();
        for(Singers object : list){
            if(object.getName().toLowerCase().contains(string.toLowerCase())){
                singerList.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(singerList);
        recyclerView.setAdapter(adapterClass);
    }
}
