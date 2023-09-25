package com.lisners.counsellor.ApiModal;

import java.util.ArrayList;

public class BookedAppointment {
    int id , appointment_detail_id , status , user_id , counselor_id ;
    String date;
    String call_type;

    String status_label;
    String specialization_id;
    String specialization_name;
    String call_rate;
    String chenal_code;
    String add_notes;
    String prescriprion;
    String total_amount;
    String call_date;
    String extra_charges;
    String call_time;

    public String getShare_amount() {
        return share_amount;
    }

    public void setShare_amount(String share_amount) {
        this.share_amount = share_amount;
    }

    String share_amount;
    String rating;
    String comment;
    User user ;
    User counselor ;
    AppointmentModel appointment_detail ;
    ArrayList<SpacializationMedel> specialization ;

    public ArrayList<SpacializationMedel> getSpecialization() {
        return specialization;
    }

    public String getRating() {
        if(rating!=null)
            return rating  ;
        return "0";
    }

    public int getId() {
        return id;
    }
    public String getComment() {
        return comment;
    }
    public String getChenal_code() {
        return chenal_code;
    }

    public int getAppointment_detail_id() {
        return appointment_detail_id;
    }

    public int getStatus() {
        return status;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCounselor_id() {
        return counselor_id;
    }

    public String getDate() {
        return date;
    }

    public String getCall_type() {
        return call_type;
    }

    public String getCall_rate() {
        return call_rate;
    }

    public String getAdd_notes() {
        return add_notes;
    }

    public String getPrescriprion() {
        return prescriprion;
    }

    public String getCall_date() {
        return call_date;
    }

    public String getCall_time() {
        return call_time;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public User getUser() {
        return user;
    }

    public User getCounselor() {
        return counselor;
    }

    public AppointmentModel getAppointment_detail() {
        return appointment_detail;
    }

    public String getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(String specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getSpecialization_name() {
        return specialization_name;
    }

    public void setSpecialization_name(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public String getStatus_label() {
        return status_label;
    }

    public void setStatus_label(String status_label) {
        this.status_label = status_label;
    }
}
