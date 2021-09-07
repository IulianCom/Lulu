package com.example.lulu.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lulu.R;
import com.example.lulu.classes.User;
import com.example.lulu.fragments.HomeFragment;
import com.example.lulu.fragments.LibraryFragment;
import com.example.lulu.fragments.ProfileFragment;
import com.example.lulu.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WelcomeActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        user = (User) getIntent().getSerializableExtra("user");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(user)).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = HomeFragment.newInstance(user);
                            break;
                        case R.id.nav_search:
                            selectedFragment = SearchFragment.newInstance(user);
                            break;
                        case R.id.nav_library:
                            selectedFragment = LibraryFragment.newInstance(user);
                            break;
                        case R.id.nav_premium:
                            selectedFragment = ProfileFragment.newInstance(user);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}