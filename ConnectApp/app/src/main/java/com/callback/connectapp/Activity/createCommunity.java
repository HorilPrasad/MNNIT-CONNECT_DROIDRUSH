package com.callback.connectapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class createCommunity extends AppCompatActivity {
    ActivityResultLauncher <String> launcher;
    private EditText name,tag,rule,about;
    private ImageView image;
    private TextView selectImage;
    private Button create;
    private AppConfig appConfig;
    FirebaseStorage storage;
    String imageUrl="";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        appConfig = new AppConfig(this);
        name = findViewById(R.id.community_name);
        tag = findViewById(R.id.community_tag);
        rule = findViewById(R.id.community_rule);
        image = findViewById(R.id.communityImg);
        selectImage = findViewById(R.id.community_uploadImg);
        create = findViewById(R.id.createCommunity);
        about = findViewById(R.id.about_community);

        create.setOnClickListener(v -> {
            String cName = name.getText().toString().trim();
            String cTag = tag.getText().toString().trim();
            String cRule = rule.getText().toString().trim();
            String cAbout = about.getText().toString().trim();
            List<String> members = new ArrayList<>();
            members.add(appConfig.getUserID());
            Community community = new Community(appConfig.getUserID(),cName,cAbout,cTag,cRule,imageUrl,members);

            if(check(cName,cTag,cRule)){
                communityCreate(community);
            }
        });
    }

    private void communityCreate(Community community) {
        Call<ApiResponse> call = APIClient.getInstance().getApiInterface()
                .createCommunity(community);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    startActivity(new Intent(createCommunity.this,MainActivity.class));
                    Toast.makeText(createCommunity.this, "community created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , result -> {
                    image.setImageURI(result);

                    //storing Img in firebase storage
                    storage= FirebaseStorage.getInstance();
                    Calendar cal=Calendar.getInstance();
                    long k=cal.getTimeInMillis();
                    String key= Long.toString(k);
                    final StorageReference reference = storage.getReference().child("profile").child(appConfig.getUserID());
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess (UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener <Uri>() {
                                @Override
                                public void onSuccess (Uri uri) {
                                    // store uri in mongo db
                                    imageUrl=uri.toString();

                                    Toast.makeText(createCommunity.this , imageUrl , Toast.LENGTH_SHORT).show();





                                }
                            });
                        }
                    });
                });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                launcher.launch("image/*");
            }
        });
    }

    private boolean check(String cName, String cTag, String cRule) {

        if(cName.isEmpty()){
            name.setError("Name can't be empty!");
            name.requestFocus();
            return false;
        }
        if(cTag.isEmpty()){
            tag.setError("Tag can't be empty!");
            tag.requestFocus();
            return false;
        }

        if(cRule.isEmpty()){
            rule.setError("Rule can't be empty!");
            rule.requestFocus();
            return false;
        }
        return true;

    }
}