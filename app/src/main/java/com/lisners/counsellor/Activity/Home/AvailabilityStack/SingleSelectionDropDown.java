package com.lisners.counsellor.Activity.Home.AvailabilityStack;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.Adapters.MultiCheckAdaptar;
import com.lisners.counsellor.Adapters.SingleSelectionAdaptar;
import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.ApiModal.ModellistItem;
import com.lisners.counsellor.R;

import java.util.ArrayList;

public class SingleSelectionDropDown {

    Dialog dg_industries;
    SingleSelectionAdaptar adaptor ;
    OnItemClickListener onItemClickListener ;

    public interface OnItemClickListener {
        void onClick( ModellistItem selected_spaci);
    }

    public SingleSelectionDropDown(Context context, ArrayList<ModellistItem> industries2, OnItemClickListener listener) {
        onItemClickListener = listener ;
        dg_industries = new Dialog(context);
        dg_industries.setContentView(R.layout.dailogbox_dwondwon);
        final RecyclerView indview = (RecyclerView) dg_industries.findViewById(R.id.dialog_list);
        final TextView title = (TextView) dg_industries.findViewById(R.id.tv_dialogTitle);
        title.setVisibility(View.VISIBLE);
        title.setText("Select Day");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        indview.setLayoutManager(linearLayoutManager);

        adaptor = new SingleSelectionAdaptar(context, industries2, new SingleSelectionAdaptar.OnItemClickListener() {
            @Override
            public void onClick(ModellistItem jobs, int pos, boolean type) {
                onItemClickListener.onClick(jobs);
                dg_industries.dismiss();
            }
        });
        dg_industries.setCancelable(false);
        indview.setAdapter(adaptor);
        dg_industries.show();

        ImageButton close_btn_ind = (ImageButton) dg_industries.findViewById(R.id.btn_dialog_close);
        close_btn_ind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dg_industries.dismiss();
            }
        });

        dg_industries.setCancelable(false);
        dg_industries.show();
    }


}
