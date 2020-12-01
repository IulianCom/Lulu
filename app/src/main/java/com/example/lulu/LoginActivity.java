package com.example.lulu;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText emailEdit, passwordEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
    }

    private void initializeViews(){
        loginBtn = findViewById(R.id.btn_login);
        emailEdit = findViewById(R.id.et_email);
        passwordEdit = findViewById(R.id.et_password);
    }

    private void setListeners(){

        
    }
}