package com.callback.connectapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.callback.connectapp.Activity.LoginActivity;
import com.callback.connectapp.Activity.UpdateProfile;
import com.callback.connectapp.R;
import com.callback.connectapp.adapter.HomePostAdapter;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.ImageDialog;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.PostData;
import com.callback.connectapp.retrofit.APIClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private AppConfig appConfig;
    ImageView profileImg;
    TextView name, email, phone, regNo, course;
    ImageView logoutButton;
    CardView editProfile,createPost;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    private RecyclerView postList_recycler;
    List <PostData> postDataArrayList;
    private HomePostAdapter homePostAdapter;

    NoInternetDialog noInternetDialog;
    RelativeLayout relativeLayout;
    BottomNavigationView bottomNavigationView;
    User user;

    public ProfileFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile , container , false);


        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        regNo = view.findViewById(R.id.regno);
        course = view.findViewById(R.id.school);
        editProfile = view.findViewById(R.id.profile_edit_button);
        noInternetDialog = new NoInternetDialog(getContext());
        relativeLayout = view.findViewById(R.id.profile_layout);
        logoutButton = view.findViewById(R.id.logout_button);
        createPost = view.findViewById(R.id.profile_create_post);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        profileImg = view.findViewById(R.id.profile_user_image);
        appConfig = new AppConfig(getContext());

        postList_recycler = view.findViewById(R.id.profilePostRecycler);


        postDataArrayList = new ArrayList <>();

        homePostAdapter = new HomePostAdapter(getContext() , postDataArrayList);

        profileImg.setOnClickListener(v -> {
            ImageDialog imageDialog = new ImageDialog(getContext(),user.getImageUrl());
            if (!user.getImageUrl().equals("") && user.getImageUrl() != null)
                imageDialog.createDialog();

        });
        postList_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        postList_recycler.setHasFixedSize(true);
        postList_recycler.setAdapter(homePostAdapter);

        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(getContext() , UpdateProfile.class));
        });

        logoutButton.setOnClickListener(v -> {
            appConfig.setUserID("");
            appConfig.setUserEmail("");
            appConfig.setLoginStatus(false);
            appConfig.setAuthToken("");
            getActivity().finishAffinity();
            Toast.makeText(getContext(), "Logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });
        createPost.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new CreatePostFragment()).commit();
        });

        getUserData();
        FetchAllPost();


        return view;
    }

    private void getUserData () {
        loading();
        Call <User> call = APIClient.getInstance().getApiInterface()
                .getUser(appConfig.getUserID());
        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if (response.isSuccessful()) {
                    name.setText(response.body().getName());
                    email.setText(response.body().getEmail());
                    phone.setText(response.body().getPhone());
                    regNo.setText(response.body().getRegNo());
                    course.setText(response.body().getBranch());
                    user = response.body();
<<<<<<< HEAD

=======
>>>>>>> 6c188239f7390e17d497ea0e03b64ac98cbb638a
                    profileImg.setImageDrawable(null);
                    if (!Objects.equals(response.body().getImageUrl() , ""))
                        Picasso.get().load(response.body().getImageUrl()).placeholder(R.drawable.avatar).into(profileImg);

                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                if (!noInternetDialog.isConnected())
                    noInternetDialog.create();
                progressDialog.dismiss();
            }
        });
    }


    private void FetchAllPost () {
        Call <List <PostData>> call = APIClient.getInstance()
                .getApiInterface().getAllPosts();

        call.enqueue(new Callback <List <PostData>>() {
            @Override
            public void onResponse (Call <List <PostData>> call , Response <List <PostData>> response) {

                if (response.isSuccessful()) {

                    List <PostData> postData = response.body();
                    for (PostData post:postData){
                        if (post.getUserId().equals(appConfig.getUserID()) && post.getCommunityId().equals(""))
                            postDataArrayList.add(post);
                    }


                    homePostAdapter.notifyDataSetChanged();


                } else {

                    Toast.makeText(getContext() , "not sucesss..." , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure (Call <List <PostData>> call , Throwable t) {
                Toast.makeText(getContext() , "fail..." , Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void loading () {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetch");
        progressDialog.setMessage("User data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }
}