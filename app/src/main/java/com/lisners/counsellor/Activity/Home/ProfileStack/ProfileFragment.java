package com.lisners.counsellor.Activity.Home.ProfileStack;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.MultiCheckDropDwon;
import com.lisners.counsellor.Activity.Home.AppointmentStack.AppointmentFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyPatientsFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyReviewFragment;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.ApiModal.CounselorProfile;
import com.lisners.counsellor.ApiModal.LanguagesModel;
import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.config.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView tv_profession, version;
    TextView tvHeader, tv_patients, tv_appointments, tv_total_reviews, tv_calls, tv_profile_name, tv_spacilization;
    TextView edit_voice_call_rate, edit_video_call_rate, tv_language, edit_info;
    Button btn_setting_save;
    LinearLayout spn_language;
    ImageButton btn_header_left;
    ImageView imageView, iv_profile;
    DProgressbar dProgressbar;
    RatingBar ratingBar;
    StoreData storeData;
    LinearLayout lv_total_patient, lv_total_reviews, tv_total_app;

    ArrayList<ModelSpacialization> lauguagelist;
    MultiCheckDropDwon speci_dropdown;
    User user;
    CounselorProfile counselorProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        version = view.findViewById(R.id.version);
        tv_profession = view.findViewById(R.id.tv_profession);
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);
        dProgressbar = new DProgressbar(getContext());
        btn_header_left.setOnClickListener(this);
        tv_patients = view.findViewById(R.id.tv_patients);
        tv_appointments = view.findViewById(R.id.tv_appointment);
        tv_total_reviews = view.findViewById(R.id.tv_total_reviews);
        tv_calls = view.findViewById(R.id.tv_calls);
        imageView = view.findViewById(R.id.iv_edit_profile);
        tv_profile_name = view.findViewById(R.id.tv_profile_name);
        iv_profile = view.findViewById(R.id.iv_profile);
        ratingBar = view.findViewById(R.id.ratingBar);
        tv_spacilization = view.findViewById(R.id.tv_spacilization);
        lv_total_patient = view.findViewById(R.id.lv_total_patient);
        lv_total_reviews = view.findViewById(R.id.lv_total_reviews);
        tv_total_app = view.findViewById(R.id.tv_total_app);

        tv_language = view.findViewById(R.id.tv_language);
        edit_video_call_rate = view.findViewById(R.id.edit_video_call_rate);
        edit_voice_call_rate = view.findViewById(R.id.edit_voice_call_rate);
        edit_info = view.findViewById(R.id.edit_info);
        btn_setting_save = view.findViewById(R.id.btn_setting_save);
        btn_setting_save.setOnClickListener(this);

        spn_language = view.findViewById(R.id.spn_language);
        spn_language.setOnClickListener(this);

        storeData = new StoreData(getContext());

        ratingBar.setEnabled(false);
        imageView.setOnClickListener(this);
        lv_total_patient.setOnClickListener(this);
        lv_total_reviews.setOnClickListener(this);
        tv_total_app.setOnClickListener(this);

        tvHeader.setText("My Profile");
        //showDashBoard();
        getDashBoard();


        getProfile();

        version.setText(String.valueOf(AppConfig.VERSION_NAME));
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
        ArrayList<SpacializationMedel> spacializationMedels = user.getSpecialization();
        UtilsFunctions.SetLOGO(getContext(), user.getProfile_image(), iv_profile);

        if (user.getCounselor_profile().getProfession() != null) {
            tv_profession.setText(user.getCounselor_profile().getProfession().getTitle());
        }
        tv_profile_name.setText(UtilsFunctions.splitCamelCase(user.getName()));
        if (spacializationMedels != null) {
            for (SpacializationMedel sm : spacializationMedels) {
                if (tv_spacilization.getText().toString().isEmpty())
                    tv_spacilization.setText(sm.getTitle());
                else
                    tv_spacilization.setText(tv_spacilization.getText() + ", " + sm.getTitle());
            }

        }


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

    }

    private void showDashBoard() {
        tv_calls.setText(UtilsFunctions.formatNumber(String.valueOf(Long.parseLong(GlobalData.dashboard_total_calls) / 60)) + " min");
        tv_patients.setText(GlobalData.dashboard_patient);
        tv_appointments.setText(GlobalData.dashboard_total_interview);
        tv_total_reviews.setText(GlobalData.dashboard_total_review);

        try {
            ratingBar.setRating(Float.valueOf(GlobalData.dashboard_reviews));
        } catch (Exception e) {
            ratingBar.setRating(0);
        }
    }

    private void getDashBoard() {
        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_DASHBOARD, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                Log.e("jsonObject", new Gson().toJson(jsonObject));
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    JSONObject datajson = jsonObject.getJSONObject("data");
                    GlobalData.dashboard_total_calls = datajson.getString("call_sec");
                    GlobalData.dashboard_patient = datajson.getString("patients");
                    GlobalData.dashboard_appointments = datajson.getString("pending_interview");
                    GlobalData.dashboard_total_interview = datajson.getString("total_interview");
                    GlobalData.dashboard_total_reviews = datajson.getString("rating");
                    GlobalData.dashboard_reviews = datajson.getString("avg_rating");
                    GlobalData.dashboard_pending_interview = datajson.getString("pending_interview");
                    GlobalData.dashboard_total_review = datajson.getString("total_review");//
                }
                showDashBoard();
            }

            @Override
            public void onError() {

            }
        });
        apiHandler.execute();
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

    boolean dataLoading = true;

    private void showLanguage() {
        if (lauguagelist != null && !lauguagelist.isEmpty() && speci_dropdown != null) {
            speci_dropdown.show();

        } else {
            lauguagelist = new ArrayList<>();
            ArrayList<LanguagesModel> selected_list = user.getLanguages();

            if (dataLoading) {
                dataLoading = false;
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (view.getId()) {
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.iv_edit_profile:
                transaction.replace(R.id.fragment_container, new EditProfileFragment());
                transaction.addToBackStack("ProfileFragment");
                transaction.commit();
                break;
            case R.id.lv_total_patient:
                transaction.replace(R.id.fragment_container, new MyPatientsFragment());
                transaction.addToBackStack("ProfileFragment");
                transaction.commit();
                break;
            case R.id.lv_total_reviews:
                transaction.replace(R.id.fragment_container, new MyReviewFragment());
                transaction.addToBackStack("ProfileFragment");
                transaction.commit();
                break;
            case R.id.tv_total_app:

                Bundle args = new Bundle();
                args.putString("from", "" + 2);
                AppointmentFragment appointmentFragment = new AppointmentFragment();
                appointmentFragment.setArguments(args);
                transaction.replace(R.id.fragment_container, appointmentFragment);
                transaction.addToBackStack("ProfileFragment");
                transaction.commit();
                break;

            case R.id.btn_setting_save:
                onSaveChanges();
                break;
            case R.id.spn_language:
                showLanguage();
                break;
        }
    }
}