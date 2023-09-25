package com.lisners.counsellor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;


public class StoreData {
    Context context;
    SharedPreferences ph;
    SharedPreferences.Editor editor;
    SetListener setListener ;
    GetListener getListener ;

    public interface SetListener {
        void setOK();
    }

    public interface GetListener {
        void getOK(String val);
        void onFail();
    }

    public StoreData(Context context) {
         ph = context.getSharedPreferences(ConstantValues.STORE_USER_TABLE, Context.MODE_PRIVATE);
         editor = ph.edit();
    }

    public void setData(String KEY ,String value, SetListener listener){
        this.setListener =listener ;
        editor.putString(KEY, value);
        editor.commit();
        setListener.setOK();

    }

    public void setMultiData(Map<String,String> map, SetListener listener){
        this.setListener =listener ;
        for(String key : map.keySet()) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
        setListener.setOK();

    }

    public void getData(String KEY,GetListener listener) {
        this.getListener =listener ;
        if(KEY!=null) {
            String val = ph.getString(KEY, "");
            Log.e("MM",val);
            if (val != null && !val.isEmpty())
                getListener.getOK(val);
            else
                getListener.onFail();
        }

    }



    public void clearData(String KEY)  {
        editor.remove(KEY).commit();
    }

    public String getToken() {
        return ph.getString(ConstantValues.USER_TOKEN, "");
    }

}
