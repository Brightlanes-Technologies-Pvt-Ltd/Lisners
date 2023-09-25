package com.lisners.counsellor.zWork.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ActivitySuperBinding;
import com.lisners.counsellor.zWork.utils.config.MainApplication;
import com.lisners.counsellor.zWork.utils.customClasses.MyProgressDialog;


public abstract class SuperActivity extends AppCompatActivity {

    public abstract Context getContext();


    private ActivitySuperBinding baseBinding;
    private MyProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseBinding = (ActivitySuperBinding) DataBindingUtil.setContentView(this, R.layout.activity_super);
        progressDialog = new MyProgressDialog(this);
        
        setSupportActionBar(this.baseBinding.toolbar);



      /*  this.baseBinding.includedErrorLayout.tvErrorRefresh.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SuperActivity.this.refreshClicked();
            }
        });*/


    }


    public void refreshClicked() {
    }


    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }


    public void initBasicUi(boolean isShowMainLayout, boolean isToolBarVisibility, boolean isBackShow, String toolbarTitle) {
        if (isShowMainLayout)
            showMainLayout();
        else
            hideMainLayout();


        setToolbarVisibility(isToolBarVisibility);
        setBackArrowVisibility(isBackShow);
        setToolbarText(toolbarTitle);
    }


    public void showProgressDialog() {
        progressDialog.show();
    }

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }


    public void showProgress() {
        if (getBaseBinding() != null) {
            getBaseBinding().progress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (getBaseBinding() != null) {
            getBaseBinding().progress.setVisibility(View.GONE);
        }
    }

    public void showMainLayout() {
        if (getBaseBinding() != null) {
            getBaseBinding().layoutContainer.setVisibility(View.VISIBLE);
        }
    }

    public void hideMainLayout() {
        if (getBaseBinding() != null) {
            getBaseBinding().layoutContainer.setVisibility(View.GONE);
        }
    }


    public void showError(String msg) {
        if (getBaseBinding() != null) {
            getBaseBinding().layoutError.setVisibility(View.VISIBLE);
            getBaseBinding().includedErrorLayout.errorText.setText(msg);
        }
    }

    public void showError(String msg, int image) {
        if (getBaseBinding() != null) {
            getBaseBinding().layoutError.setVisibility(View.VISIBLE);
            getBaseBinding().includedErrorLayout.errorText.setText(msg);
            getBaseBinding().includedErrorLayout.image.setImageResource(image);
        }
    }

    public void hideError() {
        if (getBaseBinding() != null) {
            getBaseBinding().layoutError.setVisibility(View.GONE);
            getBaseBinding().includedErrorLayout.errorText.setText("");
        }
    }


    public ActivitySuperBinding getBaseBinding() {
        return this.baseBinding;
    }

    public void setBaseBinding(ActivitySuperBinding baseBinding) {
        this.baseBinding = baseBinding;
    }


    /*  public void setCustomToolbarText(String toolbarText, float size, String font) {
          this.baseBinding.tvToolbarTitle.setText(toolbarText);
          this.baseBinding.tvToolbarTitle.setTextSize(size);
          this.baseBinding.tvToolbarTitle.(font);
      }*/

    public void setToolbarText(String toolbarText) {
        this.baseBinding.toolbarTitle.setText(toolbarText);
    }

    public void setToolbarVisibility(boolean isVisible) {
        if (!isVisible) {
            this.baseBinding.toolbar.setVisibility(View.GONE);
        } else {
            this.baseBinding.toolbar.setVisibility(View.VISIBLE);
        }
    }

    public void setBackArrowVisibility(boolean isVisible) {
        if (!isVisible) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    public void setToolBar(int backgroundColorResource, int toolbarTextColorResource) {
        this.baseBinding.toolbar.setBackgroundColor(getResources().getColor(backgroundColorResource));
        this.baseBinding.toolbarTitle.setTextColor(getResources().getColor(toolbarTextColorResource));

    }


    public void askPermission(String[] permissionList, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissionList, requestCode);
        }
    }

    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == 0;
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isNetConnected() {
        return MainApplication.get(getContext()).getMerlinsBeard().isConnected();
    }

}
