package com.lisners.counsellor.Activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyReviewFragment;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Activity.call.CallAcceptDialog;
import com.lisners.counsellor.Activity.call.PlayAudio;
import com.lisners.counsellor.Activity.call.newA.AgoraVideoCallActivity;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ActivityRingBinding;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.base.BaseActivity;
import com.lisners.counsellor.zWork.base.SocketSingleton;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;
import com.lisners.counsellor.zWork.service.CallingService;
import com.lisners.counsellor.zWork.utils.DateUtil;
import com.lisners.counsellor.zWork.utils.aModel.CallData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RingActivity extends BaseActivity<ActivityRingBinding> {


    String callInfo;
    BookedAppointment call_bookedAppointment;
    boolean callActioned = false;

    public static Intent makeIntent(Context context, String callInfo) {
        Intent intent = new Intent(context, RingActivity.class);
        intent.setAction("calling_intent");
        intent.putExtra("calling_data", callInfo);
        return intent;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_ring;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        connectToSocket();

        initBasicUi(false, false, false, "");
        receiveNotification();
    }

    private void receiveNotification() {
        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("calling_intent")) {
            //handle calling
            callInfo = intent.getStringExtra("calling_data");
            setUI();
        } else {
            finish();
        }


    }

    private void setUI() {
        StoreData storeData = new StoreData(this);
        storeData.getData(ConstantValues.USER_SETTING_AGORA_APP_ID, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                if (!val.isEmpty()) {
                    Log.e("print==>", "" + val);
                    GlobalData.setting_agora_id_model = new Gson().fromJson(val, SettingPojo.class);
                }

            }

            @Override
            public void onFail() {


            }
        });


        getBinding().callReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectCall();
            }
        });


        getBinding().callAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptCall();
            }
        });


        if (callInfo != null) {
            showProgress();
            Map<String, String> appointment = new Gson().fromJson(callInfo, Map.class);
            GetApiHandler apiHandler = new GetApiHandler(this, URLs.SET_BOOK_APPOINTMENT + "/" + appointment.get("appointment"), new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    hideProgress();
                    if (jsonObject.has("status")) {
                        call_bookedAppointment = new Gson().fromJson(jsonObject.getString("data"), BookedAppointment.class);

                        User patient = call_bookedAppointment.getUser();
                        AppointmentModel appointmentModel = call_bookedAppointment.getAppointment_detail();
                        UtilsFunctions.SetLOGO(getContext(), patient.getProfile_image(), getBinding().ivProfile);
                        getBinding().tvProfileName.setText(patient.getName());

                        getBinding().tvDesTime.setText(DateUtil.dateFormatter(call_bookedAppointment.getDate(), "dd-MM-yyyy", "dd MMM yyyy"));
                        getBinding().tvDesTimeRange.setText(String.format("%s - %s", DateUtil.dateFormatter(appointmentModel.getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(appointmentModel.getEnd_time(), "HH:mm:ss", "hh:mm a")));


                       /* tv_des_time.setText(UtilsFunctions.dateFormat(GlobalData.call_bookedAppointment.getDate()));
                        tv_des_time_range.setText(UtilsFunctions.timeFormat(appointmentModel.getStart_time()) + " - " + UtilsFunctions.timeFormat(appointmentModel.getEnd_time()));
                  */
                        showMainLayout();
                    } else {
                        rejectCall();
                    }

                }

                @Override
                public void onError() {
                    hideProgress();
                    rejectCall();
                }
            });

            apiHandler.execute();

        }


    }

    private void rejectCall() {
        callActioned = true;
        stopCallRing();
        attemptSendCallRejectToSocket("" + call_bookedAppointment.getId(), "" + call_bookedAppointment.getCounselor_id(), "" + call_bookedAppointment.getUser_id());
        finish();
    }

    private void acceptCall() {
        callActioned = true;
        stopCallRing();
        startActivity(AgoraVideoCallActivity.makeIntent(getContext(), callInfo));
        finish();

    }


    private void stopCallRing() {
        Intent objIntent = new Intent(getContext(), PlayAudio.class);
        getContext().stopService(objIntent);
    }


    Socket mSocket;

    void connectToSocket() {
        SocketSingleton socketSingleton = SocketSingleton.getSync(RingActivity.this);
        mSocket = socketSingleton.getSocket();

        mSocket.on("counslar-call-cut", onCallCutSocketEvent);
        mSocket.on("counslar-call-reject", onCallRejectSocketEvent);
        mSocket.connect();
    }


    private Emitter.Listener onCallCutSocketEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/

                    //((TextView) findViewById(R.id.tv)).setText("" + new Gson().toJson(args));


                    // add the message to view

                }
            });
        }
    };

    private Emitter.Listener onCallRejectSocketEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/

                    //((TextView) findViewById(R.id.tv)).setText("" + new Gson().toJson(args));


                    // add the message to view


                    CallData callData = new Gson().fromJson((String) args[0], CallData.class);
                    if (callData.getBookAppointmentId().equals("" + call_bookedAppointment.getId()) && callData.getCounsellorId().equals("" + call_bookedAppointment.getCounselor_id()) && callData.getPatientId().equals("" + call_bookedAppointment.getUser_id())) {
                        callActioned = true;
                        stopCallRing();
                        finish();
                    }

                }
            });
        }
    };


    private void attemptSendCallRejectToSocket(String bookAppointmentId, String counsellorId, String patientId) {

        CallData callData = new CallData(bookAppointmentId, counsellorId, patientId);
        String msg = new Gson().toJson(callData);
        mSocket.emit("counslar-call-cut", msg);
    }

    @Override
    protected void onDestroy() {

        if (!callActioned && callInfo != null) {
            Intent serviceIntent = new Intent(getApplicationContext(), CallingService.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("call_data", callInfo);
            serviceIntent.putExtras(mBundle);
            ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
        }

        mSocket.disconnect();
        mSocket.off("counslar-call-cut", onCallCutSocketEvent);
        mSocket.off("counslar-call-reject", onCallRejectSocketEvent);

        super.onDestroy();


    }


}