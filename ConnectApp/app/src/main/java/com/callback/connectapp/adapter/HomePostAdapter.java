package com.callback.connectapp.adapter;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.callback.connectapp.Activity.PostDetailActivity;
import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Comment;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.postData;
import com.callback.connectapp.retrofit.APIClient;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePostAdapter extends RecyclerView.Adapter <HomePostAdapter.postViewHolder> {

    Context context;
    List <postData> postDataArrayList;
    AppConfig appConfig;
    NoInternetDialog noInternetDialog;


    public HomePostAdapter (Context context , List <postData> postDataArrayList) {
        this.context = context;
        this.postDataArrayList = postDataArrayList;
        appConfig = new AppConfig(context);
        noInternetDialog = new NoInternetDialog(context);
    }


    @NonNull
    @Override
    public postViewHolder onCreateViewHolder (@NonNull final ViewGroup parent , final int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.post_item , parent , false);

        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull final postViewHolder holder , final int position) {


        final postData userPost = postDataArrayList.get(position);

        final String url = postDataArrayList.get(position).getImage();
        holder.postImage.setImageDrawable(null);
        holder.postImage.setVisibility(View.GONE);

//        holder.communityName.setText(userPost.getCommunityName());
        holder.likeCount.setText("likes " + userPost.getLikeCount(userPost.getLikes()));
        holder.commentCount.setText("comments " + userPost.getCommenntCount(userPost.getComments()));
        holder.dislikeCount.setText("dislike " + userPost.getDislikeCount(userPost.getDislikes()));
        holder.postText.setText(userPost.getInfo());
        holder.time.setText(userPost.getTimeIn());


        if (!Objects.equals(url , "")) {
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(holder.postImage);
        }


        String userId = appConfig.getUserID();

        boolean flag = userPost.getLikes().contains(userId);

        if (flag) {
            holder.LikeBtn.setImageResource(R.drawable.filledlike);
        } else {
            holder.LikeBtn.setImageResource(R.drawable.like);
        }

        Boolean f = userPost.getDislikes().contains(userId);
        if (userPost.getDislikes().contains(userId)) {

            holder.DislikeBtn.setImageResource(R.drawable.filledislike);
        } else {
            holder.DislikeBtn.setImageResource(R.drawable.dislike);
        }

        updatePost(userPost , holder);
        Call <User> call = APIClient.getInstance()
                .getApiInterface().getUser(userPost.getUserId());
        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if (response.isSuccessful()) {

                    holder.userName.setText(response.body().getName());
                    String url = "";
                    url = response.body().getImageUrl();
                    if (!Objects.equals(url , "")) {

                        Picasso.get().load(url).placeholder(R.drawable.avatar)
                                .into(holder.profileImg);
                    }

                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });

        holder.LikeBtn.setOnClickListener(view -> {

            User user = new User();
            user.set_id(appConfig.getUserID());
            Call <ApiResponse> call1 = APIClient.getInstance()
                    .getApiInterface().likePost(userPost.get_id() , user);

            call1.enqueue(new Callback <ApiResponse>() {
                @Override
                public void onResponse (Call <ApiResponse> call1 , Response <ApiResponse> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(context , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        updatePost(userPost , holder);

                    }
                }

                @Override
                public void onFailure (Call <ApiResponse> call1 , Throwable t) {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                }
            });


        });

        holder.DislikeBtn.setOnClickListener(view -> {
            User user = new User();
            user.set_id(appConfig.getUserID());
            Call <ApiResponse> call12 = APIClient.getInstance()
                    .getApiInterface().dislikePost(userPost.get_id() , user);

            call12.enqueue(new Callback <ApiResponse>() {
                @Override
                public void onResponse (Call <ApiResponse> call12 , Response <ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        updatePost(userPost , holder);
                    }
                }

                @Override
                public void onFailure (Call <ApiResponse> call12 , Throwable t) {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                }
            });


        });
        holder.commentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context , PostDetailActivity.class);
            intent.putExtra("PostId" , userPost.get_id());
            context.startActivity(intent);
        });
        holder.constraintLayout.setVisibility(View.VISIBLE);
    }

    private void updatePost (postData userPost , postViewHolder holder) {

        Call <postData> call12 = APIClient.getInstance()
                .getApiInterface().getPost(userPost.get_id());

        call12.enqueue(new Callback <postData>() {
            @Override
            public void onResponse (Call <postData> call , Response <postData> response) {

                if (response.isSuccessful()) {

                    postData post = response.body();

                    holder.likeCount.setText("likes " + post.getLikeCount(post.getLikes()));
                    holder.commentCount.setText("comments " + post.getCommenntCount(post.getComments()));
                    holder.dislikeCount.setText("dislike " + post.getDislikeCount(post.getDislikes()));

                    String userId = appConfig.getUserID();

                    boolean flag = post.getLikes().contains(userId);

                    if (flag) {
                        holder.LikeBtn.setImageResource(R.drawable.filledlike);
                    } else {
                        holder.LikeBtn.setImageResource(R.drawable.like);
                    }

                    Boolean f = post.getDislikes().contains(userId);
                    if (post.getDislikes().contains(userId)) {

                        holder.DislikeBtn.setImageResource(R.drawable.filledislike);
                    } else {
                        holder.DislikeBtn.setImageResource(R.drawable.dislike);
                    }
                }
            }

            @Override
            public void onFailure (Call <postData> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });
    }


    @Override
    public int getItemCount () {

        return postDataArrayList.size();
    }

    public void clear () {
        postDataArrayList.clear();
        notifyDataSetChanged();
    }

    public static class postViewHolder extends RecyclerView.ViewHolder {

        TextView userName, communityName, postText, likeCount, dislikeCount, commentCount, time;
        ImageView profileImg, postImage, shareBtn, LikeBtn, DislikeBtn, commentBtn;
        ConstraintLayout constraintLayout;

        public postViewHolder (@NonNull final View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.textView4);
            communityName = itemView.findViewById(R.id.textView5);
            time = itemView.findViewById(R.id.textView6);
            postText = itemView.findViewById(R.id.readMoreTextView2);
            likeCount = itemView.findViewById(R.id.likeCount);
            dislikeCount = itemView.findViewById(R.id.dislikeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            profileImg = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.imageView3);
            shareBtn = itemView.findViewById(R.id.imageView);

            LikeBtn = itemView.findViewById(R.id.likebtn);
            DislikeBtn = itemView.findViewById(R.id.dislikeBtn);
            commentBtn = itemView.findViewById(R.id.commentsBtn);
            constraintLayout = itemView.findViewById(R.id.post_item_layout);
            constraintLayout.setVisibility(View.GONE);


        }
    }


}