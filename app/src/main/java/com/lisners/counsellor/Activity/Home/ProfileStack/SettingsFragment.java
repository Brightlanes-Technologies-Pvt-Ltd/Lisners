package com.lisners.counsellor.Activity.Home.ProfileStack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.ChangePasswordActivity;
import com.lisners.counsellor.Activity.Auth.MultiCheckDropDwon;
import com.lisners.counsellor.ApiModal.CounselorProfile;
import com.lisners.counsellor.ApiModal.LanguagesModel;
import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
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

public class SettingsFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    ImageButton btn_header_left;
    LinearLayout lv_change_pass;
    Button btn_setting_save;
    StoreData storeData;
    DProgressbar dProgressbar;
    TextView edit_voice_call_rate, edit_video_call_rate, tv_language, edit_info;
    ArrayList<ModelSpacialization> lauguagelist;
    MultiCheckDropDwon speci_dropdown;
    SwitchCompat toggleSwitch;
    User user;
    ProgressBar pb_loader;
    CounselorProfile counselorProfile;
    boolean switch_start_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        init(view);
        return view;
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        storeData = new StoreData(getContext());
        dProgressbar = new DProgressbar(getContext());
        btn_header_left = view.findViewById(R.id.btn_header_left);
        lv_change_pass = view.findViewById(R.id.lv_change_pass);
        btn_setting_save = view.findViewById(R.id.btn_setting_save);
        tv_language = view.findViewById(R.id.tv_language);
        edit_video_call_rate = view.findViewById(R.id.edit_video_call_rate);
        edit_voice_call_rate = view.findViewById(R.id.edit_voice_call_rate);
        edit_info = view.findViewById(R.id.edit_info);
        toggleSwitch = view.findViewById(R.id.toggleSwitch);
        pb_loader = view.findViewById(R.id.pb_loader);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        tv_language.setOnClickListener(this);
        btn_setting_save.setOnClickListener(this);
        lv_change_pass.setOnClickListener(this);
        btn_header_left.setOnClickListener(this);
        tvHeader.setText("Settings");

        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch_start_status) {
                    if (isChecked)
                        getSwitch(1);
                    else
                        getSwitch(0);
                }
            }
        });

        getProfile();
    }


    private void getSwitch(int check) {

        Map<String, String> params = new HashMap<>();
        params.put("is_notify", check + "");
        pb_loader.setVisibility(View.VISIBLE);
        PostApiHandler postApiHandler = new PostApiHandler(getContext(), URLs.PROFILE_UPDATE, params, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                pb_loader.setVisibility(View.GONE);
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    storeData.setData(ConstantValues.USER_DATA, jsonObject.getString("data"), new StoreData.SetListener() {
                        @Override
                        public void setOK() {
                            if (check != 1)
                                Toast.makeText(getContext(), "Notification Off", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Notification On", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(ANError error) {
                pb_loader.setVisibility(View.GONE);
            }
        });
        postApiHandler.execute();
    }

    public void getProfile() {
        storeData.getData(ConstantValues.USER_DATA_UPDATE, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                storeData.getData(ConstantValues.USER_DATA, new StoreData.GetListener() {
                    @Override
                    public void getOK(String val) {
                        if (val != null) {
                            showProfile(val);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }

            @Override
            public void onFail() {
                dProgressbar.show();
                GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_PROFILE, new GetApiHandler.OnClickListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        if (jsonObject.has("status") && jsonObject.has("data")) {
                            showProfile(jsonObject.getString("data"));
                            Map<String, String> params = new HashMap<>();
                            params.put(ConstantValues.USER_DATA, jsonObject.getString("data"));
                            params.put(ConstantValues.USER_DATA_UPDATE, "YES");
                            storeData.setMultiData(params, new StoreData.SetListener() {
                                @Override
                                public void setOK() {
                                    dProgressbar.dismiss();
                                }
                            });
                            dProgressbar.dismiss();
                        }
                    }

                    @Override
                    public void onError() {
                        dProgressbar.dismiss();
                    }
                });
                apiHandler.execute();
            }
        });
    }

    private void showProfile(String val) {
        user = new Gson().fromJson(val, User.class);
        ArrayList<LanguagesModel> languagesModels = user.getLanguages();
        CounselorProfile counselorProfile = user.getCounselor_profile();
        lauguagelist = new ArrayList<>();

        if (languagesModels != null) {
            String s = "";
            for (LanguagesModel lge : languagesModels) {
                ModelSpacialization spacialization = new ModelSpacialization(lge.getId(), lge.getTitle(), true);
                lauguagelist.add(spacialization);
                if (s.isEmpty()) s = lge.getTitle();
                else s = s + ", " + lge.getTitle();
                tv_language.setTextColor(Color.BLACK);
            }
            tv_language.setText(s);
        }
        if (counselorProfile != null) {
            edit_info.setText(counselorProfile.getDescription());
            edit_video_call_rate.setText(counselorProfile.getVideo_call());
            edit_voice_call_rate.setText(counselorProfile.getVoice_call());
        }

        toggleSwitch.setChecked(user.getIs_notify() == 1);
        switch_start_status = true;
    }

    boolean dataLoading = true;

    private void showLanguage() {
        if (lauguagelist != null && !lauguagelist.isEmpty() && speci_dropdown != null) {
            speci_dropdown.show();

        } else {
            lauguagelist = new ArrayList<>();
            if (dataLoading) {
                dataLoading = false;
                ArrayList<LanguagesModel> selected_list = user.getLanguages();
                GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_LANGUAGES, new GetApiHandler.OnClickListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        dataLoading = true;
                        Log.e("D", new Gson().toJson(jsonObject));
                        if (jsonObject.getBoolean("status")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ModelSpacialization s = new Gson().fromJson(jsonArray.getString(i), ModelSpacialization.class);
                                for (LanguagesModel model : selected_list) {
                                    if (s.getId() == model.getId())
                                        s.setCheck(true);
                                }
                                lauguagelist.add(s);
                            }
                            Log.e("LINst", lauguagelist.size() + "");
                            speci_dropdown = new MultiCheckDropDwon(getContext(), "Select language", lauguagelist, new MultiCheckDropDwon.OnItemClickListener() {
                                @Override
                                public void onClick(ArrayList<ModelSpacialization> selected_spaci) {
                                    lauguagelist = selected_spaci;
                                    String s = "";
                                    for (ModelSpacialization lge : selected_spaci) {
                                        if (lge.isCheck()) {
                                            if (s.isEmpty()) s = lge.getName();
                                            else s = s + ", " + lge.getName();
                                            tv_language.setTextColor(Color.BLACK);
                                            LanguagesModel languagesModel = new LanguagesModel(lge.getId(), true, lge.getName());
                                            selected_list.add(languagesModel);
                                        }
                                    }
                                    tv_language.setText(s);
                                    tv_language.setError(null);
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
            }
        }
    }

    private void onSaveChanges() {
        counselorProfile = user.getCounselor_profile();
        ArrayList<LanguagesModel> languages = new ArrayList<>();
        if (lauguagelist != null) {
            for (int i = 0; i < lauguagelist.size(); i++) {
                if (lauguagelist.get(i).isCheck()) {
                    LanguagesModel languagesModel = new LanguagesModel(lauguagelist.get(i).getId(), true, lauguagelist.get(i).getName());
                    languages.add(languagesModel);
                }
            }
        }

        String st_voice = edit_voice_call_rate.getText().toString();
        String st_video = edit_video_call_rate.getText().toString();
        String st_info = edit_info.getText().toString();


        if (st_voice.isEmpty() || !Pattern.matches("([0-9]*[.])?[0-9]+", st_voice.toString()) || Double.parseDouble(st_voice.toString()) <= 0)
            Toast.makeText(getContext(), "please enter valid voice charge", Toast.LENGTH_SHORT).show();

        else if (st_video.isEmpty() || !Pattern.matches("([0-9]*[.])?[0-9]+", st_video.toString()) || Double.parseDouble(st_video.toString()) <= 0)
            Toast.makeText(getContext(), "please enter valid video charge", Toast.LENGTH_SHORT).show();

        else if (st_info.length() < 100)
            Toast.makeText(getContext(), "The description must be at least 100 characters", Toast.LENGTH_SHORT).show();
        else if (languages.size() <= 0)
            Toast.makeText(getContext(), "PLease select more than one language", Toast.LENGTH_SHORT).show();
        else {
            Map<String, String> params = new HashMap<>();
            params.put("voice_call", st_voice);
            params.put("video_call", st_video);
            int st = 0;
            if (languages != null) {
                for (int i = 0; i < languages.size(); i++) {
                    params.put("language_id[" + st + "]", languages.get(i).getId() + "");
                }
            }
            params.put("description", st_info);
            dProgressbar.show();
            PostApiHandler apiHandler = new PostApiHandler(getContext(), URLs.SET_CHANGES, params, new PostApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if (counselorProfile == null)
                        counselorProfile = new CounselorProfile();
                    dProgressbar.dismiss();
                    if (jsonObject.has("status")) {
                        counselorProfile.setVideo_call(st_video);
                        counselorProfile.setVoice_call(st_voice);
                        counselorProfile.setDescription(st_info);
                        user.setCounselorProfile(counselorProfile);
                        user.setLanguages(languages);
                        storeData.setData(ConstantValues.USER_DATA, new Gson().toJson(user), new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                Toast.makeText(getContext(), "Your profile has been updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (jsonObject.has("errors")) {
                        JSONObject errObj = jsonObject.getJSONObject("errors");
                        for (Iterator<String> it = errObj.keys(); it.hasNext(); ) {
                            String key = it.next();
                            if (key != null)
                                Toast.makeText(getContext(), UtilsFunctions.errorShow(errObj.getJSONArray(key)), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(ANError error) {
                    dProgressbar.dismiss();
                }
            });
            apiHandler.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
            case R.id.lv_change_pass:
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                intent.putExtra("FROM", "CHANGE_PASSWORD");
                startActivity(intent);
                break;
            case R.id.btn_setting_save:
                onSaveChanges();
                break;
            case R.id.tv_language:
                showLanguage();
                break;
        }
    }
}