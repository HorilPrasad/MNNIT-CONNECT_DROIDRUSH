package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.callback.connectapp.model.Community;
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

    private List<postData> postDataArrayList;
    private String communityId;
    private NoInternetDialog noInternetDialog;

    private HomePostAdapter postAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_page);
        communityImg = findViewById(R.id.communityPicture);
        memberCount = findViewById(R.id.noMember);
        joinBtn = findViewById(R.id.join_button);
        inviteBtn = findViewById(R.id.invite_button);
        recyclerView = findViewById(R.id.community_recyclerview);
        communityName = findViewById(R.id.name_community);
        appConfig = new AppConfig(this);
        userId = appConfig.getUserID();
        noInternetDialog = new NoInternetDialog(this);

        communityId = getIntent().getStringExtra("id");

        loadCommunity();

        postDataArrayList = new ArrayList<>();

        postAdapter = new HomePostAdapter(this, postDataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(postAdapter);


    }

    private void loadCommunity() {
        Call<Community> call = APIClient.getInstance().getApiInterface()
                .getCommunityById(communityId);

        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {

                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        Community community = response.body();
                        setCommunityData(community);
                        Toast.makeText(CommunityPage.this, community.getName(), Toast.LENGTH_SHORT).show();



                    }
                }
            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });

//        memberCount.setText(ob.getMembers().size());
//        communityName.setText(ob.getName());
//
//
//        if (!Objects.equals(ob.getImage() , "")) {
//
//            Picasso.get().load(ob.getImage()).into(communityImg);
//        }
//
//
//        if (ob.getMembers().contains(userId)) {
//
//            userId = appConfig.getUserID();
//
//            if (ob.getMembers().contains(userId)) {
//                joinBtn.setText("joined");
//                LoadPost();
//            } else {
//                joinBtn.setText("join");
//            }
//
//            if (joinBtn.getText() == "join") {
//                joinBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick (View view) {
//
//
//                        joinBtn.setText("joined");
//                        ob.getMembers().add(appConfig.getUserID());
//
//
//                    }
//                });
//
//
//                CreatePost.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick (View view) {
//
//
//                        if (joinBtn.getText() == "join") {
//                            joinBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick (View view) {
//
//                                    joinBtn.setText("joined");
//                                    ob.getMembers().add(appConfig.getUserID());
//                                    LoadPost();
//
//                                }
//                            });
//
//                            CreatePost.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick (View view) {
//
//
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("communityId" , ob.get_id());
//
//                                    CreatePostFragment fragment = new CreatePostFragment();
//                                    fragment.setArguments(bundle);
//                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                                    ft.replace(R.id.container , fragment);
//                                    ft.commit();
//
//
//                                }
//                            });
//
//                        }
//
//
//                    }
//                });
//
//            }
//        }
    }

    private void setCommunityData(Community community) {
        //memberCount.setText(community.getMembers().size()+"");
        communityName.setText(community.getName());
        if (!community.getImage().equals(""))
            Picasso.get().load(community.getImage()).placeholder(R.drawable.background).into(communityImg);

        if(community.getMembers().contains(userId)){
            joinBtn.setText("Joined");
            Drawable img = getResources().getDrawable(R.drawable.ic_baseline_groups_24);
            joinBtn.setCompoundDrawables(img,null,null,null);

        }else{
            joinBtn.setText("join");
            joinBtn.setCompoundDrawables(null,null,null,null);
        }

    }


    private void LoadPost() {

        Call<List<postData>> call = APIClient.getInstance()
                .getApiInterface().getCommunityPost("tre");

        call.enqueue(new Callback<List<postData>>() {
            @Override
            public void onResponse(Call<List<postData>> call, Response<List<postData>> response) {

                if (response.isSuccessful()) {

                    List<postData> po = response.body();

                    postAdapter.clear();
                    postDataArrayList.addAll(response.body());
                    postAdapter.notifyDataSetChanged();

                    Log.d("sizeif", String.valueOf(response.body().size()));


                } else {

                    Toast.makeText(CommunityPage.this, "not sucesss...", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<postData>> call, Throwable t) {
                if (!noInternetDialog.isConnected()){
                    noInternetDialog.create();
                }
            }
        });


    }
}
