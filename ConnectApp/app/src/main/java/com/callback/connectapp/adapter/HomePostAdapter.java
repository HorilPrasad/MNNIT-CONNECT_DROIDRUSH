package com.callback.connectapp.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.callback.connectapp.R;
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

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.postViewHolder> {

    Context context;
    List <postData> postDataArrayList;

    public HomePostAdapter(Context context, List<postData> postDataArrayList) {
        this.context = context;
        this.postDataArrayList = postDataArrayList;
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);

        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {



        postData userPost=postDataArrayList.get(position);


//        holder.communityName.setText(userPost.getCommunityName());
        holder.likeCount.setText("likes "+userPost.getLikeCount(userPost.getLikes()) );
        holder.commentCount.setText("comments "+userPost.getCommenntCount(userPost.getComments()) );
        holder.dislikeCount.setText("dislike "+userPost.getDislikeCount(userPost.getDislikes()) );
        holder.postText.setText(userPost.getInfo());
        holder.time.setText(userPost.getRelativeTime());
        String url= userPost.getImage();

        if(!Objects.equals(url,"")) {
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(holder.postImage);
        }

        Call <User> call = APIClient.getInstance()
                .getApiInterface().getUser(userPost.getUserId());
        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if(response.isSuccessful()){

                    holder.userName.setText(response.body().getName());

//                    Picasso.get().load(url).placeholder(R.mipmap.ic_person)
//                            .into(holder.profileImg);
                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                Log.d("sizeifs","user data fail");
            }
        });
    }

    @Override
    public int getItemCount() {

        return postDataArrayList.size();
    }


    public static class postViewHolder extends RecyclerView.ViewHolder{

        TextView userName,communityName,postText,likeCount,dislikeCount,commentCount,time;
        ImageView profileImg,postImage,shareBtn;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.textView4);
            communityName=itemView.findViewById(R.id.textView5);
            time=itemView.findViewById(R.id.textView6);
            postText=itemView.findViewById(R.id.readMoreTextView2);
            likeCount=itemView.findViewById(R.id.likeCount);
            dislikeCount=itemView.findViewById(R.id.dislikeCount);
            commentCount=itemView.findViewById(R.id.commentCount);
            profileImg=itemView.findViewById(R.id.profile_image);
            postImage=itemView.findViewById(R.id.imageView3);
            shareBtn=itemView.findViewById(R.id.imageView);


        }
    }
}