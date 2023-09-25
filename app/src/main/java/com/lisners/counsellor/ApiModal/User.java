package com.lisners.counsellor.ApiModal;

import java.util.ArrayList;

public class User {
    Long id , role_id ;


    String deleted_at;
    String name , email , mobile_no , city , address , clinic_name , profile_image , gender , active , is_notify ;
    CounselorProfile counselor_profile ;
    ArrayList<SpacializationMedel> specialization ;
    ArrayList<LanguagesModel> languages ;
    int is_online;
    int is_approved;



    int is_profile_complete;
    public Long getId() {
        return id;
    }

    public int getIs_notify() {
        if(is_notify!=null)
         return  Integer.valueOf(is_notify);
         return 0;
    }

    public int getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(int is_approved) {
        this.is_approved = is_approved;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public Long getRole_id() {
        return role_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getGender() {
        return gender;
    }

    public String getActive() {
        return active;
    }

    public CounselorProfile getCounselor_profile() {
        return counselor_profile;
    }

    public ArrayList<SpacializationMedel> getSpecialization() {
        return specialization;
    }

    public ArrayList<LanguagesModel> getLanguages() {
        return languages;
    }

    public void setCounselorProfile(CounselorProfile specialization) {
        this.counselor_profile = specialization;
    }

    public void setLanguages(ArrayList<LanguagesModel> languages) {
        this.languages = languages;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getIs_profile_complete() {
        return is_profile_complete;
    }

    public void setIs_profile_complete(int is_profile_complete) {
        this.is_profile_complete = is_profile_complete;
    }
}
