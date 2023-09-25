package com.lisners.counsellor.Activity.Home.AppointmentStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.AddPrescriptionFragment;
import com.lisners.counsellor.Adapters.MyAppointmentAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.ModelPanding;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CompleteFragment extends Fragment {
    RecyclerView rv_complete ;
    DProgressbar dProgressbar;
    MyAppointmentAdapter pendingAdapter ;
    LinearLayoutManager linearLayoutManager ;
    AppointmentListModel listModel ;
    TextView tv_no_result ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        rv_complete =view.findViewById(R.id.rv_complete);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_complete.setLayoutManager(linearLayoutManager);

        pendingAdapter = new MyAppointmentAdapter(getContext(), new MyAppointmentAdapter.OnItemClickListener() {
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
        Map<String,String> params = new HashMap<>();
        PostApiHandler postApiHandler = new PostApiHandler(getContext(), URLs.GET_COMPLETE, params, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {

                dProgressbar.dismiss();
                if(jsonObject.has("status")){
                    listModel = new Gson().fromJson(jsonObject.getString("data"),AppointmentListModel.class);
                    pendingAdapter.updateList(listModel.getData());
                    if(listModel.getTotal()<=0)
                        tv_no_result.setVisibility(View.VISIBLE);
                    else
                        tv_no_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError anError) {
                dProgressbar.dismiss();
                if(pendingAdapter.getItemCount()<=0)
                    tv_no_result.setVisibility(View.VISIBLE);
            }
        }) ;
        postApiHandler.execute();
    }
}