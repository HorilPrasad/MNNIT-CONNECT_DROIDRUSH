package com.callback.connectapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.callback.connectapp.Activity.CommunityPage;
import com.callback.connectapp.R;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityAdapter extends RecyclerView.Adapter <CommunityAdapter.CommunityViewHolder> {

    Context context;
    List <Community> communityList;

    public CommunityAdapter (Context context , List <Community> communityList) {
        this.context = context;
        this.communityList = communityList;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder (@NonNull ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_list , parent , false);

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull CommunityViewHolder holder , int position) {
        Community community = communityList.get(position);

        holder.communityName.setText(community.getName());

        if (!Objects.equals(community.getImage() , ""))
            Picasso.get().load(community.getImage()).placeholder(R.drawable.background).into(holder.communityImage);

        holder.itemLayout.setOnClickListener(view -> {

            Intent intent = new Intent(context , CommunityPage.class);
            intent.putExtra("id" , community.get_id());
            context.startActivity(intent);


        });

        Call <User> call = APIClient.getInstance().getApiInterface()
                .getUser(community.getUserId());

        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {

                if (response.isSuccessful()) {

                    holder.communityCreated.setText("Created By " + response.body().getName());
                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount () {
        return communityList.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {

        TextView communityName, communityCreated;
        ImageView communityImage;
        LinearLayout itemLayout;

        public CommunityViewHolder (@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.communityLayout);
            communityName = itemView.findViewById(R.id.community_list_name);
            communityCreated = itemView.findViewById(R.id.community_list_created);
            communityImage = itemView.findViewById(R.id.community_list_image);
        }
    }
}
