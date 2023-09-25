package com.lisners.counsellor.zWork.base;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.zWork.utils.config.MainApplication;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SocketSingleton {

    private static SocketSingleton instance;
    private static final String SERVER_ADDRESS = "http://3.109.207.193:6002";
    private Socket mSocket;
    private Context context;

    public static SocketSingleton get(Context context) {


        if (instance == null) {
            instance = getSync(context);
        }
        instance.context = context;
        return instance;
    }

    public static synchronized SocketSingleton getSync(Context context) {
        if (instance == null) {
            instance = new SocketSingleton(context);
        }
        return instance;
    }

    public Socket getSocket() {
        return this.mSocket;
    }

    private SocketSingleton(Context context) {
        this.context = context;
        this.mSocket = getServerSocket();

    }

    private Socket getServerSocket() {
        try {
            final OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
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
                    })
                    .build();

            final IO.Options options = new IO.Options();
            options.webSocketFactory = httpClient;
            options.callFactory = httpClient;

            mSocket = IO.socket(SERVER_ADDRESS, options);
            return mSocket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;

    }
}
