package com.callback.connectapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.HomePostAdapter;
import com.callback.connectapp.model.postData;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private ShimmerFrameLayout mShimmerViewContainer;
   private RecyclerView postList_recycler;
   ArrayList<postData>postDataArrayList;
   private HomePostAdapter homePostAdapter;

    public HomeFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment


    View view=inflater.inflate(R.layout.fragment_home , container , false);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
       postList_recycler=view.findViewById(R.id.homeFeedRecycler);

       postDataArrayList =new ArrayList <>();
       homePostAdapter=new HomePostAdapter(getContext(),postDataArrayList);

       postList_recycler.setAdapter(homePostAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause () {
        mShimmerViewContainer.stopShimmerAnimation();

        mShimmerViewContainer.setVisibility(View.GONE);

        super.onPause();
    }
}