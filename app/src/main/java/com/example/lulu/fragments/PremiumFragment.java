package com.example.lulu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lulu.R;
import com.example.lulu.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.lulu.FirebaseHelper.mAuth;
import static com.example.lulu.FirebaseHelper.userDatabase;

public class PremiumFragment extends Fragment {
    View view;
    EditText nameEt;
    TextView emailTv;
    Button saveDataBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_premium,container,false);
        initializeViews();
        saveDataBtn.setOnClickListener(v -> {
            String name = nameEt.getText().toString();
            if(!name.isEmpty()) {
                userDatabase.child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);
            }
            else {
                Toast.makeText(view.getContext(), "Name field must not be empty", Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        userDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                emailTv.setText(currentUser.getEmail());
                nameEt.setText(currentUser.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeViews() {
        nameEt = view.findViewById(R.id.et_name);
        emailTv = view.findViewById(R.id.tv_email);
        saveDataBtn = view.findViewById(R.id.btn_save_data);
    }




}
