package com.callback.connectapp.retrofit;

import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/user/register")
    Call<ApiResponse> registerUser(@Body User user);

    @POST("/api/user/login")
    Call<User> loginUser(@Body User user);
}
