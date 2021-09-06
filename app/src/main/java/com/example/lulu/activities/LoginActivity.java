package com.example.lulu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lulu.R;
import com.example.lulu.classes.User;
import com.example.lulu.utils.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static com.example.lulu.utils.FirebaseHelper.mAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEt;
    private EditText passwordEt;
    private Button logInButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        setListeners();
    }

    private void initializeViews(){
        logInButton = findViewById(R.id.btn_login);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
    }

    private void setListeners() {
        logInButton.setOnClickListener( view -> {
            String email = emailEt.getText().toString();
            String pass = passwordEt.getText().toString();
            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.signInWithEmailAndPassword(email.trim(), pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String registeredUserID = currentUser.getUid();
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("users").child(registeredUserID);
                        dr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                user = snapshot.getValue(User.class);
                                Log.d("user", "onDataChange: " + user);
                                String userType = snapshot.child("userType").getValue().toString();
                                if(userType.equals("Artist")){
                                    Intent intentArtist = new Intent(LoginActivity.this, SingerPageActivity.class);
                                    intentArtist.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intentArtist.putExtra("user", user);
                                    startActivity(intentArtist);
                                    finish();
                                }else if(userType.equals("Regular user")){
                                    Intent intentRegular = new Intent(LoginActivity.this, WelcomeActivity.class);
                                    intentRegular.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intentRegular.putExtra("user", user);
                                    startActivity(intentRegular);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed Login. Please Try Again", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error connecting to Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
//                    if(!task.isSuccessful()) {
//                        Toast.makeText(LoginActivity.this, "Error connecting to Firebase", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//
//                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
//                    }

            }
        });
    }
}