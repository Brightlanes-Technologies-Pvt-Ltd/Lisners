package com.lisners.counsellor.Activity.call;

import static androidx.core.app.NotificationCompat.FLAG_INSISTENT;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.lisners.counsellor.R;

public class PlayCallRingtoneOld extends Service {
    private static final String LOGCAT = null;
    MediaPlayer objPlayer;

    public void onCreate() {
        super.onCreate();
        Log.d(LOGCAT, "Service Started!");
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setFlags(FLAG_INSISTENT)
                .build();
        objPlayer = MediaPlayer.create(this, R.raw.vip);
        objPlayer.setAudioAttributes(audioAttributes);

        objPlayer.setLooping(true);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        objPlayer.start();
        Log.d(LOGCAT, "Media Player started!");
        if (objPlayer.isLooping() != true) {
            Log.d(LOGCAT, "Problem in Playing Audio");
        }
        return Integer.parseInt("1");
    }

    public void onStop() {
        objPlayer.stop();
        objPlayer.release();
    }

    public void onPause() {
        objPlayer.stop();
        objPlayer.release();
    }

    public void onDestroy() {
        objPlayer.stop();
        objPlayer.release();
    }

    @Override
    public IBinder onBind(Intent objIndent) {
        return null;
    }
}
