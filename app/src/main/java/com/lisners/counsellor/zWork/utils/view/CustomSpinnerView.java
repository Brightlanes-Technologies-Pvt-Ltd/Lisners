package com.lisners.counsellor.zWork.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;


import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ViewSpinnerBinding;

import java.util.List;

public class CustomSpinnerView extends ConstraintLayout {
    private ViewSpinnerBinding binding;


    public ViewSpinnerBinding getBinding() {
        return this.binding;
    }

    public CustomSpinnerView(Context context) {
        super(context);

        init();
    }

    public CustomSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.binding = (ViewSpinnerBinding) DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.view_spinner, this, true);
    }

    public void setSpinnerData(List<String> spn_list) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, spn_list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        
        binding.spnMain.setAdapter(dataAdapter);


    }

    public void resetSpinnerData(List<String> data) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, data);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

       /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, data);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
*/
        binding.spnMain.setAdapter(dataAdapter);
    }


    AdapterView.OnItemSelectedListener listener;

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
        binding.spnMain.setOnItemSelectedListener(listener);

    }

}

