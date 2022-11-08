package com.callback.connectapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.callback.connectapp.Activity.CreateProfile;
import com.callback.connectapp.Activity.signUpActivity;
import com.callback.connectapp.R;
import com.callback.connectapp.adapter.HomePostAdapter;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.postData;
import com.callback.connectapp.retrofit.APIClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView postList_recycler;
    List <postData> postDataArrayList;
    private HomePostAdapter homePostAdapter;
    private NoInternetDialog noInternetDialog;

    public HomeFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_home , container , false);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        postList_recycler = view.findViewById(R.id.homeFeedRecycler);
        noInternetDialog = new NoInternetDialog(getContext());
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        postDataArrayList = new ArrayList <postData>();

        homePostAdapter = new HomePostAdapter(getContext() , postDataArrayList);
        postList_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        postList_recycler.setHasFixedSize(true);
        postList_recycler.setAdapter(homePostAdapter);


        fetchAllPost();

        return view;
    }

    private void fetchAllPost () {

        Call <List <postData>> call = APIClient.getInstance()
                .getApiInterface().getAllPosts();

        call.enqueue(new Callback <List <postData>>() {
            @Override
            public void onResponse (Call <List <postData>> call , Response <List <postData>> response) {

                if (response.isSuccessful()) {

                    List <postData> po = response.body();

                    homePostAdapter.clear();


                    postDataArrayList.addAll(response.body());
                    homePostAdapter.notifyDataSetChanged();


                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                } else {

                    Toast.makeText(getContext() , "not sucesss..." , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure (Call <List <postData>> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();

            }
        });


    }

    @Override
    public void onResume () {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause () {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}