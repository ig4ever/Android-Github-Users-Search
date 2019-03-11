package com.rakhmat.androidgithubuserssearch.Rest;

import com.rakhmat.androidgithubuserssearch.Model.GetUser;
import com.rakhmat.androidgithubuserssearch.Model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("search/users")
    Call<GetUser> getUsers(@Query("q") String user);
}
