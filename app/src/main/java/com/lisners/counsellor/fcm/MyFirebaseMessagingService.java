package com.lisners.counsellor.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.SplashScreen;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Activity.call.PlayAudio;
import com.lisners.counsellor.R;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "onMessageReceived");
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Intent myIntent = new Intent("FBR-IMAGE");
        Log.e("data", new Gson().toJson(remoteMessage));


        Map<String, String> data = remoteMessage.getData();
        if (data.containsKey("type") && data.get("type").equals("calling")) {

            myIntent.putExtra("CALLING_ACTION", new Gson().toJson(data));
            MyFirebaseMessagingService.this.sendBroadcast(myIntent);

            Intent objIntent = new Intent(MyFirebaseMessagingService.this, PlayAudio.class);
            startService(objIntent);

        }

        sendNotification(notification, data);
//		Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//		 MediaPlayer mediaPlayer= MediaPlayer.create(getApplicationContext(), notification1);
//		mediaPlayer.start();
    }


    private void sendNotification(RemoteMessage.Notification
                                          notification, Map<String, String> data) {
        Bundle bundle = new Bundle();
        bundle.putString(FCM_PARAM, data.get(FCM_PARAM));
        bundle.putString("FROM_FCM", "FROM_FCM");
        bundle.putString("NOTI_ACTION", "NOTI_ACTION");
        if (data != null) {
            for (String key : data.keySet()) {
                bundle.putString(key, data.get(key));
            }
        }

        PendingIntent pendingIntent;
        if (data.containsKey("type") && data.get("type").equals("calling")) {
            pendingIntent = getCallPendingIntent(new Gson().toJson(data));
        } else {
            Intent intent = new Intent(this, SplashScreen.class);
            intent.putExtras(bundle);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(getResources().getColor(R.color.transparent_primary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_stat_name);

        try {
            String picture = data.get(FCM_PARAM);
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.default_notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }


    private PendingIntent getCallPendingIntent(String callingData) {

        Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (!callingData.isEmpty()) {
            notificationIntent.setAction("calling_intent");
            notificationIntent.putExtra("CALLING_ACTION", callingData);
        }

        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,
                notificationIntent,
                0);


        return contentPendingIntent;
    }

}