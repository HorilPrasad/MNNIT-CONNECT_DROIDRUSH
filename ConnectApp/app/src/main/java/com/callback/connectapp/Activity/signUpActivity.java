package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signUpActivity extends AppCompatActivity {
    private EditText userName,userEmail,userPassword,userReg;
    private AppCompatButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
        registerButton.setOnClickListener(view -> register());

    }

    private void register() {
        String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString();
        String regNo = userReg.getText().toString().trim();

        if(check(name,email,password,regNo))
        {
            User user = new User(name,email,regNo,password);
            Call<ApiResponse> call = APIClient.getInstance()
                    .getApiInterface().registerUser(user);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    ApiResponse apiResponse = response.body();
                    if(response.isSuccessful()){
                        Toast.makeText(signUpActivity.this,"Successfully registered......",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(signUpActivity.this, apiResponse.getMessage()+ "status : "+apiResponse.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(signUpActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean check(String name, String email, String password, String regNo) {
        if(name.isEmpty()){
            userName.setError("Name can't be empty!");
            userName.requestFocus();
            return false;
        }else if(email.isEmpty()){
            userEmail.setError("Email can't be empty!");
            userEmail.requestFocus();
            return false;
        }else if(password.isEmpty()){
            userPassword.setError("Password can't be empty!");
            userPassword.requestFocus();
            return false;
        }else if(password.length()<6){
            userPassword.setError("Password to short!");
            userPassword.requestFocus();
            return false;
        }else if(regNo.isEmpty()){
            userReg.setError("Registration number can't be empty!");
            userReg.requestFocus();
            return false;
        }else{
            return true;
        }
    }


    private void initialize() {
        userName = findViewById(R.id.register_user_name);
        userEmail = findViewById(R.id.register_user_email);
        userPassword = findViewById(R.id.register_user_password);
        userReg = findViewById(R.id.register_user_reg);
        registerButton = findViewById(R.id.register_button);
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));

    }

}