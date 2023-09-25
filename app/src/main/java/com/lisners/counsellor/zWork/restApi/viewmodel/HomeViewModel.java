package com.lisners.counsellor.zWork.restApi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.pojo.DashboardPojo;
import com.lisners.counsellor.zWork.restApi.repo.ApiSRepo;


public class HomeViewModel extends AndroidViewModel {
    private ApiSRepo apiSRepo;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.apiSRepo = new ApiSRepo(application);
    }




    public LiveData<BasePojo<User>> getProfile() {
        return apiSRepo.getProfile();
    }

    public LiveData<BasePojo<AppointmentListModel>> getPendingAppointments() {
        return apiSRepo.getPendingAppointments();
    }

    public LiveData<BasePojo<DashboardPojo>> getDashboard() {
        return apiSRepo.getDashboard();
    }

}
