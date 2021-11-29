package com.pierre44.go4lunch.repository;

import com.pierre44.go4lunch.R;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pmeignen on 18/11/2021.
 */
public class PlaceService {

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static final String API_URL = "https://maps.googleapis.com/maps/api/place/";


    private static void initLogging() {
        logging.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
    }

    private static void initApiKey() {
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    // TODO: API KEY TO BE SECURE
                    .addQueryParameter("key", String.valueOf(R.string.google_maps_key))
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
    }


    public static <S> S createService(Class<S> serviceClass) {
        initApiKey();
        initLogging();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(serviceClass);
    }
}
