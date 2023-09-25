package com.lisners.counsellor.zWork.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;

import com.lisners.counsellor.Activity.Auth.SplashScreen;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Activity.activity.RingActivity;
import com.lisners.counsellor.Activity.call.newA.AgoraVideoCallActivity;

public class HeadsUpNotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            String action = intent.getStringExtra("action_key");
            Bundle data = intent.getBundleExtra("data");

            if (action != null) {
                try {
                    performClickAction(context, action, data);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // Close the notification after the click action is performed.
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();
                // only for gingerbread and newer versions
                context.stopService(new Intent(context, CallingService.class));

            }
            else {
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
                context.stopService(new Intent(context, CallingService.class));
            }

        }
    }

    private void performClickAction(Context context, String action, Bundle data) throws ClassNotFoundException {
        if (action.equals("callReceived") && data != null /*&& data.get("type").equals("voip")*/) {
            Intent openIntent = null;
            openIntent = AgoraVideoCallActivity.makeIntent(context, data.getString("call_data"));
            ;
            openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openIntent);
        } /*else if (action.equals("callReceived") && data != null *//*&& data.get("type").equals("video")*//*) {
            Intent openIntent = null;
            try {
                openIntent = new Intent(context, VideoCallActivity.class);
                        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(openIntent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }*/ else if (action.equals("callTap") && data != null /*&& data.get("type").equals("voip")*/) {
            context.startActivity(getCallNormalIntent(context, data.getString("call_data")));
        } else if (action.equals("callCancel")) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                NotificationManagerCompat.from(context.getApplicationContext()).cancelAll();
                // only for gingerbread and newer versions
                context.stopService(new Intent(context, CallingService.class));

            }
            else {
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
                context.stopService(new Intent(context, CallingService.class));
            }
        }
    }


    private Intent getCallNormalIntent(Context context, String callingData) {

        Intent notificationIntent = new Intent(context, RingActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (!callingData.isEmpty()) {
            notificationIntent.setAction("calling_intent");
            notificationIntent.putExtra("calling_data", callingData);
        }

        return notificationIntent;
    }
}
