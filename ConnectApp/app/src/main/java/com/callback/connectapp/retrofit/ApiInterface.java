package com.callback.connectapp.retrofit;

import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.postData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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
    Call<User> getUser (@Path("id") String id);

    @PATCH("user/users/{id}")
    Call<ApiResponse> updateProfileImage(@Path("id") String id,@Body String imageUrl);

    @GET("/user/posts")
    Call<List <postData>>getAllPosts();

    @GET("/user/posts/{id}")
    Call<List<postData>>getUserPost(@Path("id")String id);




}
