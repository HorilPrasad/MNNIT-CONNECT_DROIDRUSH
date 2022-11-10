package com.callback.connectapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.callback.connectapp.R;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.Comment;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.squareup.picasso.Picasso;


import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter <CommentAdapter.CommentViewHolder> {

    Context context;
    List <Comment> commentList;
    NoInternetDialog noInternetDialog;

    public CommentAdapter (Context context , List <Comment> communityList) {
        this.context = context;
        this.commentList = communityList;
        noInternetDialog = new NoInternetDialog(context);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder (@NonNull ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_comment_row , parent , false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull CommentViewHolder holder , int position) {
        Comment comment = commentList.get(position);
//        Toast.makeText(context, comment.getComment()+"", Toast.LENGTH_SHORT).show();
        holder.commentText.setText(comment.getComment());
        holder.time.setText(comment.getTimeIn());
        holder.userImg.setOnClickListener(v -> {
//            Toast.makeText(context , comment.getUser() + "" , Toast.LENGTH_SHORT).show();
        });

        setUserData(comment , holder);

    }

    private void setUserData (Comment comment , CommentViewHolder holder) {
        Call <User> call = APIClient.getInstance()
                .getApiInterface().getUser(comment.getUser());
        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if (response.isSuccessful()) {
                    holder.userName.setText(response.body().getName());
                    if (!response.body().getImageUrl().equals(""))
                        Picasso.get().load(response.body().getImageUrl()).placeholder(R.drawable.avatar).into(holder.userImg);

                    if (!Objects.equals(response.body().getImageUrl() , "")) {
                        holder.userImg.setVisibility(View.VISIBLE);
                        Picasso.get().load(response.body().getImageUrl()).into(holder.userImg);
                    }
                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
            }
        });
    }


    @Override
    public int getItemCount () {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView userName, commentText, time;
        ImageView userImg;

        public CommentViewHolder (@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.commentname);
            commentText = itemView.findViewById(R.id.commenttext);
            time = itemView.findViewById(R.id.commenttime);
            userImg = itemView.findViewById(R.id.userImg);
        }
    }
}
