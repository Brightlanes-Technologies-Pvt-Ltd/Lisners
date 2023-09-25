package com.lisners.counsellor.ApiModal;

public class NotiInfoModel {
    int   notifiable_id;
    String title ,id, notifiable_type  ,type ,created_at_format ;

   NotificationModel data ;

    public String getId() {
        return id;
    }

    public int getNotifiable_id() {
        return notifiable_id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotifiable_type() {
        return notifiable_type;
    }

    public String getType() {
        return type;
    }

    public String getDateFormte() {
        return created_at_format;
    }
    public NotificationModel getData() {
        return data;
    }
}
