package com.lisners.counsellor.zWork.restApi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.pojo.ProfessionDatum;
import com.lisners.counsellor.zWork.restApi.pojo.Withdrawal;
import com.lisners.counsellor.zWork.restApi.pojo.walletHistory.WalletHistoryPojo;
import com.lisners.counsellor.zWork.restApi.repo.ApiSRepo;

import java.util.List;
import java.util.Map;

public class SignUpViewModel extends AndroidViewModel {
    private final ApiSRepo apiSRepo;


    public SignUpViewModel(@NonNull Application application) {
        super(application);
        this.apiSRepo = new ApiSRepo(application);
    }


    public LiveData<BasePojo<List<ProfessionDatum>>> getProfession() {
        return apiSRepo.getProfession();
    }

}
