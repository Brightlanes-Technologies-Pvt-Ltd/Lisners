package com.lisners.counsellor.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class AppointmentViewPageAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;


    public AppointmentViewPageAdapter(FragmentManager childFragmentManager, ArrayList<Fragment> frag) {
        super(childFragmentManager);
        this.fragments = frag;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Pending";
        else if (position == 1)
            title = "Complete";
//        else if (position == 2)
//            title = "Settings";
        return title;
    }
}
