package com.lisners.counsellor.Activity.Home.AppointmentStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lisners.counsellor.R;

public class MyChargesFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    ImageButton btn_header_left;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_charges, container, false);
        // Inflate the layout for this fragment
        init(view);
        return view;
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        btn_header_left.setOnClickListener(this);
        tvHeader.setText("Call & Video");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                break;
        }
    }
}