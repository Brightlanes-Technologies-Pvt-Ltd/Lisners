package com.lisners.counsellor.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.lisners.counsellor.R;
import com.lisners.counsellor.fcm.FcmService;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHeader;
    ImageButton btn_header_left;
    EditText edit_pass, edit_retry_pass, editCOldPass;
    TextInputLayout ti_old_pass, ti_newPass, ti_cnfPass;
    Button btn_submit;
    LinearLayout lv_old_pass;
    String from_to;
    FcmService fcmService = new FcmService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        from_to = getIntent().getStringExtra("FROM");
        fcmService = new FcmService();
        init();

    }

    public void init() {

        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText("Change Password");
        btn_header_left = findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        edit_pass = findViewById(R.id.editCPassPassword);
        edit_retry_pass = findViewById(R.id.editCPassCnfPassword);
        editCOldPass = findViewById(R.id.editCOldPass);
        btn_submit = findViewById(R.id.btnCPassSubmit);
        lv_old_pass = findViewById(R.id.lv_old_pass);
        ti_old_pass = findViewById(R.id.ti_old_pass);
        ti_newPass = findViewById(R.id.ti_newPass);
        ti_cnfPass = findViewById(R.id.ti_cnfPass);

        btn_submit.setOnClickListener(this);

        if (from_to != null)
            lv_old_pass.setVisibility(View.VISIBLE);

        btn_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onChangePass() {

        String st_pass = edit_pass.getText().toString();
        String st_retry_pass = edit_retry_pass.getText().toString();
        String st_old = editCOldPass.getText().toString();
        if (st_old.isEmpty() && from_to != null)
            ti_old_pass.setError("Enter old Password");
        else if (st_pass.isEmpty())
            ti_newPass.setError("Enter new Password");
        else if (st_pass.length() < 8)
            ti_cnfPass.setError("Your password must be more than 8 characters");
        else if (st_retry_pass.isEmpty())
            ti_cnfPass.setError("Enter Confirm Password");
        else if (!st_pass.equals(st_retry_pass))
            ti_cnfPass.setError("Confirm password is not the same as new password");

        else {

            ti_cnfPass.setError(null);
            ti_newPass.setError(null);
            ti_old_pass.setError(null);
            DProgressbar dProgressbar = new DProgressbar(this);
            dProgressbar.show();
            Map<String, String> params = new HashMap<>();
            if (from_to != null)
                params.put("old_password", st_old);
            params.put("password", st_pass);
            params.put("confirm_password", st_retry_pass);
            Log.e("params", new Gson().toJson(params));
            PostApiHandler postApiHandler = new PostApiHandler(this, from_to != null ? URLs.UPDATE_PASSWORD : URLs.NEW_PASSWORD, params, new PostApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        if (from_to != null) {
                            finish();
                        } else {
                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        if (jsonObject.has("message"))
                            Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObject.has("type") && !jsonObject.getString("type").isEmpty() && jsonObject.getString("type").equalsIgnoreCase("validation")) {
                        JSONObject jsonObjet = jsonObject.getJSONObject("errors");
                        if (jsonObjet.has("old_password")) {
                            ti_old_pass.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("old_password")));
                            ti_old_pass.requestFocus();
                        }
                        if (jsonObjet.has("password")) {
                            ti_newPass.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("password")));
                            ti_newPass.requestFocus();
                        }
                        if (jsonObjet.has("confirm_password")) {
                            ti_cnfPass.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("confirm_password")));
                            ti_cnfPass.requestFocus();
                        }


                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    dProgressbar.dismiss();
                }

                @Override
                public void onError(ANError anError) {
                    Toast.makeText(ChangePasswordActivity.this,
                            "Server Error", Toast.LENGTH_SHORT).show();
                    dProgressbar.dismiss();
                }
            });
            postApiHandler.execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCPassSubmit:
                Log.e("LL", "dss");
                onChangePass();
                break;
        }
    }
}