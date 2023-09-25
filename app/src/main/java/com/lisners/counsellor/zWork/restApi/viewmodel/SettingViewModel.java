package com.lisners.counsellor.zWork.restApi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;
import com.lisners.counsellor.zWork.restApi.repo.ApiSRepo;


public class SettingViewModel extends AndroidViewModel {
    private ApiSRepo apiSRepo;


    public SettingViewModel(@NonNull Application application) {
        super(application);
        this.apiSRepo = new ApiSRepo(application);
    }


    public LiveData<BasePojo<SettingPojo>> getSetting(String slug) {

        return apiSRepo.getSetting(slug);
    }

    public LiveData<BasePojo<User>> getProfile() {

        return apiSRepo.getProfile();
    }
}
