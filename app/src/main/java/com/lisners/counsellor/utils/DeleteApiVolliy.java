package com.lisners.counsellor.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;


import java.util.HashMap;
import java.util.Map;

public class DeleteApiVolliy {

    Context context ;
    DeleteApiVolliy.ApiListener listener;
    String URL ;
    StoreData storeData ;

    public interface ApiListener {
        void onResponse(String response);
        void onError();
    }

    public DeleteApiVolliy(Context context , String URL , DeleteApiVolliy.ApiListener listener) {
        this.context =context ;
        this.URL =URL ;
        this.listener = listener ;
        storeData = new StoreData(context);
    }

    public void callApi(){
        AndroidNetworking.enableLogging(); // simply enable logging
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY); // enabling logging with level'

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(listener!=null)
                            listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(listener!=null)
                            listener.onError();
                     //   Toast.makeText(context, "" ,Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");

                storeData.getData(ConstantValues.USER_TOKEN, new StoreData.GetListener() {
                   @Override
                   public void getOK(String val) {
                       params.put("Authorization", "Bearer "+val);
                   }
                   @Override
                   public void onFail() {

                   }
               });
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
