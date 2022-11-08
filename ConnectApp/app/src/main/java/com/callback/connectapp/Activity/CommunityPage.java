package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.HomePostAdapter;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.fragment.CreatePostFragment;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.postData;
import com.callback.connectapp.retrofit.APIClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class CommunityPage extends AppCompatActivity {
    private TextView communityName, memberCount;
    private Button joinBtn, inviteBtn;
    private ImageView communityImg;
    private TextView CreatePost;
    private AppConfig appConfig;
    private RecyclerView recyclerView;

    private List <postData> postDataArrayList;
    private String communityId;
    private NoInternetDialog noInternetDialog;
    TextView createBy, about;
    private HomePostAdapter postAdapter;
    CardView cardView;
    private String userId;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_community_page);
        communityImg = findViewById(R.id.communityPicture);
        memberCount = findViewById(R.id.noMember);
        joinBtn = findViewById(R.id.join_button);
        cardView = findViewById(R.id.cardView);
        recyclerView = findViewById(R.id.community_recyclerview);
        communityName = findViewById(R.id.name_community);
        appConfig = new AppConfig(this);
        userId = appConfig.getUserID();
        createBy = findViewById(R.id.createdBy);
        about = findViewById(R.id.about_community);
        noInternetDialog = new NoInternetDialog(this);
        CreatePost = findViewById(R.id.create_post);
        communityId = getIntent().getStringExtra("id");

        loadCommunity();

        postDataArrayList = new ArrayList <>();

        postAdapter = new HomePostAdapter(this , postDataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(postAdapter);
        LoadPost();


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

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
                                    cardView.setVisibility(View.VISIBLE);
                                    CreatePost.setVisibility(View.VISIBLE);
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
                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                if (!noInternetDialog.isConnected()) {
                    noInternetDialog.create();
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
            Drawable img = getResources().getDrawable(R.drawable.ic_baseline_groups_24);
            joinBtn.setCompoundDrawables(img , null , null , null);

        } else {
            joinBtn.setText("join");
            joinBtn.setCompoundDrawables(null , null , null , null);
        }


    }


    private void LoadPost () {

        Call <List <postData>> call = APIClient.getInstance()
                .getApiInterface().getCommunityPost(communityId);

        call.enqueue(new Callback <List <postData>>() {
            @Override
            public void onResponse (Call <List <postData>> call , Response <List <postData>> response) {

                if (response.isSuccessful()) {

                    List <postData> po = response.body();

                    postAdapter.clear();
                    postDataArrayList.addAll(response.body());
                    postAdapter.notifyDataSetChanged();

                    Log.d("sizeif" , String.valueOf(response.body().size()));


                } else {

                    Toast.makeText(CommunityPage.this , "not sucesss..." , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure (Call <List <postData>> call , Throwable t) {
                if (!noInternetDialog.isConnected()) {
                    noInternetDialog.create();
                }
            }
        });


    }
}
