package com.callback.connectapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.callback.connectapp.Activity.LoginActivity;
import com.callback.connectapp.Activity.createCommunity;
import com.callback.connectapp.Activity.exploreCommunity;
import com.callback.connectapp.R;
import com.callback.connectapp.adapter.CommunityAdapter;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.retrofit.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommunityFragment extends Fragment {

    CardView exploreCommunity, createCommunity;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    private List <Community> communityList;
    private CommunityAdapter communityAdapter;
    private NoInternetDialog noInternetDialog;
    AppConfig appConfig;

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_community, container, false);
          appConfig =new AppConfig(getContext());
        exploreCommunity = view.findViewById(R.id.exploreCommunity);
        createCommunity = view.findViewById(R.id.createCommunity);
        frameLayout = view.findViewById(R.id.community_fragment_layout);
        recyclerView=view.findViewById(R.id.joinCommunityRecycler);

        noInternetDialog = new NoInternetDialog(getContext());

        communityList = new ArrayList <>();

        communityAdapter = new CommunityAdapter(getContext(), communityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(communityAdapter);

          fetchCommunity();

        exploreCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  Intent i = new Intent(getContext(), exploreCommunity.class);

                startActivity(i);
            }
        });

        createCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), createCommunity.class);

                startActivity(i);
            }
        });


        return view;
    }

    private void fetchCommunity() {
        Call <List<Community>> call = APIClient.getInstance().getApiInterface()
                .getAllCommunities();

        call.enqueue(new Callback <List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response <List<Community>> response) {
                if (response.isSuccessful()) {
                    List<Community> communityList1 = response.body();


                    for(Community community:communityList1){
                        if(community.getMembers().contains(appConfig.getUserID())){
                            communityList.add(community);
                        }

                    }



                    communityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });
    }
}