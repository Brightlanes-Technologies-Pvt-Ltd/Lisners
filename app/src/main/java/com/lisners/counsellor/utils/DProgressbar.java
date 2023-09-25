package com.lisners.counsellor.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.lisners.counsellor.R;

public class DProgressbar {
  final  Dialog progressDialog ;
    public DProgressbar(Context context) {
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_api_loading);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
    }

    public void show(){
        progressDialog.show();
    }

    public void dismiss(){
        progressDialog.dismiss();
    }

}
