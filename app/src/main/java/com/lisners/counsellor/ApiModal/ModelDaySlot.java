package com.lisners.counsellor.ApiModal;

import java.util.ArrayList;

public class ModelDaySlot {
    long id ,  user_id , week_day_id;
    String    day ;
    ArrayList<ModelTimeSlot> time_slot ;

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public long getWeek_day_id() {
        return week_day_id;
    }

    public String getDay() {
        return day;
    }

    public ArrayList<ModelTimeSlot> getTime_slot() {
        return time_slot;
    }
}
