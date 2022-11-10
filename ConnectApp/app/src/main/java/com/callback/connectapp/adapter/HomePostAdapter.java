package com.callback.connectapp.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.callback.connectapp.app.ImageDialog;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.PostData;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePostAdapter extends RecyclerView.Adapter <HomePostAdapter.postViewHolder> {

    Context context;
    List <PostData> postDataArrayList;
    AppConfig appConfig;
    NoInternetDialog noInternetDialog;


    public HomePostAdapter (Context context , List <PostData> postDataArrayList) {
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


        final PostData userPost = postDataArrayList.get(position);
        final Community[] community = new Community[1];
        final User[] userDetails = new User[1];
        Call <User> call = APIClient.getInstance()
                .getApiInterface().getUser(userPost.getUserId());
        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if (response.isSuccessful()) {
                    userDetails[0] = response.body();
                    if (!userPost.getCommunityId().equals("")) {
                        Call <Community> call3 = APIClient.getInstance().getApiInterface()
                                .getCommunityById(userPost.getCommunityId());
                        call3.enqueue(new Callback <Community>() {
                            @Override
                            public void onResponse (Call <Community> call3 , Response <Community> response) {
                                if (response.isSuccessful()) {
                                    community[0] = response.body();
                                    holder.userName.setText(community[0].getName());
                                    holder.branch.setText(userDetails[0].getName());
                                    if (!Objects.equals(community[0].getImage() , "")) {
                                        Picasso.get().load(community[0].getImage()).placeholder(R.drawable.avatar)
                                                .into(holder.profileImg);
                                    }
                                }
                            }

                            @Override
                            public void onFailure (Call <Community> call3 , Throwable t) {
                                if (!noInternetDialog.isConnected())
                                    noInternetDialog.create();
                            }
                        });


                    } else {
                        holder.userName.setText(userDetails[0].getName());
                        holder.branch.setText(userDetails[0].getBranch());
                        if (!Objects.equals(userDetails[0].getImageUrl() , "")) {
                            Picasso.get().load(userDetails[0].getImageUrl()).placeholder(R.drawable.avatar)
                                    .into(holder.profileImg);
                        }
                    }

                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });


        if (userPost.getSaved().contains(appConfig.getUserID())) {
            holder.savePost.setImageResource(R.drawable.bookmark_fill);
        }

        holder.savePost.setOnClickListener(v -> {
            if (!userPost.getSaved().contains(appConfig.getUserID())) {
                User user = new User();
                user.set_id(appConfig.getUserID());
                Call <ApiResponse> call1 = APIClient.getInstance()
                        .getApiInterface().savePost(userPost.get_id() , user);
                call1.enqueue(new Callback <ApiResponse>() {
                    @Override
                    public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {
                        if (response.isSuccessful()) {
                            holder.savePost.setImageResource(R.drawable.bookmark_fill);
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

        final String url = postDataArrayList.get(position).getImage();
        holder.postImage.setImageDrawable(null);
        holder.postImage.setVisibility(View.GONE);
        holder.likeCount.setText(userPost.getLikeCount(userPost.getLikes()) + " likes");
        holder.commentCount.setText(userPost.getCommenntCount(userPost.getComments()) + " comments");
        holder.dislikeCount.setText(userPost.getDislikeCount(userPost.getDislikes()) + " dislike");
        holder.postText.setText(userPost.getInfo());
        holder.time.setText(userPost.getTimeIn());


        if (!Objects.equals(url , "")) {
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(holder.postImage);
        }

        holder.postImage.setOnClickListener(v -> {
            ImageDialog imageDialog = new ImageDialog(context , userPost.getImage());
            imageDialog.createDialog();
        });

        String userId = appConfig.getUserID();

        boolean flag = userPost.getLikes().contains(userId);

        if (flag) {
            holder.LikeBtn.setImageResource(R.drawable.filledlike);
        } else {
            holder.LikeBtn.setImageResource(R.drawable.like);
        }

        if (userPost.getDislikes().contains(userId)) {

            holder.DislikeBtn.setImageResource(R.drawable.filledislike);
        } else {
            holder.DislikeBtn.setImageResource(R.drawable.dislike);
        }

        holder.profileImg.setOnClickListener(v -> {
            ImageDialog imageDialog;
            if (userPost.getCommunityId().equals(""))
                imageDialog = new ImageDialog(context , userDetails[0].getImageUrl());
            else
                imageDialog = new ImageDialog(context , community[0].getImage());
            imageDialog.createDialog();
        });

        updatePost(userPost , holder);


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

    private void updatePost (PostData userPost , postViewHolder holder) {

        Call <PostData> call12 = APIClient.getInstance()
                .getApiInterface().getPost(userPost.get_id());

        call12.enqueue(new Callback <PostData>() {
            @Override
            public void onResponse (Call <PostData> call , Response <PostData> response) {

                if (response.isSuccessful()) {

                    PostData post = response.body();

                    holder.likeCount.setText(post.getLikeCount(post.getLikes()) + " likes");
                    holder.commentCount.setText(post.getCommenntCount(post.getComments()) + " comments");
                    holder.dislikeCount.setText(post.getDislikeCount(post.getDislikes()) + " dislike");

                    String userId = appConfig.getUserID();

                    boolean flag = post.getLikes().contains(userId);

                    if (flag) {
                        holder.LikeBtn.setImageResource(R.drawable.filledlike);
                    } else {
                        holder.LikeBtn.setImageResource(R.drawable.like);
                    }

                    if (post.getDislikes().contains(userId)) {

                        holder.DislikeBtn.setImageResource(R.drawable.filledislike);
                    } else {
                        holder.DislikeBtn.setImageResource(R.drawable.dislike);
                    }
                }
            }

            @Override
            public void onFailure (Call <PostData> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });

        holder.shareBtn.setOnClickListener(view -> {

             if(holder.postText.getText()!=null)
            shareTextOnly(holder.postText.getText().toString() , userPost.getImage().toString());
        });
    }

    private void shareTextOnly (String titlee , String imgUrl) {
        String sharebody = titlee;

        // The value which we will sending through data via
        // other applications is defined
        // via the Intent.ACTION_SEND
        Intent intentt = new Intent(Intent.ACTION_SEND);

        // setting type of data shared as text
        intentt.setType("text/plain");
        intentt.putExtra(Intent.EXTRA_SUBJECT , "Subject Here");

        // Adding the text to share using putExtra
        intentt.putExtra(Intent.EXTRA_TEXT , sharebody + "\n \n " + imgUrl);
        context.startActivity(Intent.createChooser(intentt , "Share Via"));
    }

    @Override
    public int getItemCount () {

        return postDataArrayList.size();
    }


    public static class postViewHolder extends RecyclerView.ViewHolder {

        TextView userName, branch, postText, likeCount, dislikeCount, commentCount, time;
        ImageView profileImg, postImage, shareBtn, LikeBtn, DislikeBtn, commentBtn, savePost;
        ConstraintLayout constraintLayout;

        public postViewHolder (@NonNull final View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.textView4);
            branch = itemView.findViewById(R.id.textView5);
            time = itemView.findViewById(R.id.textView6);
            postText = itemView.findViewById(R.id.readMoreTextView2);
            likeCount = itemView.findViewById(R.id.likeCount);
            dislikeCount = itemView.findViewById(R.id.dislikeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            profileImg = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.imageView3);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            savePost = itemView.findViewById(R.id.save_post);
            LikeBtn = itemView.findViewById(R.id.likebtn);
            DislikeBtn = itemView.findViewById(R.id.dislikeBtn);
            commentBtn = itemView.findViewById(R.id.commentsBtn);
            constraintLayout = itemView.findViewById(R.id.post_item_layout);
            constraintLayout.setVisibility(View.GONE);


        }
    }


}