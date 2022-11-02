package com.callback.connectapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.callback.connectapp.Activity.CreateProfile;
import com.callback.connectapp.Activity.signUpActivity;
import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.postData;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatePostFragment extends Fragment {
    ActivityResultLauncher <String> launcher;
    private Uri imageUri = null;
    private ImageView postImage;
    private EditText etPost;
    private ImageView sendBtn,attachBtn;
    private ProgressBar pb;
    private AppConfig appConfig;
    FirebaseStorage storage;
    postData post;
    private String userID;
   public postData data;

 private String url;

    private ConstraintLayout homeLayout;

    public CreatePostFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post , container , false);
        postImage = view.findViewById(R.id.postImage);
        etPost = view.findViewById(R.id.etPost);
        attachBtn=view.findViewById(R.id.attach);
        sendBtn=view.findViewById(R.id.sendBtn);
        appConfig = new AppConfig(getContext());
        userID = appConfig.getUserID();


       data=new postData("","","");

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback <Uri>() {
                    @Override
                    public void onActivityResult (Uri result) {
                        postImage.setImageURI(result);

                        //storing Img in firebase storage
                        storage=FirebaseStorage.getInstance();

                        Calendar cal=Calendar.getInstance();
                        long k=cal.getTimeInMillis();
                        String key= Long.toString(k);
                        final StorageReference reference = storage.getReference().child("post").child(key);
                                reference.putFile(result).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess (UploadTask.TaskSnapshot taskSnapshot) {

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener <Uri>() {
                                    @Override
                                    public void onSuccess (Uri uri) {
                                        // store uri in mongo db
                                      String imageUrl=uri.toString();
                                      url=imageUrl;
                                      data.setImage(uri.toString());

                                    }
                                });
                            }
                        });
                    }
                });

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                launcher.launch("image/*");
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                  if(url==null){
                      url="";
                  }

                    postData newPost=new postData(userID, etPost.getText().toString(),url.toString());
                    Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                    Call <ApiResponse> call = APIClient.getInstance()
                            .getApiInterface().createPost(newPost);
                    url="";
                    call.enqueue(new Callback <ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response <ApiResponse> response) {
                            ApiResponse apiResponse = response.body();
                            if(response.isSuccessful()){
                                  etPost.setText("");


                                Toast.makeText(getContext(),"post is uploaded.",Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(getContext(), apiResponse.getMessage()+ "status : "+apiResponse.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "fail...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }




        });

        return view;
    }


}