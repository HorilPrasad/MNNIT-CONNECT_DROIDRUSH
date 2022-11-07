package com.callback.connectapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.postData;
import com.callback.connectapp.retrofit.APIClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCommunityPost extends AppCompatActivity {

    ActivityResultLauncher <String> launcher;
    private ImageView postImage;
    private EditText etPost;
    private ImageView sendBtn, attachBtn;
    private ProgressBar pb;
    private AppConfig appConfig;
    FirebaseStorage storage;
    postData post;
    private ProgressDialog progressDialog;
    String communityId;
    private String userID;
    public postData data;
    NoInternetDialog noInternetDialog;
    private String url;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community_post);

        postImage = findViewById(R.id.postImage);
        etPost = findViewById(R.id.etPost);
        attachBtn = findViewById(R.id.attach);
        sendBtn = findViewById(R.id.sendBtn);
        appConfig = new AppConfig(this);
        userID = appConfig.getUserID();

        Intent i=getIntent();
        communityId=i.getStringExtra("communityId");

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , result -> {
                    loading();
                    progressDialog.setTitle("Image");
                    progressDialog.setMessage("Uploading...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();


                    //storing Img in firebase storage
                    storage = FirebaseStorage.getInstance();

                    Calendar cal = Calendar.getInstance();
                    long k = cal.getTimeInMillis();
                    String key = Long.toString(k);
                    final StorageReference reference = storage.getReference().child("post").child(key);
                    reference.putFile(result).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // store uri in mongo db
                        url = uri.toString();
                        postImage.setImageURI(result);
                        progressDialog.dismiss();

                    })).addOnProgressListener(snapshot -> {
                        double progress = (1.0*100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        progressDialog.setProgress((int) progress);
                    }).addOnFailureListener(e ->{
                        if (!noInternetDialog.isConnected())
                            noInternetDialog.create();
                        progressDialog.dismiss();
                    });
                });

        attachBtn.setOnClickListener(view1 -> launcher.launch("image/*"));




        sendBtn.setOnClickListener(view12 -> {

            if (url == null) {
                url = "";
            }
            loading();
            progressDialog.setTitle("Post");
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            postData newPost = new postData(userID, etPost.getText().toString(), url,communityId);
            Call<ApiResponse> call = APIClient.getInstance()
                    .getApiInterface().createPost(newPost);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    ApiResponse apiResponse = response.body();
                    if (response.isSuccessful()) {
                        etPost.setText("");

                            url="";
                    } else {

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                    progressDialog.dismiss();
                }
            });
        });


    }



    private void loading() {
        progressDialog = new ProgressDialog(CreateCommunityPost.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);

    }


}