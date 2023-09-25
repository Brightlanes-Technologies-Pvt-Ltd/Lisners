package com.lisners.counsellor.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.fcm.FcmService;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHeader, tvSignInSignUp, tvForgotPassword;
    private Button btn_sign_in;
    private EditText edit_phone, edit_password;
    StoreData storeData;
    FcmService fcmService = new FcmService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        tvHeader.setText("Login");
        fcmService = new FcmService();
        tvSignInSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btn_sign_in.setOnClickListener(this);
    }

    private void init() {
        storeData = new StoreData(this);
        tvHeader = findViewById(R.id.tvHeader);
        tvSignInSignUp = findViewById(R.id.tvSignInSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        edit_phone = findViewById(R.id.edit_login_mobile);
        edit_password = findViewById(R.id.edit_login_password);

    }

    private void onSignIn() {

        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String stPhone = edit_phone.getText().toString();
        String stPass = edit_password.getText().toString();

        if (stPhone.isEmpty())
            edit_phone.setError("");
        else if (stPass.isEmpty())
            edit_password.setError("");
        else {
            DProgressbar dProgressbar = new DProgressbar(this);
            dProgressbar.show();
            Map<String, String> prams = new HashMap<>();
            prams.put("mobile_no", stPhone);
            prams.put("password", stPass);
            prams.put("device_id", fcmService.getToken());
            prams.put("device_type", "android");

            PostApiHandler postApiHandler = new PostApiHandler(LoginActivity.this, URLs.SEND_LOGIN, prams, new PostApiHandler.OnClickListener() {
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
                                User user = null;
                                try {
                                    user = new Gson().fromJson(jsonObject.getString("data"), User.class);

                                    if (user.getIs_profile_complete() == 1) {

                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, SignUpDetailsActivity.class));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {
                        if (jsonObject.has("type") && !jsonObject.getString("type").isEmpty() && jsonObject.getString("type").equalsIgnoreCase("validation")) {
                            JSONObject jsonObjet = jsonObject.getJSONObject("errors");
                            if (jsonObjet.has("mobile_no")) {
                                edit_phone.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("mobile_no")));
                            } else if (jsonObjet.has("password")) {
                                edit_password.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("password")));
                            }

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                   /* else if(jsonObject.has("errors")){
                        JSONObject errObj =  jsonObject.getJSONObject("errors");
                        if(errObj.has("mobile_no"))
                            Toast.makeText(LoginActivity.this, UtilsFunctions.errorShow(errObj.getJSONArray("mobile_no")) , Toast.LENGTH_SHORT).show();
                    }
                    if (jsonObject.has("message"))
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
               */
                    }
                    dProgressbar.dismiss();
                }


                @Override
                public void onError(ANError error) {
                    Toast.makeText(LoginActivity.this,
                            "Server Error", Toast.LENGTH_SHORT).show();
                    dProgressbar.dismiss();
                }
            });
            postApiHandler.execute();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignInSignUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btn_sign_in:
                onSignIn();
                break;
        }
    }
}