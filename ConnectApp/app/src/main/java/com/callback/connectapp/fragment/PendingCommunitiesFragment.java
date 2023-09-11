package com.callback.connectapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.PendingCommunityAdapter;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.retrofit.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendingCommunitiesFragment extends Fragment {

    RecyclerView recyclerView;
    List<Community> communityList;
    PendingCommunityAdapter pendingCommunityAdapter;
    ProgressBar progressBar;
    NoInternetDialog noInternetDialog;
    public PendingCommunitiesFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pending_communities_fragment, container, false);
        recyclerView = view.findViewById(R.id.pending_community_recycler);
        progressBar = view.findViewById(R.id.pending_community_progress);

        noInternetDialog = new NoInternetDialog(getContext());

        communityList = new ArrayList<>();
        pendingCommunityAdapter = new PendingCommunityAdapter(getContext(),communityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(pendingCommunityAdapter);

        fetchCommunity();

        return view;
    }

    private void fetchCommunity() {
        Call<List <Community>> call = APIClient.getInstance().getApiInterface()
                .getAllCommunities();

        call.enqueue(new Callback<List <Community>>() {
            @Override
            public void onResponse (Call <List <Community>> call , Response<List <Community>> response) {
                if (response.isSuccessful()) {
                    List <Community> communityList1 = response.body();

                    for(Community community:communityList1){
                        if(!community.isVerified()){
                            communityList.add(community);
                        }

                    }
                    progressBar.setVisibility(View.GONE);

                    pendingCommunityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure (Call <List <Community>> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}