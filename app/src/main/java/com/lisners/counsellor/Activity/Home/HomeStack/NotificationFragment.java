package com.lisners.counsellor.Activity.Home.HomeStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.AppointmentStack.AppointmentFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyReviewFragment;
import com.lisners.counsellor.Adapters.NotiListAdaptor;
import com.lisners.counsellor.ApiModal.NotiInfoModel;
import com.lisners.counsellor.ApiModal.NotilistModel;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationFragment extends Fragment implements View.OnClickListener {

    RecyclerView rv_notification ;
    ImageButton btn_home_header_left ;
    NotiListAdaptor notiListAdaptor ;
    TextView tv_no_result , tvHeader ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        init(view);
        return view ;
    }

    public void init(View view){

        tvHeader = view.findViewById(R.id.tvHeader);
        tvHeader.setText("Notifications");
        rv_notification = view.findViewById(R.id.rv_notification);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        btn_home_header_left = view.findViewById(R.id.btn_header_left);
        btn_home_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        btn_home_header_left.setOnClickListener(this);
        rv_notification.setLayoutManager(new LinearLayoutManager(getContext()));
        notiListAdaptor = new NotiListAdaptor(getContext(), new NotiListAdaptor.OnItemClickListener() {
            @Override
            public void onClick(NotiInfoModel jobs, int pos) {
                if(jobs.getType().contains("AppointmentNotify"))
                    callNewScreen(new AppointmentFragment());
                else if(jobs.getType().contains("PatientReviewNotify"))
                    callNewScreen(new MyReviewFragment());

            }
        });
        rv_notification.setAdapter(notiListAdaptor);

        getApi();
    }

    private void callNewScreen(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack("HomeFragment");
        transaction.commit();
    }

    public void getApi(){
        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_NOTIFICATION, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                Log.e("jsonObject",new Gson().toJson(jsonObject));
                if (jsonObject.has("status") && jsonObject.has("data")){
                    NotilistModel notilistModel =  new Gson().fromJson(jsonObject.getString("data"), NotilistModel.class);
                    Log.e("notilistModel",new Gson().toJson(notilistModel));

                    if(notilistModel.getData()==null && notilistModel.getTotal()<=0 )
                        tv_no_result.setVisibility(View.VISIBLE);
                    else {
                        tv_no_result.setVisibility(View.GONE);
                        notiListAdaptor.updateList(notilistModel.getData());
                    }
                }else
                {
                    if(notiListAdaptor.getItemCount()<=0)
                        tv_no_result.setVisibility(View.VISIBLE);
                    else
                        tv_no_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                if(notiListAdaptor.getItemCount()<=0)
                    tv_no_result.setVisibility(View.VISIBLE);
                else
                    tv_no_result.setVisibility(View.GONE);
            }
        });
        apiHandler.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
        }
    }
}