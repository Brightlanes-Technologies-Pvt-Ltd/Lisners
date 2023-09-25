package com.lisners.counsellor.utils;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PostApiHandler {
    ANRequest.PostRequestBuilder androidNetworking;
    OnClickListener listener;
    StoreData storeData;

    public interface OnClickListener {
        void onResponse(JSONObject jsonObject) throws JSONException;

        void onError(ANError error);
    }

    public PostApiHandler(String URL, Map<String, String> map, OnClickListener listener) {
        this.listener = listener;

        androidNetworking = AndroidNetworking.post(URL).setTag("test").setPriority(Priority.MEDIUM);
        if (map != null) {
            for (String key : map.keySet()) {
                androidNetworking.addBodyParameter(key, map.get(key));
            }
        }
    }

    public PostApiHandler(Context context, String URL, Map<String, String> map, OnClickListener listener) {
        this.listener = listener;
        storeData = new StoreData(context);
        androidNetworking = AndroidNetworking.post(URL).setTag("test").setPriority(Priority.MEDIUM);
        Log.w("[API]", URL.substring(URL.lastIndexOf("/") + 1) + " " + new Gson().toJson(map));
        storeData.getData(ConstantValues.USER_TOKEN, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                androidNetworking.addHeaders("Authorization", "Bearer " + val);
                if (map != null) {
                    for (String key : map.keySet()) {
                        androidNetworking.addBodyParameter(key, map.get(key));
                    }
                }
            }

            @Override
            public void onFail() {
                if (map != null) {
                    for (String key : map.keySet()) {
                        androidNetworking.addBodyParameter(key, map.get(key));
                    }
                }
            }
        });
    }

    public void execute() {
        AndroidNetworking.enableLogging(); // simply enable logging
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY); // enabling logging with level'


        androidNetworking.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (listener != null)
                        listener.onResponse(response);
                } catch (JSONException e) {
                    e.fillInStackTrace();
                } catch (Exception e) {
                    e.fillInStackTrace();
                } finally {
                    Log.w("[RESPONSE]", new Gson().toJson(response));
                }
            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("ANError", new Gson().toJson(error.getErrorBody()));
                if (listener != null)
                    listener.onError(error);
            }
        });
    }


}
