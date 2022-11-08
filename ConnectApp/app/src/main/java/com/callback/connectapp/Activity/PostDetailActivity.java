package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.CommentAdapter;
import com.callback.connectapp.adapter.HomePostAdapter;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Comment;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.postData;
import com.callback.connectapp.retrofit.APIClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Comment> commentList;

    private AppConfig appConfig;
    private CommentAdapter commentAdapter;
    private EditText commentText;
    private ImageView sendCommentBtn;
    private TextView userName, communityName, postText, likeCount, dislikeCount, commentCount, time;
    private ImageView profileImg, postImage, shareBtn, LikeBtn, DislikeBtn, commentBtn;
    private String PostId;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        appConfig = new AppConfig(this);
        recyclerView = findViewById(R.id.commentRecycler);
        postText = findViewById(R.id.readMoreTextView2);
        profileImg = findViewById(R.id.profile_image);
        userName = findViewById(R.id.textView4);
        postImage = findViewById(R.id.imageView3);
        commentList = new ArrayList<Comment>();
        sendCommentBtn = findViewById(R.id.sendcomment);
        commentText = findViewById(R.id.typecommet);
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commentAdapter);
        noInternetDialog = new NoInternetDialog(this);
        PostId = getIntent().getStringExtra("PostId");
        fetchAllComment();

        sendCommentBtn.setOnClickListener(view -> {
            String text = commentText.getText().toString();

            if (!text.isEmpty()) {
                Comment comment = new Comment(appConfig.getUserID(), "", text);
                Call<ApiResponse> call = APIClient.getInstance()
                        .getApiInterface().commentPost(PostId, comment);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            commentList.add(comment);
                            commentAdapter.notifyDataSetChanged();
                            Toast.makeText(PostDetailActivity.this, "comment send", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        if (!noInternetDialog.isConnected())
                            noInternetDialog.create();
                    }
                });
            }
        });
    }

    private void fetchAllComment() {

        Call<postData> call = APIClient.getInstance()
                .getApiInterface().getPost(PostId);

        call.enqueue(new Callback<postData>() {
            @Override
            public void onResponse(Call<postData> call, Response<postData> response) {
                if (response.isSuccessful()) {

                    List<Comment> comment = response.body().getComments();
                    postText.setText(response.body().getInfo());
                    String postImg = response.body().getImage();
                    if (!Objects.equals(postImg, "")) {
                        postImage.setVisibility(View.VISIBLE);
                        Picasso.get().load(postImg).into(postImage);
                    }

                    String userId = response.body().getUserId();

                    Call<User> cal = APIClient.getInstance()
                            .getApiInterface().getUser(userId);
                    cal.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {

                                userName.setText(response.body().getName());

                                if (!Objects.equals(response.body().getImageUrl(), "")) {
                                    profileImg.setVisibility(View.VISIBLE);
                                    Picasso.get().load(response.body().getImageUrl()).into(profileImg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            if (!noInternetDialog.isConnected())
                                noInternetDialog.create();
                            Toast.makeText(PostDetailActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });


                    commentList.addAll(comment);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<postData> call, Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
                Toast.makeText(PostDetailActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });


    }
}