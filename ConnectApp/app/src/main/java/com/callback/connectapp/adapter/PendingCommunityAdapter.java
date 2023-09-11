package com.callback.connectapp.adapter;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.callback.connectapp.R;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingCommunityAdapter extends RecyclerView.Adapter<PendingCommunityAdapter.PendingCommunityViewHolder> {

    Context context;
    List<Community> communityList;
    NoInternetDialog noInternetDialog;

    public PendingCommunityAdapter(Context context, List<Community> communityList) {
        this.context = context;
        this.communityList = communityList;
        noInternetDialog = new NoInternetDialog(context);
    }

    @NonNull
    @Override
    public PendingCommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_communities_list,parent,false);
        return new PendingCommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingCommunityViewHolder holder, int position) {
        Community community = communityList.get(position);


        holder.verifyBtn.setOnClickListener(v -> {
            Community community1 = new Community();
            community1.setVerified(true);
            Call<ApiResponse> call = APIClient.getInstance()
                    .getApiInterface().verifyCommunity(community.get_id(),community1);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Verified", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
        });

        Call<User> call = APIClient.getInstance()
                .getApiInterface().getUser(community.getUserId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    holder.userName.setText(holder.userName.getText().toString()+user.getName());
                    holder.userPhone.setText(holder.userPhone.getText().toString()+user.getPhone());
                    holder.userBranch.setText(holder.userBranch.getText().toString()+user.getBranch());
                    holder.userReg.setText(holder.userReg.getText().toString()+user.getRegNo());
                    holder.communityName.setText(community.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (!noInternetDialog.isConnected()){
                    noInternetDialog.create();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public static class PendingCommunityViewHolder extends RecyclerView.ViewHolder{

        TextView communityName,userName,userReg,userPhone,userBranch,verifyBtn;

        public PendingCommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            communityName = itemView.findViewById(R.id.pending_community_name);
            userName = itemView.findViewById(R.id.pending_community_user_name);
            userReg = itemView.findViewById(R.id.pending_community_user_reg);
            userPhone = itemView.findViewById(R.id.pending_community_phone);
            userBranch = itemView.findViewById(R.id.pending_community_user_branch);
            verifyBtn = itemView.findViewById(R.id.pending_community_verify_btn);

        }
    }
}
