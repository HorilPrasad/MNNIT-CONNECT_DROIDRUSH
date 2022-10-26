package com.callback.connectapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.callback.connectapp.Activity.LoginActivity;
import com.callback.connectapp.Activity.createCommunity;
import com.callback.connectapp.Activity.exploreCommunity;
import com.callback.connectapp.R;


public class CommunityFragment extends Fragment {

     CardView exploreCommunity,createCommunity;

    public CommunityFragment () {
        // Required empty public constructor
    }




    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        View view= inflater.inflate(R.layout.fragment_community , container , false);

        exploreCommunity =view.findViewById(R.id.exploreCommunity);
        createCommunity =view.findViewById(R.id.createCommunity);



        exploreCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                Intent i= new Intent(getContext(), exploreCommunity.class);

                startActivity(i);
            }
        });

        createCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                Intent i= new Intent(getContext(), createCommunity.class);

                startActivity(i);
            }
        });


        return view;
    }
}