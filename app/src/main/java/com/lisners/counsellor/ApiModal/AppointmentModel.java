package com.lisners.counsellor.ApiModal;

public class AppointmentModel {
    Long id , appointment_id;
    String name , title , email , profile_image , dob , gender , mobile_no , newsletter_notify , push_notify , start_time , end_time;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }
}
