package com.lisners.counsellor.Activity.Auth;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.autofill.UserData;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.fcm.FcmService;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.zWork.service.CallingService;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    int SPLASH_DISPLAY_LENGTH = 2000;
    StoreData storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (GlobalData.mediaPlayer != null)
            GlobalData.mediaPlayer.stop();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            NotificationManagerCompat.from(getApplicationContext()).cancelAll();
            // only for gingerbread and newer versions
            stopService(new Intent(this, CallingService.class));

        }
        else {
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(it);
            stopService(new Intent(this, CallingService.class));
        }


        storeData = new StoreData(this);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToWelcome();
            }
        }, SPLASH_DISPLAY_LENGTH);


        System.out.println("fcm===>" + new FcmService().getToken());
    }

    private void checkNotification() {


        Intent homeActivityIntent = new Intent(SplashScreen.this, HomeActivity.class);

        try {
            Intent intent = getIntent();
            if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("calling_intent")) {
                Log.e("splash bId==>", intent.getStringExtra("calling_data"));
                homeActivityIntent.setAction(intent.getAction());
                homeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                homeActivityIntent.putExtra("calling_data", intent.getStringExtra("calling_data"));

            } else if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("deepLink_intent")) {
                homeActivityIntent.setAction(intent.getAction());
                homeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                homeActivityIntent.putExtra("data", intent.getStringExtra("data"));

            } else if (intent.getExtras() != null) {
                // receiving data from notification when app background
                Map<String, String> dataMap = new HashMap<>();
                for (String key : intent.getExtras().keySet()) {
                    try {
                        dataMap.put(key, intent.getStringExtra(key) + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String type = dataMap.get("type");
                String data = new Gson().toJson(dataMap);

                if (type.equalsIgnoreCase("calling")) {
                    Log.e("splash else bId==>", data);
                    homeActivityIntent.setAction("calling_intent");
                    homeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    homeActivityIntent.putExtra("calling_data", data);
                } else {
                    homeActivityIntent.setAction("deepLink_intent");
                    homeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    homeActivityIntent.putExtra("data", data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        startActivity(homeActivityIntent);
        finish();

       /* Map<String, String> params = new HashMap<>();
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                try {
                    params.put(key, intent.getStringExtra(key) + "");
                } catch (Exception e) {
                }
            }
        }

        String st_json = new Gson().toJson(params);
        Log.e("CALLING_ACTION_A", st_json);
        try {

            String noti_Status = intent.getStringExtra("FROM_FCM");

            if (params.containsKey("type") && params.get("type").equals("calling")) {
                Intent intent1 = new Intent(SplashScreen.this, HomeActivity.class);
                intent1.setAction("calling_intent");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.putExtra("CALLING_ACTION", st_json);
                startActivity(intent1);
                finish();
            } else {

                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        } catch (
                Exception e) {

            Intent i = new Intent(SplashScreen.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
*/
    }

    public void moveToWelcome() {
        storeData.getData(ConstantValues.USER_TOKEN, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                storeData.getData(ConstantValues.USER_DATA, new StoreData.GetListener() {
                    @Override
                    public void getOK(String val) {
                        if (val != null) {
                            try {
                                User user = new Gson().fromJson(val, User.class);
                                if (user.getIs_profile_complete() == 1) {
                                    checkNotification();
                                } else {
                                    startActivity(new Intent(SplashScreen.this, SignUpDetailsActivity.class));

                                }
                            } catch (Exception e) {
                                startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                            }
                        } else {
                            startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                        }
                        finish();

                    }

                    @Override
                    public void onFail() {
                        startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onFail() {
                Intent i = new Intent(SplashScreen.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}