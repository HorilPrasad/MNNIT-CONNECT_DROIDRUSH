package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.HomePostAdapter;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.PostData;
import com.callback.connectapp.retrofit.APIClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityPage extends AppCompatActivity {
    private TextView communityName, memberCount;
    private CardView joinButtonCard;
    private TextView joinBtn;
    private ImageView communityImg;
    private TextView CreatePost;
    private AppConfig appConfig;
    private RecyclerView recyclerView;

    private List <PostData> postDataArrayList;
    private String communityId;
    private NoInternetDialog noInternetDialog;
    private TextView createBy, about;
    private HomePostAdapter postAdapter;
    private CardView cardView;
    private String userId;
    private RelativeLayout relativeLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_community_page);
        communityImg = findViewById(R.id.communityPicture);
        memberCount = findViewById(R.id.noMember);
        joinBtn = findViewById(R.id.join_button);
        joinButtonCard = findViewById(R.id.join_button_card);
        cardView = findViewById(R.id.cardView);
        recyclerView = findViewById(R.id.community_recyclerview);
        communityName = findViewById(R.id.name_community);
        appConfig = new AppConfig(this);
        userId = appConfig.getUserID();
        createBy = findViewById(R.id.createdBy);
        about = findViewById(R.id.about_community);
        noInternetDialog = new NoInternetDialog(this);
        CreatePost = findViewById(R.id.create_post);
        relativeLayout = findViewById(R.id.community_page_layout);
        communityId = getIntent().getStringExtra("id");

        loadCommunity();

        postDataArrayList = new ArrayList <>();

        postAdapter = new HomePostAdapter(this , postDataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(postAdapter);
        LoadPost();


        joinBtn.setOnClickListener(view -> {

            if (joinBtn.getText() == "join") {

                User user = new User();
                user.set_id(appConfig.getUserID());
                Call <ApiResponse> call = APIClient.getInstance().getApiInterface()
                        .addUserToCommunity(communityId , user);

                call.enqueue(new Callback <ApiResponse>() {
                    @Override
                    public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {
                        if (response.isSuccessful()) {

                            if (response.code() == 200) {
                                joinBtn.setText("joined");
                                joinButtonCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                                cardView.setVisibility(View.VISIBLE);
                                CreatePost.setVisibility(View.VISIBLE);
                                joinBtn.setEnabled(false);
                                Toast.makeText(CommunityPage.this , "joined" , Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure (Call <ApiResponse> call , Throwable t) {
                        if (!noInternetDialog.isConnected())
                            noInternetDialog.create();
                    }
                });

            }
        });

        if (joinBtn.getText() == "joined") {
            cardView.setVisibility(View.VISIBLE);
            CreatePost.setVisibility(View.VISIBLE);
        }


        CreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {


                Intent i = new Intent(CommunityPage.this , CreateCommunityPost.class);
                i.putExtra("communityId" , communityId);
                startActivity(i);
            }
        });


    }

    private void loadCommunity () {
        loading();
        Call <Community> call = APIClient.getInstance().getApiInterface()
                .getCommunityById(communityId);

        call.enqueue(new Callback <Community>() {
            @Override
            public void onResponse (Call <Community> call , Response <Community> response) {

                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        Community community = response.body();
                        setCommunityData(community);

                    }
                }
            }

            @Override
            public void onFailure (Call <Community> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
                progressDialog.dismiss();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void setCommunityData (Community community) {
        memberCount.setText(community.getMembers().size() + "");
        communityName.setText(community.getName());

        Call <User> call = APIClient.getInstance().getApiInterface()
                .getUser(community.getUserId());

        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {

                if (response.isSuccessful()) {

                    createBy.setText("Created By " + response.body().getName());
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                    if (!noInternetDialog.isConnected()) {
                        noInternetDialog.create();
                    progressDialog.dismiss();
                }
            }
        });

        about.setText("About : " + community.getAbout());
        Log.d("page" , community.getMembers().toString());
        if (!Objects.equals(community.getImage() , ""))
            Picasso.get().load(community.getImage()).placeholder(R.drawable.background).into(communityImg);

        if (community.getMembers().contains(userId)) {
            joinBtn.setText("Joined");
            cardView.setVisibility(View.VISIBLE);
            CreatePost.setVisibility(View.VISIBLE);
            joinButtonCard.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
            joinBtn.setEnabled(false);

        } else {
            joinBtn.setText("join");
        }


    }


    private void LoadPost () {

        Call <List <PostData>> call = APIClient.getInstance()
                .getApiInterface().getCommunityPost(communityId);

        call.enqueue(new Callback <List <PostData>>() {
            @Override
            public void onResponse (Call <List <PostData>> call , Response <List <PostData>> response) {

                if (response.isSuccessful()) {

                    postDataArrayList.addAll(response.body());
                    postAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                } else {

                    Toast.makeText(CommunityPage.this , "not sucesss..." , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure (Call <List <PostData>> call , Throwable t) {
                if (!noInternetDialog.isConnected()) {
                    noInternetDialog.create();
                    progressDialog.dismiss();
                }
            }
        });

    }
    private void loading () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Community");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }
}
