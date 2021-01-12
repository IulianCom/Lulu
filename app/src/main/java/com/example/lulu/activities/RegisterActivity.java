package com.example.lulu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.lulu.FirebaseHelper.mAuth;
import static com.example.lulu.FirebaseHelper.userDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt, emailEt, passwordEt;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();
        setListener();
    }

    private void initializeViews(){
        nameEt = findViewById(R.id.et_name);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        createBtn= findViewById(R.id.btn_create);
    }

    private void setListener(){
        createBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String pass = passwordEt.getText().toString();
            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Complete fields", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Couldn't register", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            User user = new User(email, pass);
                            userDatabase.child(mAuth.getCurrentUser().getUid())
                                    .setValue(user);
                            startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
                        }
                    }
                });
            }
        });
    }
}