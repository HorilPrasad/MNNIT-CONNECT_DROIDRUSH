package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.callback.connectapp.R;
import com.callback.connectapp.fragment.PendingCommunitiesFragment;

public class AdminDashboard extends AppCompatActivity {

    private RelativeLayout relativeLayout,fragmentContainer;
    private CardView pendingCommunities;
    private PendingCommunitiesFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);


        relativeLayout = findViewById(R.id.dashboard_layout);
        pendingCommunities = findViewById(R.id.dashboard_pending_communities);
        fragmentContainer = findViewById(R.id.fragment_container);
        pendingCommunities.setOnClickListener(v ->{
             fragment = new PendingCommunitiesFragment();
            loadFragment(fragment);
        });
    }

    private void loadFragment(PendingCommunitiesFragment fragment) {
        relativeLayout.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
    }
}