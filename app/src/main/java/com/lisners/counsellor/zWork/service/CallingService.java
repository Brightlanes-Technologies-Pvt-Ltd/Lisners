package com.lisners.counsellor.zWork.service;

import static com.lisners.counsellor.zWork.utils.config.MainApplication.getContext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.SplashScreen;
import com.lisners.counsellor.Activity.activity.RingActivity;
import com.lisners.counsellor.R;

import java.util.Map;
import java.util.Objects;

public class CallingService extends Service {
    private String CHANNEL_ID = "LisnerCall";
    private String CHANNEL_NAME = "Lisner Call";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data = null;
        if (intent != null && intent.getExtras() != null) {
            data = intent.getExtras();
        }
        try {


            Intent notiOpenAction = new Intent(getContext(), HeadsUpNotificationActionReceiver.class);
            notiOpenAction.putExtra("action_key", "callTap");
            notiOpenAction.putExtra("data", data);
            notiOpenAction.setAction("OPEN_CALL_SCREEN");

          /*  Intent receiveCallAction = new Intent(getContext(), HeadsUpNotificationActionReceiver.class);
            receiveCallAction.putExtra("action_key", "callReceived");
            receiveCallAction.putExtra("data", data);
            receiveCallAction.setAction("RECEIVE_CALL");

            Intent cancelCallAction = new Intent(getContext(), HeadsUpNotificationActionReceiver.class);
            cancelCallAction.putExtra("action_key", "callCancel");
            cancelCallAction.putExtra("data", data);
            cancelCallAction.setAction("CANCEL_CALL");

            PendingIntent receiveCallPendingIntent = PendingIntent.getBroadcast(getContext(), 1200, receiveCallAction, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent cancelCallPendingIntent = PendingIntent.getBroadcast(getContext(), 1201, cancelCallAction, PendingIntent.FLAG_UPDATE_CURRENT);
*/
            PendingIntent contentPendingIntent = PendingIntent.getActivity(getContext(), 1200, notiOpenAction, PendingIntent.FLAG_UPDATE_CURRENT);

            createChannel();
            Map<String, String> appointment = new Gson().fromJson(data.getString("call_data"), Map.class);
            startForeground(120, makeNotification(appointment.get("title"),appointment.get("body"),getCallPendingIntent(data.getString("call_data"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    /*
    Create noticiation channel if OS version is greater than or eqaul to Oreo
    */
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Call Notifications");
            channel.setSound(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.vip),
                    new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setLegacyStreamType(AudioManager.STREAM_RING)
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION).build());
            Objects.requireNonNull(getContext().getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }


    Notification makeNotification(String title,String message,PendingIntent callPendingIntent) {


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id));

        notificationBuilder
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                /*.addAction(R.drawable.ic_stat_name, "Answer", receiveCallPendingIntent)
                .addAction(R.drawable.ic_stat_name, "Reject", cancelCallPendingIntent)*/
                .setAutoCancel(true)
                .setFullScreenIntent(callPendingIntent, true);

        return notificationBuilder.build();



    }

    private  PendingIntent getCallPendingIntent(String callingData) {

        Intent notificationIntent = new Intent(getApplicationContext(), RingActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (!callingData.isEmpty()) {
            notificationIntent.setAction("calling_intent");
            notificationIntent.putExtra("calling_data", callingData);
        }

        PendingIntent contentPendingIntent = PendingIntent.getActivity(getContext(),
                1200,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        return contentPendingIntent;
    }



}
