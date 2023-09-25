package com.lisners.counsellor.Activity.Home.AvailabilityStack;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.AvailabilityListAdaptor;
import com.lisners.counsellor.Adapters.AvailabilityTimeAdaptor;
import com.lisners.counsellor.ApiModal.APIErrorModel;
import com.lisners.counsellor.ApiModal.ModelDaySlot;
import com.lisners.counsellor.ApiModal.ModellistItem;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.DTimePicker;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.utils.helperClasses.MultiSelectDropDownForAvailiblityDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAvailabilityFragment extends Fragment implements View.OnClickListener {
    RecyclerView rv_monday, rv_tuesday, rv_available_list;
    ArrayList<String> strings;
    ImageButton btn_header_left;
    TextView tvHeader;
    Button btn_sign_in;

    LinearLayout lv_day, lv_start_time /*lv_end_time*/;
    TextView tv_day, tv_start_time/*, tv_end_time*/;
    ProgressBar pb_day;
    ImageView iv_day_arrow;
    ArrayList<ModellistItem> days;
    List<ModellistItem> selectedDay = new ArrayList<>();
    ArrayList<ModelDaySlot> modelDaySlots;
    int st_hour = 30, st_mint/*, end_hour = 30, end_mint*/;
    AvailabilityListAdaptor listAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_availability, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        tvHeader.setText("My Availability");

        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);
        btn_header_left.setOnClickListener(this);

        btn_sign_in = view.findViewById(R.id.btn_sign_in);
        lv_day = view.findViewById(R.id.lv_day);
        tv_day = view.findViewById(R.id.tv_day);
        pb_day = view.findViewById(R.id.pb_day);
        iv_day_arrow = view.findViewById(R.id.iv_day_arrow);
        lv_start_time = view.findViewById(R.id.lv_start_time);
        tv_start_time = view.findViewById(R.id.tv_start_time);
        rv_available_list = view.findViewById(R.id.rv_available_list);

       /* lv_end_time = view.findViewById(R.id.lv_end_time);
        tv_end_time = view.findViewById(R.id.tv_end_time);*/
        modelDaySlots = new ArrayList<>();

        btn_sign_in.setOnClickListener(this);
        /*lv_end_time.setOnClickListener(this);*/
        lv_start_time.setOnClickListener(this);
        lv_day.setOnClickListener(this);






        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_available_list.setLayoutManager(linearLayoutManager);
        listAdaptor = new AvailabilityListAdaptor(getContext(), new ArrayList<>(), new AvailabilityTimeAdaptor.TimingListWatcher() {
            @Override
            public void onTimingListEmpty() {
                getAvailability();

            }
        });
        rv_available_list.setAdapter(listAdaptor);

        getAvailability();

    }

    public void showDayDailog() {
        /*new SingleSelectionDropDown(getContext(), days, new SingleSelectionDropDown.OnItemClickListener() {
            @Override
            public void onClick(ModellistItem selected_spaci) {
                selectedDay = selected_spaci;
                tv_day.setText(selected_spaci.getName());
                tv_day.setTextColor(Color.BLACK);
            }
        });*/

        new MultiSelectDropDownForAvailiblityDay(getContext(), days, new MultiSelectDropDownForAvailiblityDay.OnOkClickListener() {
            @Override
            public void onClick(List<ModellistItem> list, String text) {
                selectedDay.clear();
                selectedDay.addAll(list);
                if (list.isEmpty()) {
                    tv_day.setText("Select Day");
                    tv_day.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
                } else {
                    tv_day.setText(text);
                    tv_day.setTextColor(Color.BLACK);
                }


            }
        });
    }

    public void getDaysFromApi() {

        pb_day.setVisibility(View.VISIBLE);
        iv_day_arrow.setVisibility(View.GONE);
        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_WEEK_DAYS, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                pb_day.setVisibility(View.GONE);
                iv_day_arrow.setVisibility(View.VISIBLE);
                if (jsonObject.has("status")) {
                    days = new ArrayList<>();
                    JSONArray jsonData = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject object = (JSONObject) jsonData.get(i);
                        days.add(new ModellistItem(object.getString("id"), object.getString("day")));
                    }
                    days.add(0, new ModellistItem("", "All Days"));
                    showDayDailog();
                }
            }

            @Override
            public void onError() {
                iv_day_arrow.setVisibility(View.VISIBLE);
                pb_day.setVisibility(View.GONE);
            }
        });
        apiHandler.execute();
    }

    public void saveAvailability() {

        if (selectedDay.isEmpty())
            Toast.makeText(getContext(), "Select Day", Toast.LENGTH_SHORT).show();
        else if (st_hour == 30)
            Toast.makeText(getContext(), "Select Start Time", Toast.LENGTH_SHORT).show();
       /* else if (end_hour == 30)
            Toast.makeText(getContext(), "Select End Time", Toast.LENGTH_SHORT).show();
        else if ((st_hour * 60 + st_mint) >= (end_hour * 60 + end_mint))
            Toast.makeText(getContext(), "End time should always be greater than Start time.", Toast.LENGTH_SHORT).show();
        */
        else {


            DProgressbar dProgressbar = new DProgressbar(getContext());
            dProgressbar.show();

            String st_time = st_hour + ":" + st_mint;
            /*String end_time = end_hour + ":" + end_mint;*/

            Map<String, String> params = new HashMap<>();
            int lnIdx = 1;

            if (selectedDay.size() == 1) {
                params.put("week_day_id[]", selectedDay.get(0).getId());
            } else {
                for (int i = 0; i < selectedDay.size(); i++) {
                    params.put("week_day_id[" + lnIdx + "]", selectedDay.get(i).getId());
                    Log.e("day==>", "" + selectedDay.get(i).getName());
                    lnIdx++;
                }
            }

            /*if (selectedDay.getId().equalsIgnoreCase("")) {

                for (int i = 1; i < days.size(); i++) {
                    params.put("week_day_id[" + lnIdx + "]", days.get(i).getId());
                    Log.e("day==>",""+days.get(i).getName());
                    lnIdx++;
                }
            } else {
                params.put("week_day_id[]", selectedDay.getId());
            }*/

            params.put("start_time", st_time);
            /* params.put("end_time", end_time);*/
            PostApiHandler handler = new PostApiHandler(getContext(), URLs.SET_AVAILABLE, params, new PostApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {

                    if (jsonObject.has("status") && jsonObject.getBoolean("status") && jsonObject.has("data")) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = dataJson.getJSONArray("all_data");
                        modelDaySlots = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ModelDaySlot slot = new Gson().fromJson(jsonArray.getString(i), ModelDaySlot.class);

                            if (slot.getTime_slot() != null && !slot.getTime_slot().isEmpty()) {
                                modelDaySlots.add(slot);
                            }
                        }
                        listAdaptor.onUpdateList(modelDaySlots);
                    }
                    if (jsonObject.has("message"))
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    dProgressbar.dismiss();
                }

                @Override
                public void onError(ANError error) {
                    dProgressbar.dismiss();
                    if (error.getErrorBody() != null) {
                        APIErrorModel apiErrorModel = new Gson().fromJson(error.getErrorBody(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            Toast.makeText(getContext(), apiErrorModel.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
            handler.execute();
        }

    }

    public void getAvailability() {
        DProgressbar dProgressbar = new DProgressbar(getContext());
        dProgressbar.show();

        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_AVAILABLE, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                dProgressbar.dismiss();

                if (jsonObject.has("status")) {
                    Log.e("status", new Gson().toJson(jsonObject));
                    modelDaySlots.clear();

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ModelDaySlot slot = new Gson().fromJson(jsonArray.getString(i), ModelDaySlot.class);
                        if (slot.getTime_slot() != null && !slot.getTime_slot().isEmpty()) {
                            modelDaySlots.add(slot);
                        }
                    }

                    listAdaptor.onUpdateList(modelDaySlots);
                    Log.e("D", modelDaySlots.size() + "");

                } else {
                    Log.e("Dd", new Gson().toJson(jsonObject));
                }
            }

            @Override
            public void onError() {
                dProgressbar.dismiss();
            }
        });
        apiHandler.execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.lv_day:
                if (days != null)
                    showDayDailog();
                else
                    getDaysFromApi();
                break;
            case R.id.lv_start_time:
                new DTimePicker(getContext(), new DTimePicker.TimeListener() {
                    @Override
                    public void onClick(int h, int m) {
                        String am_pm;

                        st_hour = h;
                        st_mint = m;

                        if (h == 0) {
                            am_pm = "AM";
                            h = 12;
                        } else if (h == 12) {
                            am_pm = "PM";
                        } else if (h > 12) {
                            am_pm = "PM";
                            h = h - 12;
                        } else {
                            am_pm = "AM";
                        }
                        tv_start_time.setText((h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + " " + am_pm);
                        tv_start_time.setTextColor(Color.BLACK);


                    }
                });
                break;
           /* case R.id.lv_end_time:
                new DTimePicker(getContext(), new DTimePicker.TimeListener() {
                    @Override
                    public void onClick(int h, int m) {
                        String am_pm;

                        end_hour = h;
                        end_mint = m;

                        if (h == 0) {
                            am_pm = "AM";
                            h = 12;
                        } else if (h == 12) {
                            am_pm = "PM";
                        } else if (h > 12) {
                            am_pm = "PM";
                            h = h - 12;
                        } else {
                            am_pm = "AM";
                        }

                        tv_end_time.setText((h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + " " + am_pm);
                        tv_end_time.setTextColor(Color.BLACK);


                    }
                });
                break;*/
            case R.id.btn_sign_in:
                saveAvailability();
                break;
        }
    }
}