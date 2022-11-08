package com.callback.connectapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {
    ActivityResultLauncher <String> launcher;
    private EditText name, phone, dob;
    private CircleImageView image;
    private Button update;
    private AppConfig appConfig;
    private Spinner gender, branch;
    private ImageButton back;
    private ArrayAdapter <CharSequence> genderAdapter, branchAdapter;
    private String phoneString, genderString, branchString, dobString, imageUrl = "";
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;
    private NoInternetDialog noInternetDialog;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
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
        noInternetDialog = new NoInternetDialog(this);
        relativeLayout = findViewById(R.id.update_profile_layout);

        getData();

        genderAdapter = ArrayAdapter.createFromResource(this , R.array.gender_array , R.layout.spinner_layout);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        branchAdapter = ArrayAdapter.createFromResource(this , R.array.course_array , R.layout.spinner_layout);
        branchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        branch.setAdapter(branchAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView <?> adapterView , View view , int i , long l) {
                genderString = gender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected (AdapterView <?> adapterView) {

            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView <?> adapterView , View view , int i , long l) {
                branchString = branch.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected (AdapterView <?> adapterView) {

            }
        });


        dob.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog datePicker = new DatePickerDialog(this , datePickerListener ,
                    cal.get(Calendar.YEAR) ,
                    cal.get(Calendar.MONTH) ,
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.setCancelable(false);
            datePicker.setTitle("Select the date");
            datePicker.show();
        });

        image.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1 , 1)
                    .start(this);
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });
        update.setOnClickListener(v -> {
            loading();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("User Data");
            progressDialog.setMessage("Fetching...");
            progressDialog.show();
            phoneString = phone.getText().toString().trim();
            dobString = dob.getText().toString();
            genderString = gender.getSelectedItem().toString();
            branchString = branch.getSelectedItem().toString();
            String nameString = name.getText().toString();
            phoneString = phone.getText().toString();


            User user = new User(nameString , appConfig.getUserEmail() , genderString , dobString , phoneString , branchString , imageUrl);
            Call <ApiResponse> call = APIClient.getInstance()
                    .getApiInterface().editProfile(appConfig.getUserID() , user);


            call.enqueue(new Callback <ApiResponse>() {
                @Override
                public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {

                    if (response.isSuccessful()) {
                        startActivity(new Intent(UpdateProfile.this , MainActivity.class));
                        Toast.makeText(UpdateProfile.this , "profile update" , Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(UpdateProfile.this , "error" , Toast.LENGTH_SHORT).show();

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure (Call <ApiResponse> call , Throwable t) {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                    progressDialog.dismiss();
                }
            });

        });


    }

    private void loading () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);

    }

    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet (DatePicker view , int selectedYear ,
                               int selectedMonth , int selectedDay) {
            selectedMonth = selectedMonth + 1;
            String date = selectedDay + "-" + selectedMonth + "-" + selectedYear;

            dob.setText(date);
            dobString = date;
        }
    };

    @Override
    protected void onActivityResult (int requestCode , int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = result.getUri();
                uploadImageToFirebase(selectedImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImageToFirebase (Uri selectedImage) {
        storage = FirebaseStorage.getInstance();
        final StorageReference reference = storage.getReference().child("profile").child(appConfig.getUserID());
        loading();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("User Image");
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        reference.putFile(selectedImage).addOnSuccessListener(task -> {
            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                image.setImageURI(selectedImage);
                progressDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(this , "failing to get url" , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }).addOnFailureListener(e -> {
            if (!noInternetDialog.isConnected())
                noInternetDialog.create();
            progressDialog.dismiss();
        }).addOnProgressListener(snapshot -> {
            double progress = (1.0 * 100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setProgress((int) progress);
        });

    }

    private void getData () {
        loading();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("User Data");
        progressDialog.setMessage("Fetching...");
        progressDialog.show();
        Call <User> call = APIClient.getInstance()
                .getApiInterface().getUser(appConfig.getUserID());

        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if (response.isSuccessful()) {
                    name.setText(response.body().getName());
                    phone.setText(response.body().getPhone());
                    dob.setText(response.body().getDob());
                    if (!Objects.equals(response.body().getImageUrl() , "")) {
                        Picasso.get().load(response.body().getImageUrl()).placeholder(R.drawable.avatar).into(image);
                        imageUrl = response.body().getImageUrl();
                    }
                    relativeLayout.setVisibility(View.VISIBLE);
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


}