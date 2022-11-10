package com.callback.connectapp.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.callback.connectapp.R;

public class SplashScreen extends AppCompatActivity {

    private final static int SPLASH_SCREEN_TIME_OUT = 1700;
    private static final int STORAGE_PERMISSION_CODE = 101;
    ImageView logo;
    ConstraintLayout constraintLayout;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        logo = findViewById(R.id.imageView4);
        lottieAnimationView = findViewById(R.id.animation_view);
        constraintLayout = findViewById(R.id.splash_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {


            @Override

            public void run () {
                lottieAnimationView.setVisibility(View.INVISIBLE);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.splash_color));
                logo.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run () {

                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE , STORAGE_PERMISSION_CODE);
                        Intent intent = new Intent(SplashScreen.this , MainActivity.class);

                        startActivity(intent);

                        // close this activity

                        finishAffinity();
                    }
                } , 1000);

            }

        } , SPLASH_SCREEN_TIME_OUT);
    }

    // Function to check and request permission.
    public void checkPermission (String permission , int requestCode) {
        if (ContextCompat.checkSelfPermission(SplashScreen.this , permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(SplashScreen.this , new String[]{permission} , requestCode);
        } else {
//            Toast.makeText(SplashScreen.this , "Permission already granted" , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult (int requestCode ,
                                            @NonNull String[] permissions ,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode ,
                permissions ,
                grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashScreen.this , "Storage Permission Granted" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SplashScreen.this , "Storage Permission Denied" , Toast.LENGTH_SHORT).show();
            }
        }
    }
}