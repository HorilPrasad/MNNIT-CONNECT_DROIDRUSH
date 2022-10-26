package com.callback.connectapp.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "";
    private static APIClient mInstance;
    private Retrofit retrofit;

    private APIClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized APIClient getInstance(){
        if(mInstance == null){
            mInstance = new APIClient();
        }
        return mInstance;
    }

    public ApiInterface getApiInterface(){
        return retrofit.create(ApiInterface.class);
    }
}
