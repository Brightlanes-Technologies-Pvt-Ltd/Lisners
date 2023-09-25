
package com.lisners.counsellor.zWork.restApi.pojo.walletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaction {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("per_page")
    @Expose
    private int perPage;
    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("last_page")
    @Expose
    private int lastPage;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

}
