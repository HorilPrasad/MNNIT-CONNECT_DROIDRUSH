package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;

import java.util.Calendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfile extends AppCompatActivity {
    private AppConfig appConfig;
    private CircleImageView userImage;
    private EditText phone,dob;
    private Spinner gender,branch;
    private Button createProfile;
    private ArrayAdapter<CharSequence> genderAdapter,branchAdapter;
    private String phoneString,genderString,branchString,dobString,imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        appConfig = new AppConfig(this);
        userImage = findViewById(R.id.create_profile_image);
        phone = findViewById(R.id.create_profile_phone);
        dob = findViewById(R.id.create_profile_dob);
        gender = findViewById(R.id.create_profile_gender);
        branch = findViewById(R.id.create_profile_branch);
        createProfile = findViewById(R.id.create_profile_button);

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

        createProfile.setOnClickListener(v -> {
            phoneString = phone.getText().toString().trim();
            createProfile();
        });

    }

    private void createProfile() {
        if(check(phoneString,genderString,dobString,branchString)){
            Toast.makeText(this, appConfig.isUserLogin()+"", Toast.LENGTH_SHORT).show();
            User user = new User(appConfig.getUserEmail(),genderString,dobString,phoneString,branchString,imageUrl);
            Call<ApiResponse> call = APIClient.getInstance().getApiInterface()
                    .createProfile(user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(response.isSuccessful()){
                        appConfig.setProfileCreated(true);
                        startActivity(new Intent(CreateProfile.this,MainActivity.class));
                        finish();
                        Toast.makeText(CreateProfile.this, "Profile created successfully...", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CreateProfile.this, "Problem in creating profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(CreateProfile.this, "Server error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean check(String phoneString, String genderString, String dobString, String branchString) {

        return true;
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
}