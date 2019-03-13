package com.rakhmat.androidgithubuserssearch.Rest;

import com.rakhmat.androidgithubuserssearch.Model.AccessToken;
import com.rakhmat.androidgithubuserssearch.Model.GetUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);

    @GET("search/users")
    Call<GetUser> getUsers(@Query("q") String user, @Query("per_page") String perPage, @Query("page") String page);
}
