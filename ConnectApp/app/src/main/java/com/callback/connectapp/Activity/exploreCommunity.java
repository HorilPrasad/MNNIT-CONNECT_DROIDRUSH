package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.CommunityAdapter;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.retrofit.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class exploreCommunity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List <Community> communityList;
    private CommunityAdapter communityAdapter;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_community);

        noInternetDialog = new NoInternetDialog(this);
        recyclerView = findViewById(R.id.community_recyclerview);
        communityList = new ArrayList <>();

        communityAdapter = new CommunityAdapter(this , communityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(communityAdapter);

        fetchCommunity();
    }

    private void fetchCommunity () {
        Call <List <Community>> call = APIClient.getInstance().getApiInterface()
                .getAllCommunities();

        call.enqueue(new Callback <List <Community>>() {
            @Override
            public void onResponse (Call <List <Community>> call , Response <List <Community>> response) {
                if (response.isSuccessful()) {
                    List <Community> communityList1 = response.body();
                    communityList.addAll(communityList1);
                    communityAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure (Call <List <Community>> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });
    }
}