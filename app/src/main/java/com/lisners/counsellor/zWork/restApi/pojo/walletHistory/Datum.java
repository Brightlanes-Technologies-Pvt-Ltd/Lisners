
package com.lisners.counsellor.zWork.restApi.pojo.walletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_from")
    @Expose
    private String userFrom;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("credit")
    @Expose
    private double credit;
    @SerializedName("debit")
    @Expose
    private double debit;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("is_referral")
    @Expose
    private Object isReferral;
    @SerializedName("is_withdrawl")
    @Expose
    private Object isWithdrawl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getIsReferral() {
        return isReferral;
    }

    public void setIsReferral(Object isReferral) {
        this.isReferral = isReferral;
    }

    public Object getIsWithdrawl() {
        return isWithdrawl;
    }

    public void setIsWithdrawl(Object isWithdrawl) {
        this.isWithdrawl = isWithdrawl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
