package com.rakhmat.androidgithubuserssearch.Rest;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthenticationInterceptor implements Interceptor {
    private String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Request request = builder.build();

        long t1 = System.nanoTime();
        //Log.d("OkHttp", String.format("Sending request %s on %s%n%s",
        //        request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        //Log.d("OkHttp", String.format("Received response for %s in %.1fms%n%s",
        //        response.request().url(), (t2 - t1) / 1e6d, response.headers()));


        return response;
    }
}
