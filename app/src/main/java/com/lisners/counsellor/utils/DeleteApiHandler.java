package com.lisners.counsellor.utils;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteApiHandler {
    ANRequest.DeleteRequestBuilder androidNetworking;
    OnClickListener listener ;
    StoreData storeData ;
    public interface  OnClickListener {
        void onResponse(JSONObject jsonObject) throws JSONException;
        void onError();
    }

    public DeleteApiHandler(String URL, OnClickListener listener) {
        this.listener =listener ;
        androidNetworking = (ANRequest.DeleteRequestBuilder) AndroidNetworking.delete(URL).setTag("test").setPriority(Priority.MEDIUM);

    }

    public DeleteApiHandler(Context context , String URL, OnClickListener listener) {
        this.listener = listener;
            storeData = new StoreData(context);
            androidNetworking = (ANRequest.DeleteRequestBuilder)AndroidNetworking.delete(URL).setTag("test").setPriority(Priority.MEDIUM);
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
        androidNetworking.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                 try {
                     if (listener != null)
                         listener.onResponse(response);
                 }catch (JSONException e){
                     e.fillInStackTrace();
                 }
                 catch (Exception e){
                     e.fillInStackTrace();
                 }
            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("ANError",new Gson().toJson(error));
                if(listener!=null)
                    listener.onError();
            }
        });
    }




}
