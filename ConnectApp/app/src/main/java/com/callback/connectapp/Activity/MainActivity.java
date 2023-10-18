package com.callback.connectapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.fragment.CommunityFragment;
import com.callback.connectapp.fragment.CreatePostFragment;
import com.callback.connectapp.fragment.HomeFragment;
import com.callback.connectapp.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    CreatePostFragment createPostFragment;
    CommunityFragment communityFragment;
    ProfileFragment profileFragment;
    FragmentManager fragmentManager;
    Fragment activeFragment;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        homeFragment = new HomeFragment();
        createPostFragment = new CreatePostFragment();
        communityFragment = new CommunityFragment();
        profileFragment = new ProfileFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.container,homeFragment,"1").commit();
        activeFragment = homeFragment;

        bottomNavigationView.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    switchFragment(homeFragment, "1");
                    return true;

                case R.id.post:
                    switchFragment(createPostFragment, "2");
                    return true;

                case R.id.community:
                    switchFragment(communityFragment, "3");
                    return true;

                case R.id.profile:
                    switchFragment(profileFragment, "4");
                    return true;
                default:
                    return false;
            }
        });

//        fragmentManager.addOnBackStackChangedListener(() -> {
//            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
//
//            int selectedId = getSelectedItemForFragment(currentFragment);
//
//            bottomNavigationView.setSelectedItemId(selectedId);
//        });
    }

    @Override
    protected void onStart () {
        super.onStart();
        AppConfig appConfig = new AppConfig(this);
        if (!appConfig.isUserLogin()){
            startActivity(new Intent(MainActivity.this , LoginActivity.class));

        }
//        startActivity(new Intent(MainActivity.this,AdminDashboard.class));


    }

    private void switchFragment(Fragment targetFragment, String tag) {
        if (activeFragment != targetFragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.hide(activeFragment);

            // Check if the fragment is already added to the backstack
            Fragment existingFragment = fragmentManager.findFragmentByTag(tag);

            if(targetFragment == homeFragment)
                fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);

            if (existingFragment != null) {
                transaction.replace(R.id.container,existingFragment,tag);
            } else {
                transaction.replace(R.id.container, targetFragment, tag);
                transaction.addToBackStack(null);
            }
            transaction.commit();
            activeFragment = targetFragment;
        }
    }
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    private int getSelectedItemForFragment(Fragment fragment) {
        if (fragment == homeFragment) {
            return R.id.home;
        } else if (fragment == createPostFragment) {
            return R.id.post;
        } else if (fragment == communityFragment) {
            return R.id.community;
        } else if (fragment == profileFragment) {
            return R.id.profile;
        }
        return -1;
    }
}