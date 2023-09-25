package com.lisners.counsellor.Activity.Home.HomeStack;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.AddPrescriptionFragment;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.ReportAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ReportsFragment extends Fragment  implements View.OnClickListener{
    ImageButton btn_header_left , ib_search ;
    TextView tvHeader , tv_end_date ,tv_start_date;
    RecyclerView rv_complete ;
    LinearLayout lv_start_date , lv_end_date ;
    DProgressbar dProgressbar;
    ReportAdapter pendingAdapter ;
    LinearLayoutManager linearLayoutManager ;
    AppointmentListModel listModel ;
    int st_year ,end_year ,st_month ,st_day ,end_month , end_day ;
    FrameLayout fl_root ;
    TextView tv_no_result ;
    String pdf_url ;
    Map<String,String> topparams ;
    StoreData storeData ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        tvHeader.setText("My Reports");
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);
        btn_header_left.setOnClickListener(this);
        rv_complete =view.findViewById(R.id.rv_complete);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        lv_start_date = view.findViewById(R.id.lv_start_date);
        lv_end_date = view.findViewById(R.id.lv_end_date);
        tv_start_date = view.findViewById(R.id.tv_start_date);
        tv_end_date = view.findViewById(R.id.tv_end_date);
        ib_search = view.findViewById(R.id.ib_search);
        fl_root = view.findViewById(R.id.fl_root);
        linearLayoutManager = new LinearLayoutManager(getContext());
        lv_start_date.setOnClickListener(this);
        lv_end_date.setOnClickListener(this);
        ib_search.setOnClickListener(this);
        fl_root.setOnClickListener(this);
        rv_complete.setLayoutManager(linearLayoutManager);
        storeData = new StoreData(getContext());
        fl_root.setVisibility(View.GONE);
        pendingAdapter = new ReportAdapter(getContext(), new ReportAdapter.OnItemClickListener() {
            @Override
            public void onClick(BookedAppointment jobs, int pos) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AddPrescriptionFragment appointmentDetailsFragment =  new AddPrescriptionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("BOOK_ID",jobs.getId()+"" );
                bundle.putString("HIDE", "HIDE" );
                appointmentDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container,appointmentDetailsFragment);
                transaction.addToBackStack("PatientDetailsFragment");
                transaction.commit();
            }
        });
        rv_complete.setAdapter(pendingAdapter);
        dProgressbar = new DProgressbar(getContext());

        getPendingAppointments();


    }


    private void getPendingAppointments(){
        dProgressbar.show();
        Map<String,String> params  = new HashMap<>();
        if(end_day!=0)
         params.put("start_date",st_year+"-"+st_month+"-"+st_day);
        if(st_year !=0)
         params.put("end_date",end_year+"-"+end_month+"-"+end_day);
        PostApiHandler postApiHandler = new PostApiHandler(getContext(), URLs.GET_COMPLETE, params, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {

                dProgressbar.dismiss();
                if(jsonObject.has("status") && jsonObject.getBoolean("status")){
                    listModel = new Gson().fromJson(jsonObject.getString("data"),AppointmentListModel.class);

                    pendingAdapter.updateListFirst(listModel.getData());
                    if(listModel.getTotal()<=0) {
                        tv_no_result.setVisibility(View.VISIBLE);

                    }
                    else {
                        tv_no_result.setVisibility(View.GONE);
                        fl_root.setVisibility(View.GONE);
                        if(params.containsKey("start_date")) {
                            fl_root.setVisibility(View.VISIBLE);
                            topparams =params ;
                        }
                    }
                }
                else {
                    pendingAdapter.updateListFirst(new ArrayList<>());
                    pendingAdapter.notifyDataSetChanged();
                    tv_no_result.setVisibility(View.VISIBLE);
                    fl_root.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("ANError",new Gson().toJson(params));
                dProgressbar.dismiss();

                pendingAdapter.updateListFirst(new ArrayList<>());
                pendingAdapter.notifyDataSetChanged();
                tv_no_result.setVisibility(View.VISIBLE);
                fl_root.setVisibility(View.GONE);

                // old
                if(pendingAdapter.getItemCount()<=0)
                    tv_no_result.setVisibility(View.VISIBLE);
            }
        }) ;
        postApiHandler.execute();
    }


    public void fileDownload(User user) {
      String yrl = URLs.SET_REPORT_PDF+"?start_date="+topparams.get("start_date")+"&end_date="+topparams.get("end_date")+"&user_id="+user.getId();
        Log.e("yrl",yrl);

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(yrl));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Wrong Url", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        switch(v.getId()){
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.lv_start_date :
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                st_day =dayOfMonth ;
                                st_month =monthOfYear+1 ;
                                st_year =year ;

                                tv_start_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                tv_start_date.setTextColor(Color.BLACK);
                            }
                        }, mYear , mMonth, mDay);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(Long.valueOf("-631171800000"));
                datePickerDialog.show();
                break;
            case R.id.lv_end_date :
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                end_day =dayOfMonth ;
                                end_month =monthOfYear+1 ;
                                end_year =year ;
                                tv_end_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                tv_end_date.setTextColor(Color.BLACK);
                            }
                        }, mYear , mMonth, mDay);

                datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog1.getDatePicker().setMinDate(Long.valueOf("-631171800000"));
                datePickerDialog1.show();
                break;
            case R.id.ib_search:
                getPendingAppointments();
                break;
            case R.id.fl_root :
                storeData.getData(ConstantValues.USER_DATA, new StoreData.GetListener() {
                    @Override
                    public void getOK(String val) {
                        if(val!=null) {
                            User user = new Gson().fromJson(val,User.class);
                            fileDownload(user);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });


                break;
        }
    }


}