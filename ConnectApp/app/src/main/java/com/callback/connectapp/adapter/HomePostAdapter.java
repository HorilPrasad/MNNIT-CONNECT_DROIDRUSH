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
import androidx.recyclerview.widget.RecyclerView;


import com.callback.connectapp.Activity.PostDetailActivity;
import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
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

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.postViewHolder> {

    Context context;
    List <postData> postDataArrayList;
    AppConfig appConfig;


    public HomePostAdapter(Context context, List<postData> postDataArrayList) {
        this.context = context;

        this.postDataArrayList = postDataArrayList;
        appConfig=new AppConfig(context);
    }

    public HomePostAdapter (PostDetailActivity context , List<Comment> commentList) {
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,final  int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);

        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  final postViewHolder holder, final int position) {



        final  postData userPost = postDataArrayList.get(position);

       final String url=postDataArrayList.get(position).getImage();
     holder.postImage.setImageDrawable(null);
//        holder.communityName.setText(userPost.getCommunityName());
        holder.likeCount.setText("likes "+userPost.getLikeCount(userPost.getLikes()) );
        holder.commentCount.setText("comments "+userPost.getCommenntCount(userPost.getComments()) );
        holder.dislikeCount.setText("dislike "+userPost.getDislikeCount(userPost.getDislikes()) );
        holder.postText.setText(userPost.getInfo());
        holder.time.setText(userPost.getRelativeTime());

        Log.d("time",userPost.getRelativeTime());


        if(!Objects.equals(url,"")) {
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(holder.postImage);
        }
        Log.d("user",userPost.toString());

        String userId=appConfig.getUserID();

        Boolean flag=userPost.getLikes().contains(userId);

        if(flag){
            holder.LikeBtn.setImageResource(R.drawable.filledlike);
        }else{
            holder.LikeBtn.setImageResource(R.drawable.like);
        }

        Boolean f=userPost.getDislikes().contains(userId);
        if(userPost.getDislikes().contains(userId)){

            holder.DislikeBtn.setImageResource(R.drawable.filledislike);
        }
        else{
            holder.DislikeBtn.setImageResource(R.drawable.dislike);
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

        holder.LikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

//                Toast.makeText(context,"liked "+appConfig.getUserID(),Toast.LENGTH_LONG).show();
                User user = new User();
                user.set_id(appConfig.getUserID());
                Call <ApiResponse> call = APIClient.getInstance()
                        .getApiInterface().likePost(userPost.get_id(), user);





                call.enqueue(new Callback <ApiResponse>() {
                    @Override
                    public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {

                        if(response.isSuccessful()){
                            Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("ss",response.body().toString());

                            if(response.body().getStatus()==200){
                                holder.likeCount.setText("likes "+String.valueOf(userPost.getLikes().size()+1));
                                holder.LikeBtn.setImageResource(R.drawable.filledlike);
                            }
                            else{
                                holder.likeCount.setText("likes"+String.valueOf(userPost.getLikes().size()-1));
                                holder.LikeBtn.setImageResource(R.drawable.like);
                            }

                        }
                    }

                    @Override
                    public void onFailure (Call <ApiResponse> call , Throwable t) {
                        Toast.makeText(context,"fail ",Toast.LENGTH_SHORT).show();

                        Log.d("ss ","fail");
                    }
                });
            }
        });

        holder.DislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                User user = new User();
                user.set_id(appConfig.getUserID());
                Call <ApiResponse> call = APIClient.getInstance()
                        .getApiInterface().dislikePost(userPost.get_id(), user);

                call.enqueue(new Callback <ApiResponse>() {
                    @Override
                    public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("api",response.body().toString());

                            int size=userPost.getDislikes().size();
                            if(response.body().getStatus()==200){

                                holder.DislikeBtn.setImageResource(R.drawable.filledislike);
                            }
                            else{


                                holder.DislikeBtn.setImageResource(R.drawable.dislike);
                            }
                        }
                    }

                    @Override
                    public void onFailure (Call <ApiResponse> call , Throwable t) {
                        Log.d("api","fail dislike");
                    }
                });
            }
        });
         holder.commentBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick (View view) {
                 Intent intent = new Intent(context, PostDetailActivity.class);
                 intent.putExtra("PostId",userPost.get_id());
                 context.startActivity(intent);
         }
         });

    }

    @Override
    public int getItemCount() {

        return postDataArrayList.size();
    }
    public void clear() {
        postDataArrayList.clear();
        notifyDataSetChanged();
    }

    public static class postViewHolder extends RecyclerView.ViewHolder{

        TextView userName,communityName,postText,likeCount,dislikeCount,commentCount,time;
        ImageView profileImg,postImage,shareBtn,LikeBtn,DislikeBtn,commentBtn;

        public postViewHolder(@NonNull final  View itemView) {
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

            LikeBtn=itemView.findViewById(R.id.likebtn);
            DislikeBtn=itemView.findViewById(R.id.dislikeBtn);
            commentBtn=itemView.findViewById(R.id.commentsBtn);



        }
    }
}