package com.example.lulu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lulu.R;
import com.example.lulu.classes.User;
import com.example.lulu.fragments.PostsFragment;
import com.example.lulu.fragments.SongsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SingerActivity extends AppCompatActivity {

    private User singer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);
        Intent intent = this.getIntent();
        singer = (User) intent.getSerializableExtra("artist");
        BottomNavigationView bottomNavigationView = findViewById(R.id.post_song_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_singer_fragment_container, SongsFragment.newInstance(singer)).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_artist_posts:
                            selectedFragment = PostsFragment.newInstance(singer);
                            break;
                        case R.id.nav_artist_songs:
                            selectedFragment = SongsFragment.newInstance(singer);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_singer_fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}