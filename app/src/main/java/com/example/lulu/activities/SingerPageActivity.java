package com.example.lulu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lulu.R;
import com.example.lulu.classes.User;
import com.example.lulu.fragments.ArtistHomeFragment;
import com.example.lulu.fragments.ArtistLibraryFragment;
import com.example.lulu.fragments.ArtistProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class SingerPageActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_page);
        BottomNavigationView bottomNavigationView= findViewById(R.id.singer_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        user = (User) getIntent().getSerializableExtra("user");
        Log.d("user", "onCreate: " + user);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ArtistHomeFragment.newInstance(user)).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_artist_home:
                            selectedFragment = ArtistHomeFragment.newInstance(user);
                            break;
                        case R.id.nav_artist_library:
                            selectedFragment = ArtistLibraryFragment.newInstance(user);
                            break;
                        case R.id.nav_artist_account:
                            selectedFragment = ArtistProfileFragment.newInstance(user);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
    }
