package com.lisners.counsellor.ApiModal;

public class ModelTimeSlot {
    long id , appoinment_id ;
   String  start_time , end_time ;

    public ModelTimeSlot(long id, long appoinment_id, String start_time, String end_time) {
        this.id = id;
        this.appoinment_id = appoinment_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public long getId() {
        return id;
    }

    public long getAppoinment_id() {
        return appoinment_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }
}
