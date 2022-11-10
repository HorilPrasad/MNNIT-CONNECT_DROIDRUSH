package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.callback.connectapp.R;
import com.callback.connectapp.adapter.CommentAdapter;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Comment;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.PostData;
import com.callback.connectapp.retrofit.APIClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List <Comment> commentList;

    private AppConfig appConfig;
    private CommentAdapter commentAdapter;
    private EditText commentText;
    private ImageView sendCommentBtn;
    private TextView userName, communityName, postText, likeCount, dislikeCount, commentCount, time;
    private ImageView profileImg, postImage, shareBtn, LikeBtn, DislikeBtn, commentBtn;
    private String PostId;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog progressDialog;
    private RelativeLayout constraintLayout;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        appConfig = new AppConfig(this);
        recyclerView = findViewById(R.id.commentRecycler);
        postText = findViewById(R.id.readMoreTextView2);
        profileImg = findViewById(R.id.profile_image);
        time = findViewById(R.id.textView6);
        userName = findViewById(R.id.textView4);
        postImage = findViewById(R.id.imageView3);
        commentList = new ArrayList <>();
        sendCommentBtn = findViewById(R.id.sendcomment);
        commentText = findViewById(R.id.typecommet);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);
        commentCount = findViewById(R.id.commentCount);
        LikeBtn = findViewById(R.id.likebtn);
        DislikeBtn = findViewById(R.id.dislikeBtn);
        commentBtn = findViewById(R.id.commentsBtn);
        commentAdapter = new CommentAdapter(this , commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commentAdapter);
        noInternetDialog = new NoInternetDialog(this);
        PostId = getIntent().getStringExtra("PostId");
        constraintLayout = findViewById(R.id.postdetail_layout);
        fetchAllComment();

        sendCommentBtn.setOnClickListener(view -> {
            String text = commentText.getText().toString();

            if (!text.isEmpty()) {
                Comment comment = new Comment(appConfig.getUserID() , new Date().toString() , text);
                Call <ApiResponse> call = APIClient.getInstance()
                        .getApiInterface().commentPost(PostId , comment);
                call.enqueue(new Callback <ApiResponse>() {
                    @Override
                    public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {
                        if (response.isSuccessful()) {
                            commentList.add(comment);
                            commentAdapter.notifyDataSetChanged();
                            commentText.setText("");
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
    }

    private void fetchAllComment () {
        loading();
        Call <PostData> call = APIClient.getInstance()
                .getApiInterface().getPost(PostId);

        call.enqueue(new Callback <PostData>() {
            @Override
            public void onResponse (Call <PostData> call , Response <PostData> response) {
                if (response.isSuccessful()) {
                    PostData post = response.body();
                    List <Comment> comment = response.body().getComments();
                    postText.setText(response.body().getInfo());

                    String userId = appConfig.getUserID();
                    boolean flag = post.getLikes().contains(userId);

                    likeCount.setText(post.getLikeCount(post.getLikes())+" likes");
                    commentCount.setText(post.getCommenntCount(post.getComments())+" comments");
                    dislikeCount.setText(post.getDislikeCount(post.getDislikes())+" dislike");
                    if (flag) {
                        LikeBtn.setImageResource(R.drawable.filledlike);
                    } else {
                        LikeBtn.setImageResource(R.drawable.like);
                    }

                    if (post.getDislikes().contains(userId)) {

                        DislikeBtn.setImageResource(R.drawable.filledislike);
                    } else {
                        DislikeBtn.setImageResource(R.drawable.dislike);
                    }
                    String postImg = response.body().getImage();
                    if (!Objects.equals(postImg , "")) {
                        postImage.setVisibility(View.VISIBLE);
                        Picasso.get().load(postImg).into(postImage);
                    }
                    time.setText(response.body().getTimeIn());

                    String postUserId = response.body().getUserId();

                    Call <User> cal = APIClient.getInstance()
                            .getApiInterface().getUser(postUserId);
                    cal.enqueue(new Callback <User>() {
                        @Override
                        public void onResponse (Call <User> call , Response <User> response) {
                            if (response.isSuccessful()) {

                                userName.setText(response.body().getName());

                                if (!Objects.equals(response.body().getImageUrl() , "")) {
                                    profileImg.setVisibility(View.VISIBLE);
                                    Picasso.get().load(response.body().getImageUrl()).into(profileImg);
                                }
                                constraintLayout.setVisibility(View.VISIBLE);
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure (Call <User> call , Throwable t) {
                            if (!noInternetDialog.isConnected())
                                noInternetDialog.create();
                            progressDialog.dismiss();
                        }
                    });


                    commentList.addAll(comment);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure (Call <PostData> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
                progressDialog.dismiss();
            }
        });

    }

    private void loading () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Post");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}