package com.zeeice.themovie.Utilities;

import com.zeeice.themovie.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Oriaje on 3/7/2017.
 */
public class ServiceGenerator {

    private static HttpLoggingInterceptor loggingInterceptor = new
            HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.
            Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", BuildConfig.API_KEY)
                            .build();
                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build());

    private static Retrofit retrofit = builder.build();

    public static <T> T createService(Class<T> serviceClass)
    {
        return retrofit.create(serviceClass);
    }
}
