package com.callback.connectapp.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfile extends AppCompatActivity {

    ActivityResultLauncher <String> launcher;
    private static final int IMAGE_CODE = 1;
    private AppConfig appConfig;
    private CircleImageView userImage;
    private EditText phone, dob;
    FirebaseStorage storage;
    private Spinner gender, branch;
    private Button createProfile;
    private ArrayAdapter <CharSequence> genderAdapter, branchAdapter;
    private String phoneString, genderString, branchString, dobString, imageUrl;
    private ProgressDialog progressDialog;
    private NoInternetDialog noInternetDialog;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        appConfig = new AppConfig(this);
        userImage = findViewById(R.id.create_profile_image);
        phone = findViewById(R.id.create_profile_phone);
        dob = findViewById(R.id.create_profile_dob);
        gender = findViewById(R.id.create_profile_gender);
        branch = findViewById(R.id.create_profile_branch);
        createProfile = findViewById(R.id.create_profile_button);
        noInternetDialog = new NoInternetDialog(this);
        relativeLayout = findViewById(R.id.create_profile_layout);


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


        storage = FirebaseStorage.getInstance();

        userImage = findViewById(R.id.create_profile_image);


        userImage.setOnClickListener(view -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1 , 1)
                    .start(this);
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

        createProfile.setOnClickListener(v -> {
            phoneString = phone.getText().toString().trim();
            genderString = gender.getSelectedItem().toString();
            branchString = branch.getSelectedItem().toString();

            createProfile();
        });

    }

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
        progressDialog.setTitle("User Profile Image");
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        reference.putFile(selectedImage).addOnSuccessListener(task -> {
            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                userImage.setImageURI(selectedImage);
                progressDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(this , "failing to get url" , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this , "Internet issue" , Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }).addOnProgressListener(snapshot -> {
            double progress = (1.0 * 100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setProgress((int) progress);
        });
    }

    private void createProfile () {
        loading();
        progressDialog.setTitle("User Profile");
        progressDialog.setMessage("creating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        if (check(phoneString , dobString)) {
            progressDialog.show();
            if (imageUrl == null)
                imageUrl = "";

            User user = new User(appConfig.getUserEmail() , genderString , dobString , phoneString , branchString , imageUrl);
            Call <ApiResponse> call = APIClient.getInstance().getApiInterface()
                    .editProfile(appConfig.getUserID() , user);
            call.enqueue(new Callback <ApiResponse>() {
                @Override
                public void onResponse (Call <ApiResponse> call , Response <ApiResponse> response) {
                    if (response.isSuccessful()) {
                        appConfig.setProfileCreated(true);
                        startActivity(new Intent(CreateProfile.this , MainActivity.class));
                        finish();
                        Toast.makeText(CreateProfile.this , "Profile created successfully..." , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateProfile.this , "Problem in creating profile" , Toast.LENGTH_SHORT).show();
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
        }
    }

    private boolean check (String phoneString , String dobString) {
        if (phoneString.isEmpty()) {
            phone.setError("Phone number can't be empty!");
            phone.requestFocus();
            return false;
        }

        if (dobString.isEmpty()) {
            dob.setError("Date of birth can't be empty!");
            dob.requestFocus();
            return false;
        }
        return true;
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

    private void loading () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);

    }


}