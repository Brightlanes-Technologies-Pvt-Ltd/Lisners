package com.lisners.counsellor.ApiModal;

public class CounselorProfile {
    int id, user_id ;
    String clinic_name , voice_call , video_call, description ;

    int profession_id;
    Profession profession;


    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public String getVoice_call() {
        return voice_call;
    }

    public String getVideo_call() {
        return video_call;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public void setVoice_call(String voice_call) {
        this.voice_call = voice_call;
    }

    public void setVideo_call(String video_call) {
        this.video_call = video_call;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProfession_id() {
        return profession_id;
    }

    public void setProfession_id(int profession_id) {
        this.profession_id = profession_id;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

}
