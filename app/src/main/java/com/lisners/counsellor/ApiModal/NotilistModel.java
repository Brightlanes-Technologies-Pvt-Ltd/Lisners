package com.lisners.counsellor.ApiModal;

import java.util.ArrayList;

public class NotilistModel {
    int total , count , per_page , current_page , last_page=1 ;
     ArrayList<NotiInfoModel> data ;

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public int getPer_page() {
        return per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public ArrayList<NotiInfoModel> getData() {
        return data;
    }

    public void setData(ArrayList<NotiInfoModel> data){
        this.data = data ;
    }
}
