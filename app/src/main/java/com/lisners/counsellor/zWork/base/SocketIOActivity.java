package com.lisners.counsellor.zWork.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.zWork.utils.config.MainApplication;


import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SocketIOActivity extends AppCompatActivity {
    private Socket mSocket;

    {
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
                                    .addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTcyLjEwNS4zNS45MS9ibHVlcm9vbS9hcGkvdjEvdGhlcmFwaXN0L2xvZ2luIiwiaWF0IjoxNjQwNjMxNzE1LCJleHAiOjIzMjQwNjMxNzE1LCJuYmYiOjE2NDA2MzE3MTUsImp0aSI6IlN5eFdRd2hzQkFiNm1KRTciLCJzdWIiOjUsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.OVTWoW1yDycrPjGXv1mc8b1IXO2d9fvghUKumlhgpJw")
                                    .build();
                            return chain.proceed(request);

                        }
                    })
                    .build();

            final IO.Options options = new IO.Options();
            options.webSocketFactory = httpClient;
            options.callFactory = httpClient;

            mSocket = IO.socket("http://3.109.207.193:6002", options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        mSocket.on("message-receive", onNewMessage);

        mSocket.connect();
        ((TextView) findViewById(R.id.tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/

                    ((TextView) findViewById(R.id.tv)).setText("" + new Gson().toJson(args));


                    // add the message to view

                }
            });
        }
    };


    private void attemptSend() {

        String msg = "asif hello";
        mSocket.emit("send-message", msg);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("StatusUpdate", onNewMessage);
    }
}