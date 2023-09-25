package com.lisners.counsellor.ApiModal;

public class NotificationModel {
    int user ;
    String title , message , url ,type ;
    NotiInfoModel data ;
    public int getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public NotiInfoModel getData() {
        return data;
    }
}
