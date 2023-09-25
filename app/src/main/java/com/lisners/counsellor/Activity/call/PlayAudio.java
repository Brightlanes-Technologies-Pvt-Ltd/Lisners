package com.lisners.counsellor.Activity.call;

import static androidx.core.app.NotificationCompat.FLAG_INSISTENT;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.lisners.counsellor.R;

public class PlayAudio extends Service {
    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri ringtoneUri = Uri.parse(intent.getExtras().getString("ringtone-uri"));

        this.ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            this.ringtone.setLooping(true);
        }
        ringtone.play();

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        ringtone.stop();
    }
}
