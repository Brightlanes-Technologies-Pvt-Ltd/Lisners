package com.lisners.counsellor.zWork.restApi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.pojo.Withdrawal;
import com.lisners.counsellor.zWork.restApi.pojo.walletHistory.WalletHistoryPojo;
import com.lisners.counsellor.zWork.restApi.repo.ApiSRepo;

import java.util.Map;

public class DataViewModel extends AndroidViewModel {
    private final ApiSRepo apiSRepo;


    public DataViewModel(@NonNull Application application) {
        super(application);
        this.apiSRepo = new ApiSRepo(application);
    }



    public LiveData<BasePojo<WalletHistoryPojo>> getWalletHistory(int page) {

        return apiSRepo.getWalletHistory(page);
    }


    public LiveData<BasePojo<Withdrawal>> withdrawRequest(Map<String, String> params) {
        return apiSRepo.withdrawRequest(params);
    }




  }
