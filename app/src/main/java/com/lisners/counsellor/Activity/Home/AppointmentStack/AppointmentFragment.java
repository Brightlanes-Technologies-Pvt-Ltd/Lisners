package com.lisners.counsellor.Activity.Home.AppointmentStack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.AppointmentViewPageAdapter;
import com.lisners.counsellor.R;

import java.util.ArrayList;


public class AppointmentFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    PendingFragment pendingFragment;
    CompleteFragment completeFragment;
    ArrayList<Fragment> fragments;
    ImageButton btn_header_left ;
    AppointmentViewPageAdapter viewPagerAdapter;
    ViewPager view_paper;
    TabLayout tab_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        init(view,savedInstanceState);

        return view;
    }

    private void init(View view, Bundle savedInstanceState) {
        tvHeader = view.findViewById(R.id.tvHeader);
        tvHeader.setText("My Appointments");
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);
        btn_header_left.setOnClickListener(this);
        pendingFragment = new PendingFragment();
        completeFragment = new CompleteFragment();
        view_paper = view.findViewById(R.id.view_paper);
        tab_layout = view.findViewById(R.id.tab_layout_profile);
        fragments = new ArrayList<>();
        fragments.add(pendingFragment);
        fragments.add(completeFragment);
        viewPagerAdapter = new AppointmentViewPageAdapter(getChildFragmentManager(), fragments);
        view_paper.setAdapter(viewPagerAdapter);
        tab_layout.setupWithViewPager(view_paper);



        Bundle bundle = getArguments();
        if(bundle !=null){
            assert bundle != null;
            view_paper.setCurrentItem(1);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
        }
    }
}