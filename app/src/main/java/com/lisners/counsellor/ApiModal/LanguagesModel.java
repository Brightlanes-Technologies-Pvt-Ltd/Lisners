package com.lisners.counsellor.ApiModal;

public class LanguagesModel {
    int id , active  ;
    boolean isCheck ;
    String title , counselors_count ;
    PivotModel pivot ;

    public LanguagesModel(int id, boolean isCheck, String title) {
        this.id = id;
        this.isCheck = isCheck;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getActive() {
        return active;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean ck) {
         this.isCheck =ck;
    }

    public String getTitle() {
        return title;
    }

    public String getCounselors_count() {
        return counselors_count;
    }

    public PivotModel getPivot() {
        return pivot;
    }
}
