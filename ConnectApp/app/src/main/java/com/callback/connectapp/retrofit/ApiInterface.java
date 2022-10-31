package com.callback.connectapp.retrofit;

import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("/user/register")
    Call<ApiResponse> registerUser(@Body User user);

    @POST("/user/login")
    Call<User> loginUser(@Body User user);

    @POST("/user/create")
    Call<ApiResponse> createProfile(@Body User user);

    @GET("/user/users/{id}")
    Call<User> getProfile(@Path("id") String id);
}
