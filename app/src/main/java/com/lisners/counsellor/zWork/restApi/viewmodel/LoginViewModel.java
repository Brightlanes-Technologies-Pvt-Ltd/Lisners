package com.lisners.counsellor.zWork.restApi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.repo.ApiSRepo;

import java.io.File;
import java.util.Map;


public class LoginViewModel extends AndroidViewModel {
    private ApiSRepo apiSRepo;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.apiSRepo = new ApiSRepo(application);
    }



    public LiveData<BasePojo<User>> imageUpdate(File imageFile) {
        return apiSRepo.imageUpdate(imageFile);
    }


}
