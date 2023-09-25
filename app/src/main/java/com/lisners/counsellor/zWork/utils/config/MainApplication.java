package com.lisners.counsellor.zWork.utils.config;

import static androidx.core.app.NotificationCompat.FLAG_INSISTENT;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lisners.counsellor.Activity.call.newA.AgoraVideoCallActivity;
import com.lisners.counsellor.Activity.call.newA.callbacks.EngineEventListener;
import com.lisners.counsellor.Activity.call.newA.callbacks.IEventListener;
import com.lisners.counsellor.BuildConfig;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.zWork.daggerClient.ApiModule;
import com.lisners.counsellor.zWork.daggerClient.AppComponent;
import com.lisners.counsellor.zWork.daggerClient.AppModule;
import com.lisners.counsellor.zWork.daggerClient.DaggerAppComponent;
import com.lisners.counsellor.zWork.utils.customClasses.ReleaseTree;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;

import io.agora.rtc.RtcEngine;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;
import timber.log.Timber;


public class MainApplication extends Application {
    public static final String TAG = MainApplication.class.getSimpleName();

    private static MainApplication mInstance;

    private MerlinsBeard merlinsBeard;
    private Merlin merlin;
    private AppComponent mAppComponent;


    private RtcEngine mRtcEngine;
    private RtmClient mRtmClient;
    private RtmCallManager rtmCallManager;
    private EngineEventListener mEventListener;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

                if (activity.getLocalClassName() == AgoraVideoCallActivity.class.getSimpleName()) {
                    destroyEngine();
                    Log.e("===>", "onActivityDestroyed: ");
                }


            }
        });

       /* StoreData storeData = new StoreData(this);
        storeData.getData(ConstantValues.USER_SETTING_AGORA_APP_ID, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                initAgoraEngine(val);
            }

            @Override
            public void onFail() {

            }
        });*/
        mEventListener = new EngineEventListener();


        merlin = new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .build(this);
        merlinsBeard = MerlinsBeard.from(this);


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }


        //AppConfig.DEVICE_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), "android_id");
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).apiModule(new ApiModule()).build();

        //StrictMode.setVmPolicy(new Builder().build());
        mInstance = this;

        getNotificationManager();

    }


    private NotificationManager getNotificationManager() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel chan1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Default", NotificationManager.IMPORTANCE_HIGH);
            chan1.setLightColor(Color.GREEN);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(chan1);
        }


        //calling channel
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.vip); //Here is FILE_NAME is the name of file that you want to play
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library if

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "lisner call";
            String description = "testing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setFlags(FLAG_INSISTENT)
                    .build();
            NotificationChannel channel = new NotificationChannel("lisner_call", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(sound, audioAttributes);
            mNotificationManager.createNotificationChannel(channel);
        }

        return mNotificationManager;
    }


    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    public static MainApplication get(Application application) {
        return (MainApplication) application;
    }


    public AppComponent getAppComponent() {
        return this.mAppComponent;
    }


    public Merlin getMerlin() {
        return merlin;
    }

    public MerlinsBeard getMerlinsBeard() {
        return merlinsBeard;
    }


    private void initAgoraEngine(String appId) {



        if (TextUtils.isEmpty(appId)) {
            return;
            //throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }


        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), appId, mEventListener);
            mRtmClient = RtmClient.createInstance(getApplicationContext(), appId, mEventListener);
            rtmCallManager = mRtmClient.getRtmCallManager();
            rtmCallManager.setEventListener(mEventListener);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public RtcEngine rtcEngine() {
        if (mRtcEngine == null) {
            Log.e("initializing agora ","==>"+GlobalData.setting_agora_id_model.getValue());
            initAgoraEngine(GlobalData.setting_agora_id_model.getValue());
        }
        return mRtcEngine;
    }

    public RtmClient rtmClient() {
        return mRtmClient;
    }

    public void registerEventListener(IEventListener listener) {
        mEventListener.registerEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        mEventListener.removeEventListener(listener);
    }

    public RtmCallManager rtmCallManager() {
        return rtmCallManager;
    }

    @Override
    public void onTerminate() {
        destroyEngine();
        super.onTerminate();

    }

    private void destroyEngine() {
        rtcEngine().leaveChannel();
        RtcEngine.destroy();


    }


}
