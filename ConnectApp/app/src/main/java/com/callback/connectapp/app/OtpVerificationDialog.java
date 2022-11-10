package com.callback.connectapp.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.callback.connectapp.Activity.CreateProfile;
import com.callback.connectapp.R;
import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.VerifyResponse;
import com.callback.connectapp.retrofit.APIClient;
import com.chaos.view.PinView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationDialog {

    TextView otpVerify;
    PinView userOtp;
    String verificationCode;
    AppConfig appConfig;
    private Context context;

    public OtpVerificationDialog(Context context) {
        this.context = context;
        appConfig = new AppConfig(context);
    }

    public void createDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_verification_dialog);

        otpVerify = dialog.findViewById(R.id.verify_otp);
        userOtp = dialog.findViewById(R.id.user_otp);

        userOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificationCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpVerify.setOnClickListener(v -> {
            verifyOtp(verificationCode);
            dialog.dismiss();
        });
        dialog.show();

    }

    private void verifyOtp(String verificationCode) {
        Call<VerifyResponse> call = APIClient.getInstance()
                .getApiInterface().getUserVerificationCode(appConfig.getUserID());

        call.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                if (response.isSuccessful()){
                    VerifyResponse verifyResponse = response.body();

                    if (verifyResponse.getOtp().equals(verificationCode) && verifyResponse.getUserId().equals(appConfig.getUserID())){

                        updateUserVerifyStatus();

                    }else{
                        Toast.makeText(context, "Invalid OTP!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {

            }
        });
    }

    private void updateUserVerifyStatus() {
        User user = new User();
        user.setVerified(true);
        Call<ApiResponse> call = APIClient.getInstance()
                .getApiInterface().statusUpdate(appConfig.getUserID(),user);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    appConfig.setLoginStatus(true);
                    Toast.makeText(context, "OTP Verified", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, CreateProfile.class));
                    ((Activity)context).finishAffinity();
                }else{

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
