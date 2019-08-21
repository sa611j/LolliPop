package com.example.lollipop;

import android.app.Application;

import com.apollographql.apollo.ApolloClient;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApolloClient extends Application {
    private static final String BASE_URL = "https://api-alpha.lpp.pw/v1/graphql";
    private ApolloClient apolloClient;

    //TODO: put your token here man!!!
    private String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJsb2xsaXBvcDEyMzQiLCJpYXQiOjE1NjYxMzQ2MzUsImV4cCI6MTU2NzM0NDIzNX0.cYLiF3wXVn-dHUXKlk1ovtsBy5uw7TiIbxxlcIl--1E";

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("lollipop-auth", TOKEN); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(client)
                .build();
    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

}
