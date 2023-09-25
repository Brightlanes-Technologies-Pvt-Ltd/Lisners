
package com.lisners.counsellor.zWork.restApi.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lisners.counsellor.GlobalData;

public class DashboardPojo {

    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("patients")
    @Expose
    private String patients;

    @SerializedName("pending_interview")
    @Expose
    private String pending_interview;

    @SerializedName("total_interview")
    @Expose
    private String total_interview;

    @SerializedName("avg_rating")
    @Expose
    private String avg_rating;

    @SerializedName("call_sec")
    @Expose
    private String call_sec;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("total_review")
    @Expose
    private String total_review;

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getPending_interview() {
        return pending_interview;
    }

    public void setPending_interview(String pending_interview) {
        this.pending_interview = pending_interview;
    }

    public String getTotal_interview() {
        return total_interview;
    }

    public void setTotal_interview(String total_interview) {
        this.total_interview = total_interview;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getCall_sec() {
        return call_sec;
    }

    public void setCall_sec(String call_sec) {
        this.call_sec = call_sec;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal_review() {
        return total_review;
    }

    public void setTotal_review(String total_review) {
        this.total_review = total_review;
    }
}
