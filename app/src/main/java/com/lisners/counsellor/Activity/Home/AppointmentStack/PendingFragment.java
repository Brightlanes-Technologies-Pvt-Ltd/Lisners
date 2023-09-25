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

import com.google.gson.Gson;
import com.lisners.counsellor.Adapters.MyAppointmentAdapter;
import com.lisners.counsellor.Adapters.MyUpcommingAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.ModelPanding;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;


public class PendingFragment extends Fragment implements View.OnClickListener {
    //CardView cv_item;
    RecyclerView rv_pending ;
    DProgressbar dProgressbar;
    MyUpcommingAdapter pendingAdapter ;
    LinearLayoutManager linearLayoutManager ;
    AppointmentListModel listModel ;
    TextView tv_no_result ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        init(view);
        return view ;
    }
    private void init(View view){
        rv_pending =view.findViewById(R.id.rv_pending);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_pending.setLayoutManager(linearLayoutManager);

         pendingAdapter = new MyUpcommingAdapter(getContext(), new MyUpcommingAdapter.OnItemClickListener() {
            @Override
            public void onClick(BookedAppointment jobs, int pos) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AppointmentDetailsFragment appointmentDetailsFragment =  new AppointmentDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("BOOK_ID",jobs.getId()+"" );
                appointmentDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container,appointmentDetailsFragment);
                transaction.addToBackStack("HomeFragment");
                transaction.commit();
            }
        });
        rv_pending.setAdapter(pendingAdapter);
        dProgressbar = new DProgressbar(getContext());
        getPendingAppointments();
    }

    private void getPendingAppointments(){
        dProgressbar.show();

        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_PENDING, new GetApiHandler.OnClickListener() {
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
            public void onError() {
                dProgressbar.dismiss();
                if(pendingAdapter.getItemCount()<=0)
                    tv_no_result.setVisibility(View.VISIBLE);
            }
        }) ;
        apiHandler.execute();
    }

    @Override
    public void onClick(View v) {
    //    switch (v.getId())
//        {
//            case R.id.cv_item:
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new AppointmentDetailsFragment());
//                transaction.addToBackStack("MyReviewFragment");
//                transaction.commit();
//                break;
  //      }
    }
}