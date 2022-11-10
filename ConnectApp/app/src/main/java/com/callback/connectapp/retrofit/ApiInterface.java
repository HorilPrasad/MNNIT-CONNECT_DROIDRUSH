package com.callback.connectapp.retrofit;

import com.callback.connectapp.model.ApiResponse;
import com.callback.connectapp.model.Comment;
import com.callback.connectapp.model.Community;
import com.callback.connectapp.model.User;
import com.callback.connectapp.model.PostData;
import com.callback.connectapp.model.VerifyResponse;

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
    Call <User> registerUser (@Body User user);

    @POST("/user/login")
    Call <User> loginUser (@Body User user);

    @GET("/user/verify/{id}")
    Call<VerifyResponse> getUserVerificationCode(@Path("id") String id);

    @PUT("/user/edit/{id}")
    Call <ApiResponse> editProfile (@Path("id") String id , @Body User user);

    @PUT("/user/status/{id}")
    Call <ApiResponse> statusUpdate (@Path("id") String id , @Body User user);


    @GET("/user/users/{id}")
    Call <User> getUser (@Path("id") String id);

//    @PATCH("user/users/{id}")
//    Call<ApiResponse> updateProfile(@Path("id") String id,@Body User user);


    //all post Methods
    @GET("/post/allpost")
    Call <List <PostData>> getAllPosts ();

    @GET("/post/{id}")
    Call <List <PostData>> getUserPost (@Path("id") String id);

    @GET("/post/{id}")
    Call <PostData> getPost (@Path("id") String id);

    @POST("/post/create")
    Call <ApiResponse> createPost (@Body PostData post);

    @PUT("/post/{id}")
    Call <ApiResponse> updatePost (@Path("id") String id , @Body PostData post);


    @PUT("/post/like/{id}")
    Call <ApiResponse> likePost (@Path("id") String id , @Body User user);

    @PUT("/post/{id}/dislike")
    Call <ApiResponse> dislikePost (@Path("id") String id , @Body User user);

    @PUT("post/{id}/comment")
    Call <ApiResponse> commentPost (@Path("id") String id , @Body Comment comment);

    @DELETE("post/{id}")
    Call <ApiResponse> deletePost (@Path("id") String id , @Body User user);

    @PUT("/post/saved/{id}")
    Call<ApiResponse> savePost(@Path("id") String id,@Body User user);


    //community methods

    @POST("/community/create")
    Call <ApiResponse> createCommunity (@Body Community community);

    @GET("/community/communities")
    Call <List <Community>> getAllCommunities ();

    @GET("/community/{id}")
    Call <Community> getCommunityById (@Path("id") String id);

    @PATCH("/community/addmember/{id}")
    Call <ApiResponse> addUserToCommunity (@Path("id") String id , @Body User user);

    //get community post
    @GET("/community/post/{id}")
    Call <List <PostData>> getCommunityPost (@Path("id") String id);

    @GET("/post/{id}")
    Call <List <Community>> getUserCommunities (@Path("id") String id , @Body User user);
}
