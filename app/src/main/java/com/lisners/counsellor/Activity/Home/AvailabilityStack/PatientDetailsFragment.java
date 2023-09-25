package com.lisners.counsellor.Activity.Home.AvailabilityStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lisners.counsellor.Adapters.PatientHistoryAdapter;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientDetailsFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader , tv_name , tv_spacilization , tv_no_result;
    ImageButton btn_header_left;
    ImageView iv_image ;
    PatientHistoryAdapter historyAdapter ;
    RecyclerView rv_history ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_details, container, false);
        init(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        btn_header_left.setOnClickListener(this);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        rv_history = view.findViewById(R.id.rv_history);
        iv_image = view.findViewById(R.id.iv_image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_spacilization = view.findViewById(R.id.tv_spacilization);
        tvHeader.setText("Patient Details");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_history.setLayoutManager(linearLayoutManager);
        historyAdapter = new PatientHistoryAdapter(getContext(), new PatientHistoryAdapter.OnItemClickListener() {
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
        rv_history.setAdapter(historyAdapter);
        getProfile();
    }

   public void getProfile(){
       if(GlobalData.my_patients_details!=null){
           User user = GlobalData.my_patients_details.getUser();
           ArrayList<SpacializationMedel> spacializationMedels = GlobalData.my_patients_details.getSpecialization();
           tv_name.setText(UtilsFunctions.splitCamelCase(user.getName()));
           UtilsFunctions.SetLOGO(getContext(),user.getProfile_image(),iv_image);
          /* if(spacializationMedels!=null) {
               for (SpacializationMedel medel : spacializationMedels)
                   tv_spacilization.setText(medel.getTitle());
           }*/
           tv_spacilization.setText(GlobalData.my_patients_details.getSpecialization_name());
           getHistory();
       }

   }

   public void getHistory(){
       GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_PATIENTS_DETAILS+"/"+ GlobalData.my_patients_details.getUser_id(), new GetApiHandler.OnClickListener() {
           @Override
           public void onResponse(JSONObject jsonObject) throws JSONException {
               if(jsonObject.has("status") && jsonObject.has("data")){
                   AppointmentListModel listModel = new Gson().fromJson(jsonObject.getString("data"),AppointmentListModel.class);
                   historyAdapter.updateList(listModel.getData());
               }

           }

           @Override
           public void onError() {

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