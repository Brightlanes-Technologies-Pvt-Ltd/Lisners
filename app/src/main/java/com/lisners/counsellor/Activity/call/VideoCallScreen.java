package com.lisners.counsellor.Activity.call;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ActivityRecieveVideoCallBinding;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class VideoCallScreen extends BaseActivity<ActivityRecieveVideoCallBinding> {
    private static final String LOG_TAG = VideoCallScreen.class.getSimpleName();

    private RtcEngine rtcEngine;

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

    BookedAppointment appointment;
    User therapist;
    AppointmentModel appointmentDetail;


    CountDownTimer timer;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_recieve_video_call;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        initBasicUi(true, false, false, "");
        permissionCheck();


        Intent objIntent = new Intent(this, PlayAudio.class);
        stopService(objIntent);
    }

    private void initUi(BookedAppointment appointment) {
        this.appointment = appointment;
        therapist = appointment.getCounselor();
        appointmentDetail = appointment.getAppointment_detail();

        if (appointmentDetail != null)
            setTimer(UtilsFunctions.getMillisecondsRAnge(appointmentDetail.getStart_time(), appointmentDetail.getEnd_time()));

        if (therapist != null) {
            UtilsFunctions.SetLOGO(VideoCallScreen.this, therapist.getProfile_image(), findViewById(R.id.iv_image));
            getBinding().tvProfileName.setText(therapist.getName());

        }


        getBinding().audioBtn.setVisibility(View.GONE); // set the audio button hidden
        getBinding().leaveBtn.setVisibility(View.GONE); // set the leave button hidden
        getBinding().videoBtn.setVisibility(View.GONE); // set the video button hidden


        setUpSession(appointment.getCall_type());


    }

    private void initAgoraEngine() {

        try {
            rtcEngine = RtcEngine.create(this,"" /*getString(R.string.agora_app_id)*/, mRtcEventHandler);
        } catch (Exception e) {
            Log.e(LOG_TAG, " " + Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        getAppointmentDetail();


    }


    private void setUpSession(String callType) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        if (callType.equals("video")) {
            rtcEngine.enableVideo();
            rtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(width, height, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));

        } else {
            rtcEngine.enableAudio(); // chnage call type
        }


        joinChannel();

    }


    void permissionCheck() {
        if (checkPermission(REQUESTED_PERMISSIONS[0]) && checkPermission(REQUESTED_PERMISSIONS[1])) {
            initAgoraEngine();
        } else {
            askPermission(REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            permissionCheck();
        }
    }

    public long getTime(String dateSt) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = format.parse(dateSt);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }


    void setTimer(long time) {

        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                getBinding().tvTimer.setText(UtilsFunctions.calculateTime(sec));
            }

            @Override
            public void onFinish() {

            }
        };
    }


    public void joinChannel() {
        rtcEngine.joinChannel(null, appointment.getChenal_code(), "Extra Optional Data", 0);
        if (appointment.getCall_type().contains("video")) {
            setupLocalVideoFeed();
            getBinding().videoBtn.setVisibility(View.VISIBLE);
            getBinding().floatingVideoContainer.setVisibility(View.VISIBLE);
        } else {
            getBinding().floatingVideoContainer.setVisibility(View.GONE);
            getBinding().videoBtn.setVisibility(View.GONE);
        }


        findViewById(R.id.audioBtn).setVisibility(View.VISIBLE); // set the audio button hidden
        findViewById(R.id.leaveBtn).setVisibility(View.VISIBLE); // set the leave button hidden
        // set the video button hidden
    }

    // call disconnect by user
    public void onLeaveChannelClicked(View view) {
        rtcEngine.leaveChannel();
        getBinding().floatingVideoContainer.removeAllViews();
        getBinding().bgVideoContainer.removeAllViews();
        findViewById(R.id.audioBtn).setVisibility(View.GONE); // set the audio button hidden
        findViewById(R.id.leaveBtn).setVisibility(View.GONE); // set the leave button hidden
        findViewById(R.id.videoBtn).setVisibility(View.GONE); // set the video button hidden

        onDestroy();
    }

    private void onRemoteUserLeft() {
        getBinding().bgVideoContainer.removeAllViews();
        onDestroy();
    }

    private void setupLocalVideoFeed() {

        // setup the container for the local user
        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoSurface.setZOrderMediaOverlay(true);
        getBinding().floatingVideoContainer.addView(videoSurface);
        rtcEngine.setupLocalVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void setupRemoteVideoStream(int uid) {

        // ignore any new streams that join the session
        if (getBinding().bgVideoContainer.getChildCount() >= 1) {
            return;
        }

        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        getBinding().bgVideoContainer.addView(videoSurface);
        rtcEngine.setupRemoteVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, uid));
        rtcEngine.setRemoteSubscribeFallbackOption(io.agora.rtc.Constants.STREAM_FALLBACK_OPTION_AUDIO_ONLY);

    }

    private void onRemoteUserVideoToggle(int uid, int toggle) {

        SurfaceView videoSurface = (SurfaceView) getBinding().bgVideoContainer.getChildAt(0);
        videoSurface.setVisibility(toggle == 0 ? View.GONE : View.VISIBLE);

        // add an icon to let the other user know remote video has been disabled
        if (toggle == 0) {
            ImageView noCamera = new ImageView(this);
            noCamera.setImageResource(R.drawable.video_disabled);
            getBinding().bgVideoContainer.addView(noCamera);
        } else {
            ImageView noCamera = (ImageView) getBinding().bgVideoContainer.getChildAt(1);
            if (noCamera != null) {
                getBinding().bgVideoContainer.removeView(noCamera);
            }
        }
    }

    // call disconnect by therapist


    // Handle SDK Events


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideoStream(uid);
                    startTimeCounter();
                }
            });
        }

        // remote user has left channel
        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        // remote user has toggled their video
        @Override
        public void onRemoteVideoStateChanged(final int uid, final int state, int reason, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoToggle(uid, state);
                }
            });
        }
    };

    private void startTimeCounter() {
        if (timer != null)
            timer.start();
    }


    public void onAudioMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(R.drawable.audio_toggle_btn);
        } else {
            btn.setSelected(true);
            btn.setImageResource(R.drawable.audio_toggle_active_btn);
        }

        rtcEngine.muteLocalAudioStream(btn.isSelected());
    }

    public void onVideoMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(R.drawable.video_toggle_btn);
        } else {
            btn.setSelected(true);
            btn.setImageResource(R.drawable.video_toggle_active_btn);
        }

        rtcEngine.muteLocalVideoStream(btn.isSelected());


        getBinding().floatingVideoContainer.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
        SurfaceView videoSurface = (SurfaceView) getBinding().floatingVideoContainer.getChildAt(0);
        videoSurface.setZOrderMediaOverlay(!btn.isSelected());
        videoSurface.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        rtcEngine.leaveChannel();
        RtcEngine.destroy();
        rtcEngine = null;


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onBackClicked() {

    }

    private void getAppointmentDetail() {
        String jsonSting = getIntent().getStringExtra("CALLING_ACTION");
        if (jsonSting != null) {
            Map<String, String> appointment = new Gson().fromJson(jsonSting, Map.class);
            GetApiHandler apiHandler = new GetApiHandler(VideoCallScreen.this, URLs.SET_BOOK_APPOINTMENT + "/" + appointment.get("appointment"), new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if (jsonObject.has("status")) {
                        BookedAppointment appointment = new Gson().fromJson(jsonObject.getString("data"), BookedAppointment.class);
                        getBinding().pbCallLoader.setVisibility(View.GONE);
                        initUi(appointment);


                    }

                }

                @Override
                public void onError() {

                }
            });
            apiHandler.execute();
        } else {
            finish();
        }
    }


}