package com.zeeice.themovie.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Oriaje on 3/7/2017.
 */
public class ServiceGenerator {

    private static HttpLoggingInterceptor loggingInterceptor = new
            HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.
            Builder().addInterceptor(loggingInterceptor);

    private static final String BASE_URL = "https://api.themoviedb.org/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build());

    private static Retrofit retrofit = builder.build();

    public static <T> T createService(Class<T> serviceClass)
    {
        return retrofit.create(serviceClass);
    }
}
