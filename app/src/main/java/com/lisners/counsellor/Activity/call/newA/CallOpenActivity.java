package com.lisners.counsellor.Activity.call.newA;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lisners.counsellor.Activity.call.CallAcceptDialog;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ActivityCallOpenBinding;
import com.lisners.counsellor.zWork.base.BaseActivity;

public class CallOpenActivity extends BaseActivity<ActivityCallOpenBinding> {
    private static final String LOG_TAG = CallOpenActivity.class.getSimpleName();


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_call_open;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initBasicUi(true, false, false, "");

        Intent intent = getIntent();
        Log.e("print==>",""+intent.getAction());
        if (intent.getAction() != null) {
            String action = intent.getStringExtra("CALLING_ACTION");
            Log.e("print==>",""+action);
            if (action != null) {


                final CallAcceptDialog acceptDialog = new CallAcceptDialog(CallOpenActivity.this, new CallAcceptDialog.OnClickListener() {
                    @Override
                    public void onAccept() {
                        try {
                            /*Intent intent1 = new Intent(HomeActivity.this, VideoCallScreen.class);
                            intent1.putExtra("CALLING_ACTION", action);*/

                            Intent intent1 = AgoraVideoCallActivity.makeIntent(CallOpenActivity.this, action);
                            startActivity(intent1);
                            finish();

                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onReject(BookedAppointment bookedAppointmentData) {

                    }


                });
                acceptDialog.checkAndShow(action);

            }

        }
    }
}