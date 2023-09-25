package com.lisners.counsellor.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FcmService {
 String TAG ="FCM";
 String token ;
    public FcmService() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        token = task.getResult();
                        Log.w(TAG+"[TOKEN]",token);
                    }
                });
    }

    public String getToken() {
        if(token!=null) return token;
        else return "" ;
    }
}
