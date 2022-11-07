package com.callback.connectapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.app.NoInternetDialog;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signUpActivity extends AppCompatActivity {
    private EditText userName, userEmail, userPassword, userReg;
    private AppCompatButton registerButton;
    private AppConfig appConfig;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog progressDialog;

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

        if (check(name, email, password, regNo)) {
            loading();
            User user = new User(name, email, regNo, password);
            Call<User> call = APIClient.getInstance()
                    .getApiInterface().registerUser(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()) {
                        appConfig.setLoginStatus(true);
                        appConfig.setUserEmail(email);
                        appConfig.setUserID(response.body().get_id());
                        startActivity(new Intent(signUpActivity.this, CreateProfile.class));
                        Toast.makeText(signUpActivity.this, "Successfully registered......", Toast.LENGTH_LONG).show();

                    } else {
                        if (!noInternetDialog.isConnected())
                            noInternetDialog.create();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    if (!noInternetDialog.isConnected())
                        noInternetDialog.create();
                    progressDialog.dismiss();
                }
            });
        }

    }

    private boolean check(String name, String email, String password, String regNo) {
        if (name.isEmpty()) {
            userName.setError("Name can't be empty!");
            userName.requestFocus();
            return false;
        } else if (email.isEmpty()) {
            userEmail.setError("Email can't be empty!");
            userEmail.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            userPassword.setError("Password can't be empty!");
            userPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            userPassword.setError("Password to short!");
            userPassword.requestFocus();
            return false;
        } else if (regNo.isEmpty()) {
            userReg.setError("Registration number can't be empty!");
            userReg.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void loading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("User");
        progressDialog.setMessage("Creating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.show();

    }

    private void initialize() {
        userName = findViewById(R.id.register_user_name);
        userEmail = findViewById(R.id.register_user_email);
        userPassword = findViewById(R.id.register_user_password);
        userReg = findViewById(R.id.register_user_reg);
        registerButton = findViewById(R.id.register_button);
        appConfig = new AppConfig(this);
        noInternetDialog = new NoInternetDialog(this);
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));

    }

}