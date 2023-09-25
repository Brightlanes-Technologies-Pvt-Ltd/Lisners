package com.lisners.counsellor.Activity.call.newA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


import com.lisners.counsellor.Activity.call.newA.callbacks.IEventListener;
import com.lisners.counsellor.zWork.base.SuperActivity;

import java.util.Map;

import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;

public abstract class BaseCallActivity<T extends ViewDataBinding> extends SuperActivity implements IEventListener {
    private T binding;


    public abstract int getLayoutResourceId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        LayoutInflater mInflater = LayoutInflater.from(this);
        this.binding = DataBindingUtil.inflate(mInflater, getLayoutResourceId(), getBaseBinding().layoutContainer, true);
    }

    public T getBinding() {
        return this.binding;
    }

    public void setBinding(T binding) {
        this.binding = binding;
    }


    @Override
    public void onConnectionStateChanged(int status, int reason) {

    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

    }

    @Override
    public void onLocalInvitationReceived(LocalInvitation localInvitation) {

    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation localInvitation, String response) {

    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation localInvitation, String response) {

    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation localInvitation) {

    }

    @Override
    public void onLocalInvitationFailure(LocalInvitation localInvitation, int errorCode) {

    }

    @Override
    public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int errorCode) {
    }

}


