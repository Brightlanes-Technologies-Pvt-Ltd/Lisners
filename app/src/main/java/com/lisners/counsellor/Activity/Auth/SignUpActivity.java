package com.lisners.counsellor.Activity.Auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lisners.counsellor.R;
import com.lisners.counsellor.fcm.FcmService;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OtpTextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHeader, tvSignUpSignIn;
    private Button btn_signUp, btn_otp_submit;
    private AlertDialog alert;
    private EditText edit_signUp_mobile;
    private TextView txt_timer, txt_resend_otp;
    private OtpTextView otpTextView;
    private LinearLayout lv_resend_otp;
    FcmService fcmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        fcmService = new FcmService();
        tvHeader.setText("Create an Account");
        tvSignUpSignIn.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvSignUpSignIn = findViewById(R.id.tvSignUpSignIn);
        btn_signUp = findViewById(R.id.btn_signUp);
        edit_signUp_mobile = findViewById(R.id.edit_signUp_mobile);
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_layout, null);
        AlertDialog.Builder rateDialog = new AlertDialog.Builder(SignUpActivity.this, R.style.DialogAnimation);
        rateDialog.setView(view);
        alert = rateDialog.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initDialog(view);
        alert.show();
        startCounter();
    }

    private void initDialog(View view) {
        Button submit = view.findViewById(R.id.btn_otp_submit);
        txt_timer = view.findViewById(R.id.txt_timer);
        txt_resend_otp = view.findViewById(R.id.tvSheetResendCode);
        otpTextView = view.findViewById(R.id.otpTextView);
        lv_resend_otp = view.findViewById(R.id.lv_resend_otp);
        btn_otp_submit = view.findViewById(R.id.btn_otp_submit);

        txt_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });

        btn_otp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });

    }

    private void verifyOTP() {
        StoreData storeData = new StoreData(this);
        String otp = otpTextView.getOTP();
        String phone = edit_signUp_mobile.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        if (otp.trim().length() >= 4) {
            final DProgressbar dProgressbar = new DProgressbar(this);
            dProgressbar.show();
            params.put("mobile_no", phone);
            params.put("otp", otp);
            params.put("device_id", fcmService.getToken());
            params.put("device_type", "android");

            PostApiHandler senOtp = new PostApiHandler(URLs.SEND_VERIFY_SIGN_UP, params, new PostApiHandler.OnClickListener() {

                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    Log.e("JSONObject", new Gson().toJson(jsonObject));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        Map<String, String> params = new HashMap<>();
                        params.put(ConstantValues.USER_TOKEN, jsonObject.getString("token"));
                        params.put(ConstantValues.USER_DATA, jsonObject.getString("data"));
                        storeData.setMultiData(params, new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                startActivity(new Intent(SignUpActivity.this, SignUpDetailsActivity.class));
                            }
                        });

//                        storeData.setData(ConstantValues.USER_TOKEN, jsonObject.getString("token"), new StoreData.SetListener() {
//                            @Override
//                            public void setOK() {
//                                startActivity(new Intent(SignUpActivity.this, SignUpDetailsActivity.class));
//                                alert.dismiss();
//                            }
//                        });
                    } else {

                        Toast.makeText(SignUpActivity.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                    }
                    dProgressbar.dismiss();
                }

                @Override
                public void onError(ANError error) {
                    Toast.makeText(SignUpActivity.this,
                            "Server Error", Toast.LENGTH_SHORT).show();
                    dProgressbar.dismiss();
                }
            });
            senOtp.execute();
        } else
            Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();
    }

    private void sendOTP() {
        String phone = edit_signUp_mobile.getText().toString();
        Map<String, String> params = new HashMap<>();

        if (!phone.trim().isEmpty()) {
            final DProgressbar dProgressbar = new DProgressbar(this);
            dProgressbar.show();
            params.put("mobile_no", phone);
            PostApiHandler senOtp = new PostApiHandler(URLs.SEND_OTP_SIGN_UP, params, new PostApiHandler.OnClickListener() {

                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    dProgressbar.dismiss();
                    if (jsonObject.has("status") && jsonObject.getBoolean("status"))
                        showBottomSheetDialog();
                    else {
                        if (jsonObject.has("type") && !jsonObject.getString("type").isEmpty() && jsonObject.getString("type").equalsIgnoreCase("validation")) {
                            JSONObject jsonObjet = jsonObject.getJSONObject("errors");
                            if (jsonObjet.has("mobile_no")) {
                                edit_signUp_mobile.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("mobile_no")));
                            }

                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(ANError error) {
                    dProgressbar.dismiss();
                    Toast.makeText(SignUpActivity.this,
                            "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
            senOtp.execute();
        } else
            edit_signUp_mobile.setError("Enter Mobile Number");
    }

    public void startCounter() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                Long sec = millisUntilFinished / 1000;
                String str = sec < 10 ? "0:0" + sec : "0:" + sec;
                if (sec > 0)
                    txt_timer.setText(str);
                else
                    txt_timer.setText("");
                lv_resend_otp.setVisibility(View.GONE);
            }

            public void onFinish() {
                lv_resend_otp.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignUpSignIn:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;

            case R.id.btn_signUp:
                sendOTP();
                break;
        }
    }


}