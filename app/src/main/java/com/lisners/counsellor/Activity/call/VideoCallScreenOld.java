package com.lisners.counsellor.Activity.call;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.MainActivity;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class VideoCallScreenOld extends AppCompatActivity {
    private RtcEngine mRtcEngine;
    Long sec   ;
    TextView tv_timer ;
    TextView tv_profile_name ;
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    BookedAppointment bookedAppointment ;

    // Handle SDK Events
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // set first remote user to the main bg video container
                    setupRemoteVideoStream(uid);
                }
            });
        }

        // remote user has left channel
        @Override
        public void onUserOffline(int uid, int reason) { // Tutorial Step 7
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        // remote user has toggled their video
        @Override
        public void onUserMuteVideo(final int uid, final boolean toggle) { // Tutorial Step 10
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoToggle(uid, toggle);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_screen);
        tv_timer = findViewById(R.id.tv_timer);
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initAgoraEngine();
        }

        findViewById(R.id.audioBtn).setVisibility(View.GONE); // set the audio button hidden
        findViewById(R.id.leaveBtn).setVisibility(View.GONE); // set the leave button hidden
        findViewById(R.id.videoBtn).setVisibility(View.GONE); // set the video button hidden
        Intent objIntent = new Intent(this, PlayAudio.class);
        stopService(objIntent);
    }



    private void initAgoraEngine() {
        tv_profile_name = findViewById(R.id.tv_profile_name);
        try {
            //mRtcEngine = RtcEngine.create(this, GlobalData.setting_model.getAgora_app_id(), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
        getPatient();
    }

    private void setupSession(String type) {

        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        if(type.equals("voice"))
            mRtcEngine.enableAudio();
        else{
            mRtcEngine.enableVideo();
            mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_180x180, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
        }

    }
    private void getPatient(){
        String jsonSting  = getIntent().getStringExtra("CALLING_ACTION");
        if(jsonSting!=null) {
            Map<String, String> appointment = new Gson().fromJson(jsonSting, Map.class);
            GetApiHandler apiHandler = new GetApiHandler(VideoCallScreenOld.this, URLs.SET_BOOK_APPOINTMENT + "/"+appointment.get("appointment"), new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if(jsonObject.has("status")){
                        bookedAppointment = new Gson().fromJson(jsonObject.getString("data"),BookedAppointment.class);
                        AppointmentModel appointmentModel = bookedAppointment.getAppointment_detail();
                        User therapist = bookedAppointment.getCounselor();
                        findViewById(R.id.pb_call_loader).setVisibility(View.GONE);
                        findViewById(R.id.joinBtn).setVisibility(View.GONE);

                        if(appointmentModel!=null)
                          startCounter(UtilsFunctions.getMillisecondsRAnge(appointmentModel.getStart_time(),appointmentModel.getEnd_time()) );

                        if(therapist!=null) {
                            UtilsFunctions.SetLOGO(VideoCallScreenOld.this, therapist.getProfile_image(), findViewById(R.id.iv_image));
                            tv_profile_name.setText(therapist.getName());

                        }
                        if(bookedAppointment.getCall_type().equals("voice")) {
                            audioCall();
                            setupSession("voice");
                            onjoinChannelClicked();
                        }
                        else {
                            findViewById(R.id.videoBtn).setVisibility(View.VISIBLE);
                            setupSession("video");
                            onjoinChannelClicked();
                            setupLocalVideoFeed();
                        }


                       }

                }

                @Override
                public void onError() {

                }
            });
            apiHandler.execute();
        }else{
            finish();
        }
    }

    private void audioCall(){
        FrameLayout container = findViewById(R.id.floating_video_container);
        container.setVisibility(View.GONE);
        findViewById(R.id.videoBtn).setVisibility(View.GONE);

    }


    public void startCounter(long CALL_LIMIT) {
        new CountDownTimer(CALL_LIMIT, 1000) {
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                tv_timer.setText(UtilsFunctions.calculateTime(sec));
            }

            public void onFinish() {
              //  onLeaveChannelClicked();
                tv_timer.setText("");
            }
        }.start();
    }









    public void onLeaveChannelClicked(View view) {
        onLeaveChannelClicked();
    }



    private void leaveChannel() {
        mRtcEngine.leaveChannel();

    }

    private void removeVideo(int containerID) {
        FrameLayout videoContainer = findViewById(containerID);
        videoContainer.removeAllViews();
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

        mRtcEngine.muteLocalAudioStream(btn.isSelected());
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

        mRtcEngine.muteLocalVideoStream(btn.isSelected());

        FrameLayout container = findViewById(R.id.floating_video_container);
        container.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
        SurfaceView videoSurface = (SurfaceView) container.getChildAt(0);
        videoSurface.setZOrderMediaOverlay(!btn.isSelected());
        videoSurface.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
    }
    // join the channel when user clicks UI button
    public void onjoinChannelClicked() {
        mRtcEngine.joinChannel(null,  bookedAppointment.getChenal_code(), "Extra Optional Data", 0); // if you do not specify the uid, Agora will assign one.
        findViewById(R.id.joinBtn).setVisibility(View.GONE); // set the join button hidden
        findViewById(R.id.audioBtn).setVisibility(View.VISIBLE); // set the audio button hidden
        findViewById(R.id.leaveBtn).setVisibility(View.VISIBLE); // set the leave button hidden
        // set the video button hidden
    }
    public void onLeaveChannelClicked( ) {
        leaveChannel();
        removeVideo(R.id.floating_video_container);
        removeVideo(R.id.bg_video_container);
        findViewById(R.id.joinBtn).setVisibility(View.VISIBLE); // set the join button visible
        findViewById(R.id.audioBtn).setVisibility(View.GONE); // set the audio button hidden
        findViewById(R.id.leaveBtn).setVisibility(View.GONE); // set the leave button hidden
        findViewById(R.id.videoBtn).setVisibility(View.GONE); // set the video button hidden
        finish();

    }
    private void onRemoteUserLeft() {
        removeVideo(R.id.bg_video_container);
        finish();
    }
    private void setupLocalVideoFeed() {
        // setup the container for the local user
        FrameLayout videoContainer = findViewById(R.id.floating_video_container);
        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoSurface.setZOrderMediaOverlay(true);
        videoContainer.addView(videoSurface);
        mRtcEngine.setupLocalVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, 0));

    }
    private void setupRemoteVideoStream(int uid) {
        // setup ui element for the remote stream
        FrameLayout videoContainer = findViewById(R.id.bg_video_container);
        // ignore any new streams that join the session
        if (videoContainer.getChildCount() >= 1) {
            return;
        }

        SurfaceView videoSurface = RtcEngine.CreateRendererView(getBaseContext());
        videoContainer.addView(videoSurface);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, uid));
        mRtcEngine.setRemoteSubscribeFallbackOption(Constants.STREAM_FALLBACK_OPTION_AUDIO_ONLY);

    }
    private void onRemoteUserVideoToggle(int uid, boolean toggle) {
        FrameLayout videoContainer = findViewById(R.id.bg_video_container);

        SurfaceView videoSurface = (SurfaceView) videoContainer.getChildAt(0);
        videoSurface.setVisibility(toggle ? View.GONE : View.VISIBLE);

        // add an icon to let the other user know remote video has been disabled
        if(toggle){
            ImageView noCamera = new ImageView(this);
            noCamera.setImageResource(R.drawable.video_disabled);
            videoContainer.addView(noCamera);
        } else {
            ImageView noCamera = (ImageView) videoContainer.getChildAt(1);
            if(noCamera != null) {
                videoContainer.removeView(noCamera);
            }
        }
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    REQUESTED_PERMISSIONS,
                    requestCode);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "Need permissions " + Manifest.permission.RECORD_AUDIO + "/" + Manifest.permission.CAMERA);
                    break;
                }

                initAgoraEngine();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}