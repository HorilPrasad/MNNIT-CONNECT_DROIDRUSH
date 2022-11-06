package com.callback.connectapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.fragment.CommunityFragment;
import com.callback.connectapp.fragment.CreatePostFragment;
import com.callback.connectapp.fragment.HomeFragment;
import com.callback.connectapp.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppConfig appConfig = new AppConfig(this);

        if (!appConfig.isUserLogin())
            startActivity(new Intent(MainActivity.this, signUpActivity.class));
    }

    HomeFragment homeFragment = new HomeFragment();
    CreatePostFragment createPostFragment = new CreatePostFragment();
    CommunityFragment communityFragment = new CommunityFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;

            case R.id.post:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, createPostFragment).commit();
                return true;

            case R.id.community:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, communityFragment).commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }
        return false;
    }
}