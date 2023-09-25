package com.lisners.counsellor.Activity.Home.AvailabilityStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.MyAppointmentAdapter;
import com.lisners.counsellor.Adapters.MyPatientAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;


public class MyPatientsFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    ImageButton btn_header_left , ib_search;
    DProgressbar dProgressbar ;
    RecyclerView rv_pending ;
    MyPatientAdapter pendingAdapter ;
    LinearLayoutManager linearLayoutManager ;
    AppointmentListModel listModel ;
    EditText etHeaderSearch ;
    TextView tv_no_result ;
    int page =1 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_patients, container, false);
        init(view);
        getPatients();
        return view;
    }

    private void init(View view) {

        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        ib_search = view.findViewById(R.id.ib_search);
        etHeaderSearch = view.findViewById(R.id.etHeaderSearch);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);

        btn_header_left.setOnClickListener(this);
        ib_search.setOnClickListener(this);
        rv_pending =view.findViewById(R.id.rv_pending);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_pending.setLayoutManager(linearLayoutManager);

        pendingAdapter = new MyPatientAdapter(getContext(), new MyPatientAdapter.OnItemClickListener() {
            @Override
            public void onClick(BookedAppointment jobs, int pos) {
                GlobalData.my_patients_details =jobs ;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PatientDetailsFragment detailsFragment =  new PatientDetailsFragment();
                transaction.replace(R.id.fragment_container,detailsFragment);
                transaction.addToBackStack("MyPatientsFragment");
                transaction.commit();
            }
        });

        etHeaderSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page = 1;
                    getPatients();
                    return true;
                }
                return false;
            }
        });
        rv_pending.setAdapter(pendingAdapter);
        dProgressbar = new DProgressbar(getContext());

        tvHeader.setText("My Patients");
    }


    private void getPatients(){

        dProgressbar.show();
        String st = etHeaderSearch.getText().toString();
        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_PATIENTS+"?page="+page+"&search="+st, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                dProgressbar.dismiss();
                if(jsonObject.has("status")){
                    listModel = new Gson().fromJson(jsonObject.getString("data"), AppointmentListModel.class);
                    if(page==1)
                        pendingAdapter.updateListFirst(listModel.getData());
                    else
                        pendingAdapter.updateList(listModel.getData());

                    if(listModel.getTotal()<=0)
                        tv_no_result.setVisibility(View.VISIBLE);
                    else
                        tv_no_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                dProgressbar.dismiss();
                if(pendingAdapter.getItemCount()<=0)
                    tv_no_result.setVisibility(View.VISIBLE);
            }
        }) ;

        apiHandler.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.ib_search:
                page =1 ;
                getPatients();
                break;
        }
    }
}