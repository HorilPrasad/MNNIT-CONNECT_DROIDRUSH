package com.callback.connectapp.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    Context context;
    List<Community> communityList;

    public CommunityAdapter(Context context, List<Community> communityList) {
        this.context = context;
        this.communityList = communityList;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_list, parent, false);

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        Community community = communityList.get(position);
        holder.communityName.setText(community.getName());
        Toast.makeText(context, "aya", Toast.LENGTH_SHORT).show();

        if (!Objects.equals(community.getImage(), ""))
            Picasso.get().load(community.getImage()).into(holder.communityImage);


        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String myJson = gson.toJson(community);
                Intent intent = new Intent(context, CommunityPage.class);
                intent.putExtra("myjson", myJson);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {

        TextView communityName, communityCreated;
        ImageView communityImage;
        LinearLayout itemLayout;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.communityLayout);
            communityName = itemView.findViewById(R.id.community_list_name);
            communityCreated = itemView.findViewById(R.id.community_list_created);
            communityImage = itemView.findViewById(R.id.community_list_image);
        }
    }
}
