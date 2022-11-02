package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class createCommunity extends AppCompatActivity {

    private EditText name,tag,rule,about;
    private ImageView image;
    private TextView selectImage;
    private Button create;
    private AppConfig appConfig;

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
            List<String> list = new ArrayList<>();
            list.add(appConfig.getUserID());
            Community community = new Community(appConfig.getUserID(),cName,cAbout,cTag,cRule);

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