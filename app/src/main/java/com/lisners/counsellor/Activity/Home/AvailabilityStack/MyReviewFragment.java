package com.lisners.counsellor.Activity.Home.AvailabilityStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lisners.counsellor.Adapters.MyAppointmentAdapter;
import com.lisners.counsellor.Adapters.ReviewAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class MyReviewFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    ImageButton btn_header_left;
    ReviewAdapter pendingAdapter ;
    LinearLayoutManager linearLayoutManager ;
    AppointmentListModel listModel ;
    RecyclerView rv_complete ;
    DProgressbar dProgressbar;
    TextView tv_no_result ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_review, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        btn_header_left.setOnClickListener(this);
        tvHeader.setText("My Reviews");

        rv_complete =view.findViewById(R.id.rv_complete);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_complete.setLayoutManager(linearLayoutManager);
        pendingAdapter = new ReviewAdapter(getContext(), new ReviewAdapter.OnItemClickListener() {
            @Override
            public void onClick(BookedAppointment jobs, int pos) {
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                AddPrescriptionFragment appointmentDetailsFragment =  new AddPrescriptionFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("BOOK_ID",jobs.getId()+"" );
//                bundle.putString("HIDE", "HIDE" );
//                appointmentDetailsFragment.setArguments(bundle);
//                transaction.replace(R.id.fragment_container,appointmentDetailsFragment);
//                transaction.addToBackStack("PatientDetailsFragment");
//                transaction.commit();
            }
        });
        rv_complete.setAdapter(pendingAdapter);
        dProgressbar = new DProgressbar(getContext());
        getReview();
    }

    private void getReview(){
        dProgressbar.show();
        GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_REVIEW, new GetApiHandler.OnClickListener() {
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
                if(pendingAdapter!=null && pendingAdapter.getItemCount()<=0)
                    tv_no_result.setVisibility(View.VISIBLE);

            }
        });
        apiHandler.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
        }
    }
}