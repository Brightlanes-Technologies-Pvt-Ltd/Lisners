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

public class GetApiHandler {
    ANRequest.GetRequestBuilder androidNetworking;
    OnClickListener listener;
    StoreData storeData;

    public interface OnClickListener {
        void onResponse(JSONObject jsonObject) throws JSONException;

        void onError();
    }

    public GetApiHandler(String URL, OnClickListener listener) {
        this.listener = listener;

        androidNetworking = AndroidNetworking.get(URL).setTag("test").setPriority(Priority.MEDIUM);

    }

    public GetApiHandler(Context context, String URL, OnClickListener listener) {
        this.listener = listener;
        storeData = new StoreData(context);
        androidNetworking = AndroidNetworking.get(URL).setTag("test").setPriority(Priority.MEDIUM);
        storeData.getData(ConstantValues.USER_TOKEN, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                androidNetworking.addHeaders("Authorization", "Bearer " + val);
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void execute() {
        //AndroidNetworking.enableLogging(); // simply enable logging

        AndroidNetworking.enableLogging(); // simply enable logging
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY); // enabling logging with level
        androidNetworking.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (listener != null)
                        listener.onResponse(response);
                } catch (JSONException e) {
                    if (listener != null)
                        listener.onError();
                    e.fillInStackTrace();
                } catch (Exception e) {
                    if (listener != null)
                        listener.onError();
                    e.fillInStackTrace();
                }
            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("ANError", new Gson().toJson(error));
                if (listener != null)
                    listener.onError();
            }
        });
    }


}
