package com.callback.connectapp.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {
    ActivityResultLauncher<String> launcher;
    private EditText name,phone,dob;
    private CircleImageView image;
    private Button update;
    private AppConfig appConfig;
    private Spinner gender,branch;
    private ImageButton back;
    private ArrayAdapter<CharSequence> genderAdapter,branchAdapter;
    private String phoneString,genderString,branchString,dobString,imageUrl;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = findViewById(R.id.edit_profile_name);
        phone = findViewById(R.id.edit_profile_phone);
        dob = findViewById(R.id.edit_profile_dob);
        image = findViewById(R.id.edit_profile_image);
        update = findViewById(R.id.update_profile_button);
        gender = findViewById(R.id.edit_profile_gender);
        branch = findViewById(R.id.edit_profile_branch);
        back = findViewById(R.id.edit_profile_back);
        appConfig = new AppConfig(this);

        getData();

        genderAdapter = ArrayAdapter.createFromResource(this,R.array.gender_array,R.layout.spinner_layout);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        branchAdapter = ArrayAdapter.createFromResource(this,R.array.course_array,R.layout.spinner_layout);
        branchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        branch.setAdapter(branchAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderString = gender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                branchString = branch.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , result -> {
                    image.setImageURI(result);

                    //storing Img in firebase storage
                    storage= FirebaseStorage.getInstance();

                    final StorageReference reference = storage.getReference().child("profile").child(appConfig.getUserID());
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess (UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener <Uri>() {
                                @Override
                                public void onSuccess (Uri uri) {
                                    // store uri in mongo db
                                    imageUrl=uri.toString();

                                }
                            });
                        }
                    });
                });

        image.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });
        update.setOnClickListener(v -> {

        });
    }

    private void getData() {
        Call<User> call = APIClient.getInstance()
                .getApiInterface().getUser(appConfig.getUserID());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    name.setText(response.body().getName());
                    phone.setText(response.body().getPhone());
                    dob.setText(response.body().getDob());
                    if(!Objects.equals(response.body().getImageUrl(), "")){
                        Picasso.get().load(response.body().getImageUrl()).into(image);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}