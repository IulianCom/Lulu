package com.example.lulu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingerActivity extends AppCompatActivity {
    private TextView singerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);

        Intent intent = this.getIntent();
        String singerUUID = intent.getStringExtra("uuid");

        initializeViews();
        singerName.setText(singerUUID);
    }

    private void initializeViews() {
        singerName = findViewById(R.id.singer_name);
    }
}