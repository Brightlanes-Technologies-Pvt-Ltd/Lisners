package com.lisners.counsellor.Activity.Home.HomeStack;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.MyChargesActivity;
import com.lisners.counsellor.Activity.Auth.SignUpDetailsActivity;
import com.lisners.counsellor.Activity.Home.AppointmentStack.AppointmentDetailsFragment;
import com.lisners.counsellor.Activity.Home.AppointmentStack.AppointmentFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyPatientsFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyReviewFragment;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.MyUpcommingAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.viewmodel.HomeViewModel;
import com.lisners.counsellor.zWork.restApi.viewmodel.SettingViewModel;
import com.lisners.counsellor.zWork.utils.ViewModelUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader, tvPrice, tv_balances, tv_patients, tv_appointments, tv_reviews, tv_account_approved;
    ImageButton btn_header_left;
    LinearLayout lv_menu_review, lv_home_balance, lv_total_patient, lV_upcoming;
    RecyclerView rv_upcoming;
    DProgressbar dProgressbar;
    MyUpcommingAdapter pendingAdapter;
    LinearLayoutManager linearLayoutManager;
    AppointmentListModel listModel;
    TextView tv_no_result;
    ProgressBar pb_loader;


    HomeViewModel homeVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        homeVM = ViewModelUtils.getViewModel(HomeViewModel.class, this);


        tv_no_result = view.findViewById(R.id.tv_no_result);
        tvHeader = view.findViewById(R.id.tvHeader);
        tv_account_approved = view.findViewById(R.id.tv_account_approved);
//        tvPrice = view.findViewById(R.id.tvPrice);
//        tvPrice.setVisibility(View.GONE);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        dProgressbar = new DProgressbar(getContext());
        tv_balances = view.findViewById(R.id.tv_balance);
        tv_patients = view.findViewById(R.id.tv_patients);
        tv_appointments = view.findViewById(R.id.tv_appointments);
        tv_reviews = view.findViewById(R.id.tv_reviews);
        rv_upcoming = view.findViewById(R.id.rv_upcoming);
        lV_upcoming = view.findViewById(R.id.lV_upcoming);
        pb_loader = view.findViewById(R.id.pb_loader);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);
        lv_home_balance = view.findViewById(R.id.lv_home_balance);
        lv_home_balance.setOnClickListener(this);
        btn_header_left.setOnClickListener(this);
        lV_upcoming.setOnClickListener(this);
//        cv_item = view.findViewById(R.id.cv_item);
//        cv_item.setOnClickListener(this);
        lv_menu_review = view.findViewById(R.id.lv_menu_review);
        lv_total_patient = view.findViewById(R.id.lv_total_patient);
        lv_menu_review.setOnClickListener(this);
        lv_total_patient.setOnClickListener(this);
        tvHeader.setText("Dashboard");

        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_upcoming.setLayoutManager(linearLayoutManager);

        pendingAdapter = new MyUpcommingAdapter(getContext(), new MyUpcommingAdapter.OnItemClickListener() {
            @Override
            public void onClick(BookedAppointment jobs, int pos) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AppointmentDetailsFragment appointmentDetailsFragment = new AppointmentDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("BOOK_ID", jobs.getId() + "");
                appointmentDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, appointmentDetailsFragment);
                transaction.addToBackStack("HomeFragment");
                transaction.commit();
            }
        });
        rv_upcoming.setAdapter(pendingAdapter);
        showDashBoard();
        getDashBoard();
        getPendingAppointments();
        getProfile();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack("HomeFragment");
        transaction.commit();
    }

    private void showDashBoard() {
        tv_balances.setText(GlobalData.dashboard_balance + " ₹");
        tv_patients.setText(GlobalData.dashboard_patient);
        tv_appointments.setText(GlobalData.dashboard_appointments);
        tv_reviews.setText(GlobalData.dashboard_total_review);
    }

    private void getDashBoard() {

        homeVM.getDashboard()
                .observe(getViewLifecycleOwner(), response -> {
                    dProgressbar.dismiss();
                    if (response.getStatus() && response.getData() != null) {

                        GlobalData.dashboard_balance = response.getData().getWallet();
                        GlobalData.dashboard_patient = response.getData().getPatients();
                        GlobalData.dashboard_appointments =response.getData().getPending_interview();
                        GlobalData.dashboard_total_interview = response.getData().getTotal_interview();
                        GlobalData.dashboard_reviews = response.getData().getAvg_rating();
                        GlobalData.dashboard_total_calls = response.getData().getCall_sec();
                        GlobalData.dashboard_total_reviews =response.getData().getRating();
                        GlobalData.dashboard_pending_interview = response.getData().getPending_interview();
                        GlobalData.dashboard_total_review = response.getData().getTotal_review();

                    }

                    showDashBoard();

                });

        //  dProgressbar.show();
       /* GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_DASHBOARD, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                Log.e("jsonObject", new Gson().toJson(jsonObject));
                dProgressbar.dismiss();
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    JSONObject datajson = jsonObject.getJSONObject("data");
                    GlobalData.dashboard_balance = datajson.getString("wallet");//
                    GlobalData.dashboard_patient = datajson.getString("patients");//
                    GlobalData.dashboard_appointments = datajson.getString("pending_interview");
                    GlobalData.dashboard_total_interview = datajson.getString("total_interview");//
                    GlobalData.dashboard_reviews = datajson.getString("avg_rating");//
                    GlobalData.dashboard_total_calls = datajson.getString("call_sec"); //
                    GlobalData.dashboard_total_reviews = datajson.getString("rating");//
                    GlobalData.dashboard_pending_interview = datajson.getString("pending_interview");//
                    GlobalData.dashboard_total_review = datajson.getString("total_review");//
                }
                showDashBoard();
            }

            @Override
            public void onError() {
                dProgressbar.dismiss();
            }
        });
        apiHandler.execute();*/



    }

    private void getPendingAppointments() {
        pb_loader.setVisibility(View.VISIBLE);



        homeVM.getPendingAppointments()
                .observe(getViewLifecycleOwner(), response -> {
                    pb_loader.setVisibility(View.GONE);
                    if (response.getStatus() && response.getData() != null) {

                        listModel = response.getData();
                        pendingAdapter.updateList(listModel.getData());
                        if (listModel.getTotal() <= 0)
                            tv_no_result.setVisibility(View.VISIBLE);
                        else
                            tv_no_result.setVisibility(View.GONE);

                    }
                    else {
                        if (pendingAdapter.getItemCount() <= 0)
                            tv_no_result.setVisibility(View.VISIBLE);
                    }

                });
     /*   GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_PENDING, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                pb_loader.setVisibility(View.GONE);
                if (jsonObject.has("status")) {
                    listModel = new Gson().fromJson(jsonObject.getString("data"), AppointmentListModel.class);
                    pendingAdapter.updateList(listModel.getData());
                    if (listModel.getTotal() <= 0)
                        tv_no_result.setVisibility(View.VISIBLE);
                    else
                        tv_no_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                pb_loader.setVisibility(View.GONE);
                if (pendingAdapter.getItemCount() <= 0)
                    tv_no_result.setVisibility(View.VISIBLE);
            }
        });
        apiHandler.execute()*/;
    }

    public void getProfile() {


        homeVM.getProfile()
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getStatus() && response.getData() != null) {

                        User user = response.getData();
                        if (user.getIs_approved() == 0) {
                            tv_account_approved.setText(String.format("Dear %s thank you for registering with us. Once approved, your profile will be live in next 12 hours.\n Team Lisners.", user.getName()));
                            tv_account_approved.setVisibility(View.VISIBLE);
                        } else {
                            tv_account_approved.setText("");
                            tv_account_approved.setVisibility(View.GONE);
                        }

                    }

                });


        /*GetApiHandler apiHandler = new GetApiHandler(getActivity(), URLs.GET_PROFILE, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    User user = new Gson().fromJson(jsonObject.getString("data"), User.class);
                    if (user.getIs_approved() == 0) {
                        tv_account_approved.setText(String.format("Dear %s thank you for registering with us. Once approved, your profile will be live in next 12 hours.\n Team Lisners.", user.getName()));
                        tv_account_approved.setVisibility(View.VISIBLE);
                    } else {
                        tv_account_approved.setText("");
                        tv_account_approved.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onError() {
            }
        });
        apiHandler.execute();*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.cv_item:
                loadFragment(new AppointmentDetailsFragment());
                break;
            case R.id.lv_menu_review:
                loadFragment(new MyReviewFragment());
                break;
            case R.id.lv_home_balance:
                loadFragment(new WalletHistoryFragment());
                break;
            case R.id.lv_total_patient:
                loadFragment(new MyPatientsFragment());
                break;
            case R.id.lV_upcoming:

                //loadFragment(new AppointmentFragment());
                break;


        }
    }


}