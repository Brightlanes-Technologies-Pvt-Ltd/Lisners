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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.R;
import com.lisners.counsellor.zWork.base.SocketSingleton;
import com.lisners.counsellor.zWork.utils.aModel.CallData;

import java.util.Map;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CallRejectSocketService extends Service {

    Socket mSocket;
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

        Map<String, String> appointment = new Gson().fromJson(data.getString("call_data"), Map.class);


        SocketSingleton socketSingleton = SocketSingleton.getSync(getContext());
        mSocket = socketSingleton.getSocket();

        mSocket.on("counslar-call-cut", onCallCutSocketEvent);
        mSocket.connect();



        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("counslar-call-cut", onCallCutSocketEvent);

    }



    private Emitter.Listener onCallCutSocketEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

        }
    };


    private void attemptSendCallRejectToSocket(String bookAppointmentId, String counsellorId, String patientId) {

        CallData callData = new CallData(bookAppointmentId, counsellorId, patientId);
        String msg = new Gson().toJson(callData);
        mSocket.emit("counslar-call-cut", msg);
    }
}
