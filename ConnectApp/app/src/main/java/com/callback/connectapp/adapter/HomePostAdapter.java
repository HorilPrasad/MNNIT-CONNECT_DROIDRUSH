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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.callback.connectapp.R;
import com.callback.connectapp.model.postData;

import java.util.ArrayList;
public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.postViewHolder> {

    Context context;
    ArrayList<postData> postDataArrayList;

    public HomePostAdapter(Context context, ArrayList<postData> postDataArrayList) {
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

//        Shops shop=shopsArrayList.get(position);
//        holder.shopName.setText(shop.getShopName());
//        holder.shopAddress.setText(shop.getShopLocation());
//        holder.shopType.setText(shop.getShopType());

        postData userPost=postDataArrayList.get(position);
        holder.userName.setText(userPost.getName());
        holder.communityName.setText(userPost.getCommunityName());
        //glide use to add img in post
        holder.likeCount.setText("Likes "+userPost.getLikeCount() );
        holder.commentCount.setText("comments "+userPost.getCommentCount() );
        holder.dislikeCount.setText("dislike "+userPost.getDislikeCount() );
        holder.postText.setText(userPost.getPostText());
        
//        holder.shopListLayout.setOnClickListener(v -> {
//            Intent intent=new Intent(context, ProductListActivity.class);
//            intent.putExtra("uid",shop.getUserId());
//            intent.putExtra("shopname",shop.getShopName());
//            context.startActivity(intent);
//        });
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