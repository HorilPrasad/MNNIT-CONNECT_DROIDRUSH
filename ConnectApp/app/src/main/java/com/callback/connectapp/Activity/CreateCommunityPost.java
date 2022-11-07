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


                    postImage.setImageURI(result);

                    //storing Img in firebase storage
                    storage = FirebaseStorage.getInstance();

                    Calendar cal = Calendar.getInstance();
                    long k = cal.getTimeInMillis();
                    String key = Long.toString(k);
                    final StorageReference reference = storage.getReference().child("post").child(key);
                    reference.putFile(result).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // store uri in mongo db
                        url = uri.toString();
//                        data.setImage(uri.toString());


                    }));
                });

        attachBtn.setOnClickListener(view1 -> launcher.launch("image/*"));




        sendBtn.setOnClickListener(view12 -> {

            if (url == null) {
                url = "";
            }

            postData newPost = new postData(userID, etPost.getText().toString(), url,communityId);
            Toast.makeText(CreateCommunityPost.this, url, Toast.LENGTH_LONG).show();
            Call <ApiResponse> call = APIClient.getInstance()
                    .getApiInterface().createPost(newPost);
            call.enqueue(new Callback <ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response <ApiResponse> response) {
                    ApiResponse apiResponse = response.body();
                    if (response.isSuccessful()) {
                        etPost.setText("");


                        Toast.makeText(CreateCommunityPost.this, "post is uploaded.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(CreateCommunityPost.this, apiResponse.getMessage() + "status : " + apiResponse.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(CreateCommunityPost.this, "fail...", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void loading() {
        progressDialog = new ProgressDialog(CreateCommunityPost.this);
        progressDialog.setTitle("Data");
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

    }
}