package com.lisners.counsellor.Activity.call;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.SettingModel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CallAcceptDialog {
    final Dialog call_dialog;
    Context context;
    ImageView profilePic;
    TextView profile_name, tv_des_time, tv_des_time_range;
    Button btn_accept, btn_close;
    OnClickListener listener;
    MediaPlayer mediaPlayer;
    StoreData storeData;

    BookedAppointment bookedAppointmentData;

    public interface OnClickListener {
        void onAccept();
        void onReject(BookedAppointment bookedAppointmentData);

    }

    public CallAcceptDialog(Context context, OnClickListener listener) {
        call_dialog = new Dialog(context);
        this.context = context;
        this.listener = listener;
        storeData = new StoreData(context);
        call_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        call_dialog.setContentView(R.layout.call_accept_dailog);
        call_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        call_dialog.setCancelable(false);
        profilePic = call_dialog.findViewById(R.id.profile_photo);
        profile_name = call_dialog.findViewById(R.id.profile_name);
        tv_des_time = call_dialog.findViewById(R.id.tv_des_time);
        tv_des_time_range = call_dialog.findViewById(R.id.tv_des_time_range);
        btn_accept = call_dialog.findViewById(R.id.btn_accept);
        btn_close = call_dialog.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null)
                    mediaPlayer.stop();
                stopCallRing();
                onReject();
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSetting();
                stopCallRing();
                if (mediaPlayer != null)
                    mediaPlayer.stop();
            }
        });

    }


    public void checkAndShow(String jsonSting) {
        Log.e("Call dialog bId==>",jsonSting);
        if (jsonSting != null) {
            Map<String, String> appointment = new Gson().fromJson(jsonSting, Map.class);
            GetApiHandler apiHandler = new GetApiHandler(context, URLs.SET_BOOK_APPOINTMENT + "/" + appointment.get("appointment"), new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {

                    if (jsonObject.has("status")) {
                        GlobalData.call_bookedAppointment = new Gson().fromJson(jsonObject.getString("data"), BookedAppointment.class);

                        bookedAppointmentData = GlobalData.call_bookedAppointment;
                        User patient = GlobalData.call_bookedAppointment.getUser();
                        AppointmentModel appointmentModel = GlobalData.call_bookedAppointment.getAppointment_detail();
                        UtilsFunctions.SetLOGO(context, patient.getProfile_image(), profilePic);
                        profile_name.setText(patient.getName());

                        tv_des_time.setText(DateUtil.dateFormatter(GlobalData.call_bookedAppointment.getDate(), "dd-MM-yyyy", "dd MMM yyyy"));
                        tv_des_time_range.setText(String.format("%s - %s", DateUtil.dateFormatter(appointmentModel.getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(appointmentModel.getEnd_time(), "HH:mm:ss", "hh:mm a")));


                       /* tv_des_time.setText(UtilsFunctions.dateFormat(GlobalData.call_bookedAppointment.getDate()));
                        tv_des_time_range.setText(UtilsFunctions.timeFormat(appointmentModel.getStart_time()) + " - " + UtilsFunctions.timeFormat(appointmentModel.getEnd_time()));
                  */
                        show();
                    }

                }

                @Override
                public void onError() {

                }
            });

            apiHandler.execute();

        }
    }

    private void stopCallRing() {
        Intent objIntent = new Intent(context, PlayAudio.class);
        context.stopService(objIntent);
    }


    private void onAccept() {
        this.dismiss();
        if (listener != null)
            listener.onAccept();

    }

    private void onReject() {
        this.dismiss();
        if (listener != null)
            listener.onReject(bookedAppointmentData);
    }

    public void getSetting() {
        onAccept();

       /* Log.e("getSetting", "getSetting");
        storeData.getData(ConstantValues.USER_SETTING, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                Log.e("getSetting", val);
                if (val != null) {
                    GlobalData.setting_model = new Gson().fromJson(val, SettingModel.class);
                    onAccept();
                } else getSettingfromApi();
            }

            @Override
            public void onFail() {
                getSettingfromApi();

            }
        });*/
    }

    /*private void getSettingfromApi() {
        Log.e("getSettingfromApi", "getSettingfromApi");
        PostApiHandler apiHandler = new PostApiHandler(context, URLs.GET_SETTING, null, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    Log.e("getSettingfromApi", jsonObject.getString("data"));
                    GlobalData.setting_model = new Gson().fromJson(jsonObject.getString("data"), SettingModel.class);
                    storeData.setData(ConstantValues.USER_SETTING, jsonObject.getString("data"), new StoreData.SetListener() {
                        @Override
                        public void setOK() {
                            onAccept();
                        }
                    });
                } else
                    Log.e("getSettingfromApi", jsonObject.getString("status"));
            }

            @Override
            public void onError(ANError error) {

            }
        });
        apiHandler.execute();
    }*/

    public void show() {
        call_dialog.show();
    }

    public void dismiss() {
        call_dialog.dismiss();
    }

}
