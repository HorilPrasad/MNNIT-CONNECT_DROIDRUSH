package com.callback.connectapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.callback.connectapp.Activity.UpdateProfile;
import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private AppConfig appConfig;
    ActivityResultLauncher<String> launcher;
    ImageView profileImg;
    TextView name, email, phone, regNo, course;
    ImageButton editProfile;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    NoInternetDialog noInternetDialog;
    RelativeLayout relativeLayout;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        regNo = view.findViewById(R.id.regno);
        course = view.findViewById(R.id.school);
        editProfile = view.findViewById(R.id.profile_edit_button);
        noInternetDialog = new NoInternetDialog(getContext());
        relativeLayout = view.findViewById(R.id.profile_layout);

        profileImg = view.findViewById(R.id.profile_user_image);
        appConfig = new AppConfig(getContext());

        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), UpdateProfile.class));
        });

        getUserData();


        return view;
    }

    private void getUserData() {
        loading();
        Call<User> call = APIClient.getInstance().getApiInterface()
                .getUser(appConfig.getUserID());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    name.setText(response.body().getName());
                    email.setText(response.body().getEmail());
                    phone.setText(response.body().getPhone());
                    regNo.setText(response.body().getRegNo());
                    course.setText(response.body().getBranch());

                    if (!Objects.equals(response.body().getImageUrl(), ""))
                        Picasso.get().load(response.body().getImageUrl()).placeholder(R.drawable.avatar).into(profileImg);

                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
                progressDialog.dismiss();
            }
        });
    }

    private void loading() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetch");
        progressDialog.setMessage("User data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }
}