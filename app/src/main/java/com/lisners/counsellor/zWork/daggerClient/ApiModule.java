package com.lisners.counsellor.zWork.daggerClient;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lisners.counsellor.BuildConfig;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.restApi.api.ApiS;
import com.lisners.counsellor.zWork.utils.config.MainApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Singleton
    Interceptor provideInterceptor() { // This is where the Interceptor object is constructed

        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {


                Request main_request = chain.request();
                HttpUrl originalHttpUrl = main_request.url();
                Request.Builder requestBuilder = main_request.newBuilder();


                StoreData storeData = new StoreData(MainApplication.getContext());
                String token = storeData.getToken();

                Request request = main_request
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(request);

            }
        };
    }

    /*
     * The method returns the Cache object
     * */
    @Singleton
    @Provides
    Cache provideHttpCache(Application application) {
        long cacheSize = 10 * 1024 * 1024; // 10 MB
        return new Cache(application.getCacheDir(), cacheSize);
    }


    /*
     * The method returns the Gson object
     * */
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return builder.setLenient().create();
    }


    /*
     * The method returns the Okhttp object
     * */
    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache, Interceptor interceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);




        httpClient.addInterceptor(interceptor)
                .cache(cache)
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(loggingInterceptor);
        }

        return httpClient.build();
    }


    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(URLs.ROOT_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

    }


    @Singleton
    @Provides
    public ApiS getApiS(Retrofit retroFit) {
        return retroFit.create(ApiS.class);
    }



}