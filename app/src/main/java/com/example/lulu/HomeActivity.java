package com.example.lulu;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

//    private HomeFragment homeFragment;
//    private ProfileFragment dashboardFragment;
//    private NotificationsFragment notificationsFragment;
//    private BottomNavigationView navView;
//    private Fragment activeFragment;
//    final FragmentManager fragmentManager = getSupportFragmentManager();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first_menu);
//
//        initializeViews();
//        LoadFragment();
//        navView.setOnNavigationItemSelectedListener(this);
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
//                activeFragment = homeFragment;
//                return true;
//
//            case R.id.navigation_dashboard:
//                fragmentManager.beginTransaction().hide(activeFragment).show(dashboardFragment).commit();
//                activeFragment = dashboardFragment;
//                return true;
//
//            case R.id.navigation_notifications:
//                fragmentManager.beginTransaction().hide(activeFragment).show(notificationsFragment).commit();
//                activeFragment = notificationsFragment;
//                return true;
//        }
//        return false;
//    }
//
//    private void LoadFragment() {
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, notificationsFragment, "3").hide(notificationsFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, dashboardFragment, "2").hide(dashboardFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit();
//    }
//
//    public void initializeViews() {
//        navView = findViewById(R.id.bnv_main_menu);
//
//        homeFragment = new HomeFragment();
//        dashboardFragment = new ProfileFragment();
//        notificationsFragment = new NotificationsFragment();
//
//        activeFragment = homeFragment;
//    }

}