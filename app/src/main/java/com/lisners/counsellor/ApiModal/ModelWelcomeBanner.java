package com.lisners.counsellor.ApiModal;

public class ModelWelcomeBanner {
    int logo;
    String title, des;

    public ModelWelcomeBanner(int logo, String title, String des) {
        this.logo = logo;
        this.title = title;
        this.des = des;
    }

    public int getLogo() {
        return logo;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }
}
