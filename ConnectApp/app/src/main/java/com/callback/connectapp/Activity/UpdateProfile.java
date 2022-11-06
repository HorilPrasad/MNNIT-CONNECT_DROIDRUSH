package com.callback.connectapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {
    ActivityResultLauncher<String> launcher;
    private EditText name, phone, dob;
    private CircleImageView image;
    private Button update;
    private AppConfig appConfig;
    private Spinner gender, branch;
    private ImageButton back;
    private ArrayAdapter<CharSequence> genderAdapter, branchAdapter;
    private String phoneString, genderString, branchString, dobString, imageUrl = "";
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;

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

        genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.spinner_layout);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        branchAdapter = ArrayAdapter.createFromResource(this, R.array.course_array, R.layout.spinner_layout);
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
            loading();
                    //storing Img in firebase storage
                    storage = FirebaseStorage.getInstance();
                    final StorageReference reference = storage.getReference().child("profile").child(appConfig.getUserID());
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // store uri in mongo db
                                    imageUrl = uri.toString();
                                    image.setImageURI(result);
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateProfile.this, imageUrl, Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    });
                });

        dob.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog datePicker = new DatePickerDialog(this, datePickerListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.setCancelable(false);
            datePicker.setTitle("Select the date");
            datePicker.show();
        });

        image.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });
        update.setOnClickListener(v -> {


            phoneString = phone.getText().toString().trim();
            dobString = dob.getText().toString();
            genderString = gender.getSelectedItem().toString();
            branchString = branch.getSelectedItem().toString();
            String nameString = name.getText().toString();
            phoneString = phone.getText().toString();


            User user = new User(nameString, appConfig.getUserEmail(), genderString, dobString, phoneString, branchString, imageUrl);
            Call<ApiResponse> call = APIClient.getInstance()
                    .getApiInterface().editProfile(appConfig.getUserID(), user);


            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    if (response.isSuccessful()) {
                        startActivity(new Intent(UpdateProfile.this,MainActivity.class));
                        Toast.makeText(UpdateProfile.this, "profile update", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(UpdateProfile.this, "error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });

        });


    }

    private void loading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Image");
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

    }
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            selectedMonth=selectedMonth+1;
            String date = selectedDay+"-"+selectedMonth+"-"+selectedYear;

            dob.setText(date);
            dobString = date;
        }
    };

    private void getData() {
        Call<User> call = APIClient.getInstance()
                .getApiInterface().getUser(appConfig.getUserID());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    name.setText(response.body().getName());
                    phone.setText(response.body().getPhone());
                    dob.setText(response.body().getDob());
                    if (!Objects.equals(response.body().getImageUrl(), "")) {
                        Picasso.get().load(response.body().getImageUrl()).into(image);
                        imageUrl = response.body().getImageUrl();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}