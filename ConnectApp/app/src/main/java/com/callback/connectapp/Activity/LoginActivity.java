package com.callback.connectapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView userEmail,userPassword;
    private AppCompatButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        loginButton.setOnClickListener(v -> {
            getFirebaseToken();

        });

    }

    private void getFirebaseToken() {
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String firebaseMessagingToken = task.getResult();
                    userLogin(firebaseMessagingToken);
                }
            }
        });
    }

    private void userLogin(String token) {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString();

        if(check(email,password)){
            User user = new User(email,password,token);
            Call<User> call = APIClient.getInstance().getApiInterface()
                    .loginUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "login...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    private boolean check(String email, String password) {

        if(email.isEmpty()){
            userEmail.setError("Email can't be empty!");
            userEmail.requestFocus();
            return false;
        }else if(password.isEmpty()){
            userPassword.setError("Password can't be empty!");
            userPassword.requestFocus();
            return false;
        }else
            return true;
    }

    private void initialize() {
        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
    }

    public void onRegisterClick(View View){
        startActivity(new Intent(this,signUpActivity.class));
    }
}