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
import com.callback.connectapp.model.Community;

import java.util.List;

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
        View view = LayoutInflater.from(context).inflate(R.layout.community_list,parent,false);

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        Community community = communityList.get(position);
        holder.communityName.setText(community.getName());
        Toast.makeText(context, "aya", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder{

        TextView communityName,communityCreated;
        ImageView communityImage;
        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);

            communityName = itemView.findViewById(R.id.community_list_name);
            communityCreated = itemView.findViewById(R.id.community_list_created);
            communityImage = itemView.findViewById(R.id.community_list_image);
        }
    }
}
