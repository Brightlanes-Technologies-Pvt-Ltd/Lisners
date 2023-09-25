package com.lisners.counsellor.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.R;

public class DTimePicker {

    final Dialog dialog;
    TimePicker picker;
    Button btnGet;
    TextView tvw;
    TimeListener listener ;
    public interface  TimeListener {
        void onClick(int hour,int minute );
    }


    public DTimePicker(Context context , TimeListener timeListener) {
        listener = timeListener ;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_time_picker);
        tvw = (TextView) dialog.findViewById(R.id.textView1);
        picker = (TimePicker) dialog.findViewById(R.id.timePicker1);
        picker.setIs24HourView(false);

        btnGet = (Button) dialog.findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;

                if (Build.VERSION.SDK_INT >= 23) {
                    hour = picker.getHour();
                    minute = picker.getMinute();
                } else {
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }

                listener.onClick(hour,minute);
                dialog.dismiss();
            }
        });
       // setTimePickerInterval(picker);
        dialog.show();
    }

//    private void setTimePickerInterval(TimePicker timePicker) {
//        try {
//            int TIME_PICKER_INTERVAL = 15;
//            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
//                    "minute", "id", "android"));
//            minutePicker.setMinValue(0);
//            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
//            List<String> displayedValues = new ArrayList<String>();
//            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//                displayedValues.add(  i+"");
//            }
//            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
//        } catch (Exception e) {
//            Log.e("TAG", "Exception: " + e);
//        }
//    }
}
