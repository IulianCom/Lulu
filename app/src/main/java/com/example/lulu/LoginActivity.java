package com.example.lulu;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.lulu.FirebaseHelper.mAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEt;
    private EditText passwordEt;
    private Button logInButton;

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
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, task -> {
                    if(!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Error connecting to Firebase", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                });
            }
        });
    }
}