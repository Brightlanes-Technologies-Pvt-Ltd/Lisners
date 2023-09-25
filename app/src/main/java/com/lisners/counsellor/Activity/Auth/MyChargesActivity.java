package com.lisners.counsellor.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class MyChargesActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_sign_in;
    TextView tvHeader;
    EditText edit_language, edit_des;
    ImageButton btn_header_left;
    ArrayList<ModelSpacialization> lauguagelist;
    MultiCheckDropDwon speci_dropdown;
    EditText edit_video_call_rate, edit_voice_call_rate;
    DProgressbar dProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_charges);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        edit_language = findViewById(R.id.edit_language);
        tvHeader = findViewById(R.id.tvHeader);
        edit_des = findViewById(R.id.edit_des);
        edit_voice_call_rate = findViewById(R.id.edit_voice_call_rate);
        edit_video_call_rate = findViewById(R.id.edit_video_call_rate);
        tvHeader.setText("My Charges");
        btn_sign_in.setOnClickListener(this);
        edit_language.setOnClickListener(this);
        dProgressbar = new DProgressbar(this);
    }

    private void saveCharge() {

        String st_lge = edit_language.getText().toString();
        String st_des = edit_des.getText().toString();
        String st_video = edit_video_call_rate.getText().toString();
        String st_voice = edit_voice_call_rate.getText().toString();

        if (st_voice.isEmpty() || !Pattern.matches("([0-9]*[.])?[0-9]+", st_voice.toString()) || Double.parseDouble(st_voice.toString()) <= 0)
            Toast.makeText(this, "please enter valid voice charge", Toast.LENGTH_SHORT).show();
        else if (st_video.isEmpty() || !Pattern.matches("([0-9]*[.])?[0-9]+", st_video.toString()) || Double.parseDouble(st_video.toString()) <= 0)
            Toast.makeText(this, "please enter valid video charge", Toast.LENGTH_SHORT).show();
        else if (st_lge.isEmpty())
            edit_language.setError("Select Language");
        else if (st_des.isEmpty())
            edit_des.setError("Enter Description");
        else {


            dProgressbar.show();
            Map<String, String> map = new HashMap<>();
            map.put("voice_call", st_voice);
            map.put("video_call", st_video);
            map.put("description", st_des);
            int idx = 0;
            for (ModelSpacialization ss : lauguagelist) {
                if (ss.isCheck()) {
                    map.put("language_id[" + idx + "]", ss.getId() + "");
                    idx++;
                }
            }
            PostApiHandler apiHandler = new PostApiHandler(MyChargesActivity.this, URLs.SET_CHANGES, map, new PostApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    dProgressbar.dismiss();
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        startActivity(new Intent(MyChargesActivity.this, HomeActivity.class));
                        finish();
                    } else if (jsonObject.has("status") && jsonObject.has("errors")) {
                        JSONObject errObj = jsonObject.getJSONObject("errors");
                        for (Iterator<String> it = errObj.keys(); it.hasNext(); ) {
                            String key = it.next();
                            if (key != null)
                                Toast.makeText(MyChargesActivity.this, UtilsFunctions.errorShow(errObj.getJSONArray(key)), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(ANError error) {
                    Log.e("ERRE", error.getErrorDetail());
                    dProgressbar.dismiss();
                }
            });
            apiHandler.execute();
        }
    }

    boolean dataLoading = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                saveCharge();
                break;
            case R.id.edit_language:

                if (lauguagelist != null && !lauguagelist.isEmpty() && speci_dropdown != null) {
                    speci_dropdown.show();

                } else {
                    lauguagelist = new ArrayList<>();
                    if (dataLoading) {
                        dataLoading = false;
                        GetApiHandler apiHandler = new GetApiHandler(this, URLs.GET_LANGUAGES, new GetApiHandler.OnClickListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) throws JSONException {
                                dataLoading = true;
                                Log.e("D", new Gson().toJson(jsonObject));
                                if (jsonObject.getBoolean("status")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ModelSpacialization s = new Gson().fromJson(jsonArray.getString(i), ModelSpacialization.class);
                                        lauguagelist.add(s);
                                    }
                                    Log.e("LINst", lauguagelist.size() + "");
                                    speci_dropdown = new MultiCheckDropDwon(MyChargesActivity.this, "Select language", lauguagelist, new MultiCheckDropDwon.OnItemClickListener() {
                                        @Override
                                        public void onClick(ArrayList<ModelSpacialization> selected_spaci) {
                                            lauguagelist = selected_spaci;
                                            String s = "";
                                            for (ModelSpacialization lge : selected_spaci) {
                                                if (lge.isCheck()) {
                                                    if (s.isEmpty()) s = lge.getName();
                                                    else s = s + ", " + lge.getName();
                                                }
                                            }
                                            edit_language.setText(s);
                                            edit_language.setError(null);
                                        }
                                    });

                                }

                            }

                            @Override
                            public void onError() {
                                dataLoading = true;
                            }
                        });
                        apiHandler.execute();
                        break;

                    }
                }

        }
    }
}
