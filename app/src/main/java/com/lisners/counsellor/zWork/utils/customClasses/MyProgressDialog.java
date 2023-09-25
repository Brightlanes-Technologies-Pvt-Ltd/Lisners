package com.lisners.counsellor.zWork.utils.customClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.lisners.counsellor.R;


public class MyProgressDialog {

    final  Dialog dialog ;

    public MyProgressDialog(Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_api_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

    }

    public void show() {

        if (!dialog.isShowing() && dialog != null) {
            dialog.show();

        }

    }

    public void dismiss() {

        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }


}