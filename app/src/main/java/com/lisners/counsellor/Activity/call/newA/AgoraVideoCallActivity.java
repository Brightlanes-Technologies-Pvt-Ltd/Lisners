package com.lisners.counsellor.Activity.call.newA;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;

import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Activity.call.newA.callbacks.IEventListener;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ActivityAgoraVideoCallCounselorBinding;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.DialogUtil;
import com.lisners.counsellor.zWork.utils.TimeUtils;
import com.lisners.counsellor.zWork.utils.config.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class AgoraVideoCallActivity extends BaseCallActivity<ActivityAgoraVideoCallCounselorBinding> {
    private static final String LOG_TAG = AgoraVideoCallActivity.class.getSimpleName();

    BookedAppointment appointment;
    User therapist;
    User patient;
    AppointmentModel appointmentDetail;

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};


    DialogUtil dialogUtil;
    AlertDialog alertDialog;
    CountDownTimer timer;

    public static Intent makeIntent(Context context, String action) {
        Intent intent = new Intent(context, AgoraVideoCallActivity.class);
        intent.putExtra("CALLING_ACTION", action);
        return intent;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_agora_video_call_counselor;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogUtil = new DialogUtil(getContext());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initBasicUi(true, false, false, "");


        getBinding().btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClicked(view);
            }
        });

        getBinding().btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClicked(view);
            }
        });


        getBinding().btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClicked(view);
            }
        });


        permissionCheck();

    }

    private void iniUI(BookedAppointment appointment, String rtmToken) {

        this.appointment = appointment;
        therapist = appointment.getCounselor();
        patient = appointment.getUser();
        appointmentDetail = appointment.getAppointment_detail();


        getBinding().btnMute.setActivated(true);
        if (patient.getProfile_image() != null) {
            UtilsFunctions.SetLOGO(this, patient.getProfile_image(), getBinding().ivProfile);
            getBinding().ivImageChar.setVisibility(View.GONE);
        } else {
            getBinding().ivImageChar.setText(UtilsFunctions.getFistLastChar(patient.getName()));
            getBinding().ivImageChar.setVisibility(View.VISIBLE);

        }

        getBinding().tvProfileName.setText(UtilsFunctions.splitCamelCase(patient.getName()));

        initAgoraEngine(rtmToken);

    }

    void permissionCheck() {
        if (checkPermission(REQUESTED_PERMISSIONS[0]) && checkPermission(REQUESTED_PERMISSIONS[1])) {
            getAppointmentDetail();
        } else {
            askPermission(REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

    }

    private void getAppointmentDetail() {

        String jsonSting = getIntent().getStringExtra("CALLING_ACTION");
        if (jsonSting != null) {
            showProgressDialog();
            Map<String, String> call_data = new Gson().fromJson(jsonSting, Map.class);
            GetApiHandler apiHandler = new GetApiHandler(AgoraVideoCallActivity.this, URLs.SET_BOOK_APPOINTMENT + "/" + call_data.get("appointment"), new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    closeProgressDialog();
                    if (jsonObject.has("status")) {
                        BookedAppointment appointment = new Gson().fromJson(jsonObject.getString("data"), BookedAppointment.class);
                        iniUI(appointment, call_data.get("token"));

                    }

                }

                @Override
                public void onError() {
                    closeProgressDialog();

                }
            });
            apiHandler.execute();
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void initAgoraEngine(String rtmToken) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        rtcEngine().setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        rtcEngine().enableDualStreamMode(true);
        if (appointment.getCall_type().equals("video")) {
            getBinding().btnSwitchCamera.setVisibility(View.VISIBLE);
            getBinding().btnSpeaker.setVisibility(View.INVISIBLE);
            rtcEngine().enableVideo();
            rtcEngine().setVideoEncoderConfiguration(
                    new VideoEncoderConfiguration(
                            width,
                            height,
                            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                            VideoEncoderConfiguration.STANDARD_BITRATE,
                            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT)

            );

            getBinding().remotePreviewLayout.setVisibility(View.VISIBLE);
            getBinding().localPreviewLayout.setVisibility(View.VISIBLE);
        } else {
            getBinding().btnSwitchCamera.setVisibility(View.INVISIBLE);
            getBinding().btnSpeaker.setVisibility(View.VISIBLE);
            //rtcEngine().setEnableSpeakerphone(true);
            rtcEngine().disableVideo();
            rtcEngine().enableAudio();
            getBinding().remotePreviewLayout.setVisibility(View.INVISIBLE);
            getBinding().localPreviewLayout.setVisibility(View.GONE);
        }


        setupLocalPreview();
        joinRtcChannel(rtmToken, appointment.getChenal_code(), "", Integer.parseInt("0"));
    }

    private void setupLocalPreview() {
        SurfaceView surfaceView = setupVideo(Integer.parseInt("0"), true);
        surfaceView.setZOrderOnTop(true);
        getBinding().localPreviewLayout.addView(surfaceView);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            permissionCheck();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        registerEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        removeEventListener(this);
    }

    private void registerEventListener(IEventListener listener) {
        application().registerEventListener(listener);
    }

    private void removeEventListener(IEventListener listener) {
        application().removeEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //leaveChannel();
        removeEventListener(this);
    }


    public MainApplication application() {
        return (MainApplication) getApplication();
    }

    protected RtcEngine rtcEngine() {
        return application().rtcEngine();
    }


    protected void joinRtcChannel(String accessToken, String channel, String info, int uid) {
        /*String accessToken = getString(R.string.agora_access_token);
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "<#YOUR ACCESS TOKEN#>")) {
            accessToken = null;
        }*/
        rtcEngine().joinChannel(accessToken, channel, info, uid);
    }

    protected void leaveChannel() {
        rtcEngine().leaveChannel();
    }


    protected SurfaceView setupVideo(int uid, boolean local) {
        SurfaceView surfaceView = RtcEngine.
                CreateRendererView(getApplicationContext());
        if (local) {
            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceView,
                    VideoCanvas.RENDER_MODE_HIDDEN, uid));
        } else {
            rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceView,
                    VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }

        return surfaceView;
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserJoined(final int uid, int elapsed) {
        //if (uid != mPeerUid) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long statTime = Calendar.getInstance().getTimeInMillis();
                Log.e("endTime==>", appointmentDetail.getEnd_time());
                Log.e("stTime==>", appointmentDetail.getStart_time());
                startCounter(TimeUtils.getTimeDifferenceInMiliSecondsForCall(statTime, getENDTime_Plus_30M(appointment.getDate() + " " + appointmentDetail.getStart_time())));
                getBinding().tvTimer.setVisibility(View.VISIBLE);

                getBinding().loader.setVisibility(View.GONE);
                if (getBinding().remotePreviewLayout.getChildCount() == 0) {
                    SurfaceView surfaceView = setupVideo(uid, false);
                    getBinding().remotePreviewLayout.addView(surfaceView);
                }


            }
        });
    }


    private void startCounter(long miliSeconds) {
        timer = new CountDownTimer(miliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                getBinding().tvTimer.setText(UtilsFunctions.calculateTime(sec));
            }

            @Override
            public void onFinish() {
                /*Intent intent3 = new Intent();
                intent3.putExtra("book_id", appointment.getId());
                setResult(ConstantValues.RESULT_CALL, intent3);*/
                finishCall("The appointment is over.If the call is disconnected by mistake/network issues, the client may call you again.",true);
            }
        };

        if (timer != null)
            timer.start();
    }


    @Override
    public void onUserOffline(int uid, int reason) {
        //if (uid != mPeerUid) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              /*  Intent intent3 = new Intent();
                intent3.putExtra("book_id", appointment.getId());
                setResult(ConstantValues.RESULT_CALL, intent3);
                finishCall("The appointment is over.If the call is disconnected by mistake/network issues, the client may call you again.");
            */

                if (timer != null) {
                    timer.cancel();
                    timer.onFinish();
                }
            }
        });
    }


    public void onButtonClicked(View view) {
        switch (view.getId()) {
          /*  case R.id.btn_endcall:
                finishCall("Your appointment get Over or get disconnected.Thanks for taking");
                break;*/
            case R.id.btn_mute:
                rtcEngine().muteLocalAudioStream(getBinding().btnMute.isActivated());
                getBinding().btnMute.setActivated(!getBinding().btnMute.isActivated());
                break;
            case R.id.btn_switch_camera:
                rtcEngine().switchCamera();
                break;

            case R.id.btn_speaker:
                rtcEngine().setEnableSpeakerphone(!getBinding().btnSpeaker.isActivated());
                getBinding().btnSpeaker.setActivated(!getBinding().btnSpeaker.isActivated());
                break;
        }
    }


    public void finishCall(String msg,boolean isHomeRedirect) {
        leaveChannel();
        getBinding().getRoot().setVisibility(View.INVISIBLE);
        alertDialog = dialogUtil.createAppointmentCallOverDialog("", msg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isHomeRedirect)
                    startActivity(HomeActivity.makeCallOverIntent(getContext(),appointment.getId()));


                alertDialog.cancel();
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isHomeRedirect)
                    startActivity(HomeActivity.makeCallOverIntent(getContext(),appointment.getId()));
                alertDialog.cancel();
                finish();

            }
        });
        alertDialog.show();

    }

    @Override
    public void finish() {
        super.finish();
    }


    @Override
    public void onBackClicked() {

    }

    @Override
    public void onBackPressed() {

    }

    public static int getSystemStatusBarHeight(Context context) {
        int id = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        return id > 0 ? context.getResources().getDimensionPixelSize(id) : id;
    }

    public static long getTime(String dateSt) {
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            Date date = format.parse(dateSt);

            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getENDTime_Plus_30M(String dateSt) {
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            Date date = format.parse(dateSt);
            return date.getTime() + 30 * 60 * 1000;
        } catch (Exception e) {
            return 0;
        }
    }
}