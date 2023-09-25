
package com.lisners.counsellor.zWork.restApi.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lisners.counsellor.zWork.restApi.pojo.walletHistory.Transaction;

public class Withdrawal {

    @SerializedName("wallet")
    @Expose
    private String wallet;

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
