package com.callback.connectapp.retrofit;

import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.postData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

import retrofit2.http.PATCH;

import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("/user/users/{id}")
    Call<User> getUser (@Path("id") String id);

    @PATCH("user/users/{id}")
    Call<ApiResponse> updateProfileImage(@Path("id") String id,@Body String imageUrl);


    //all post Methods
    @GET("/post/allpost")
    Call<List<postData>>getAllPosts();

    @GET("/post/{id}")
    Call<List<postData>>getUserPost(@Path("id")String id,@Body User user);

    @POST("/post/create")
    Call<ApiResponse>createPost(@Body postData post);

    @PUT("/post/{id}")
    Call<ApiResponse>updatePost(@Path("id") String id,@Body postData post);

    @PUT("post/{id}/like")
    Call<ApiResponse>likePost(@Path("id") String id,@Body String userId);

    @PUT("post/{id}/dislike")
    Call<ApiResponse>dislikePost(@Path("id") String id,@Body String userId);

    @PUT("post/{id}/comment")
    Call<ApiResponse>commentPost(@Path("id") String id,@Body String userId);

    @DELETE("post/{id}")
    Call<ApiResponse>deletePost(@Path("id") String id,@Body User user);


    //community methods

    @POST("/community/create")
    Call<ApiResponse> createCommunity(@Body Community community);


}
