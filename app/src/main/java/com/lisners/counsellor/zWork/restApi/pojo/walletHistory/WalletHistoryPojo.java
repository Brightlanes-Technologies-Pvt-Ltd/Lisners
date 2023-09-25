
package com.lisners.counsellor.zWork.restApi.pojo.walletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletHistoryPojo {

    @SerializedName("wallet")
    @Expose
    private  double  wallet;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}
