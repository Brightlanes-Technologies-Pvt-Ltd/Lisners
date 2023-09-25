package com.lisners.counsellor.Activity.Home.AppointmentStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.AddPrescriptionFragment;
import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.CounselorProfile;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class AppointmentDetailsFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader, tv_date, tv_profile_name, tv_spacilization, tv_notes, tv_short_name, tv_time_range;
    ImageButton btn_header_left;
    Button btn_accept, btn_reject;
    BookedAppointment bookApp;
    ImageView iv_image;
    Random random;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        tv_date = view.findViewById(R.id.tv_date);
        iv_image = view.findViewById(R.id.iv_profile);
        tv_profile_name = view.findViewById(R.id.tv_profile_name);
        tv_spacilization = view.findViewById(R.id.tv_spacilization);
        tv_short_name = view.findViewById(R.id.tv_short_name);
        tv_time_range = view.findViewById(R.id.tv_time_range);
        tv_notes = view.findViewById(R.id.tv_notes);
        btn_header_left.setOnClickListener(this);
        tvHeader.setText("Appointment details");
        btn_accept = view.findViewById(R.id.btn_accept);
        btn_reject = view.findViewById(R.id.btn_reject);
        btn_accept.setOnClickListener(this);
        btn_reject.setOnClickListener(this);
        random = new Random(121232322);
        getAppDetails();
    }

    private void getAppDetails() {
        Bundle bundle = this.getArguments();
        String bookID = bundle.getString("BOOK_ID", "NO");
        if (!bookID.equals("NO")) {
            GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.SET_BOOK_APPOINTMENT + "/" + bookID, new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if (jsonObject.has("status") && jsonObject.has("data")) {
                        bookApp = new Gson().fromJson(jsonObject.getString("data"), BookedAppointment.class);
                        ArrayList<SpacializationMedel> spacializationMedels = bookApp.getSpecialization();
                        User user = bookApp.getUser();
                        AppointmentModel appmodel = bookApp.getAppointment_detail();

                        if (bookApp.getDate() != null) {
                            tv_date.setText(DateUtil.dateFormatter(bookApp.getDate(), "dd-MM-yyyy", "dd MMM yyyy"));
                        } else {
                            tv_date.setText("");
                        }
                        if (appmodel != null) {
                            tv_time_range.setText(String.format("%s - %s", DateUtil.dateFormatter(appmodel.getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(appmodel.getEnd_time(), "HH:mm:ss", "hh:mm a")));
                        } else {
                            tv_time_range.setText("");
                        }


                        tv_profile_name.setText(UtilsFunctions.splitCamelCase(user.getName()));

                        if (user.getProfile_image() != null ) {
                            UtilsFunctions.SetLOGO(getContext(), user.getProfile_image(), iv_image);
                            tv_short_name.setVisibility(View.GONE);
                        } else {
                            tv_short_name.setText(UtilsFunctions.getFistLastChar(user.getName()));
                            tv_short_name.setVisibility(View.VISIBLE);
                        }


                        // UtilsFunctions.SetLOGO(getContext(),user.getProfile_image(),iv_image);
                        tv_notes.setText(bookApp.getAdd_notes());
                        /*if (spacializationMedels != null) {
                            for (SpacializationMedel medel : spacializationMedels) {
                                tv_spacilization.setText(medel.getTitle());
                            }
                        }*/

                        tv_spacilization.setText(bookApp.getSpecialization_name());

                    }
                }

                @Override
                public void onError() {

                }
            });
            apiHandler.execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_accept:
                Bundle bundle = this.getArguments();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AddPrescriptionFragment fragment = new AddPrescriptionFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("AppointmentStack");
                transaction.commit();
                break;
            case R.id.btn_reject:
                getFragmentManager().popBackStack();
                break;
        }
    }
}