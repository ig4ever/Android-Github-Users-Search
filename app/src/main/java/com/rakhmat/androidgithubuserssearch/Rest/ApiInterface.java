package com.rakhmat.androidgithubuserssearch.Rest;

import com.rakhmat.androidgithubuserssearch.Model.GetUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("users/{user}")
    Call<GetUser> getUser(@Path("user") String user);

}
