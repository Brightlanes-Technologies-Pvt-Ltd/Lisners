package com.lisners.counsellor.zWork.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class BaseActivity<T extends ViewDataBinding> extends SuperActivity {
    private T binding;


    public abstract int getLayoutResourceId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(this);
        this.binding = DataBindingUtil.inflate(mInflater, getLayoutResourceId(), getBaseBinding().layoutContainer, true);
    }

    public T getBinding() {
        return this.binding;
    }

    public void setBinding(T binding) {
        this.binding = binding;
    }




}
