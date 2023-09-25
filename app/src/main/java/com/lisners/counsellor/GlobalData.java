package com.lisners.counsellor;

import android.media.MediaPlayer;

import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.SettingModel;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;

public class GlobalData {
    public static String dashboard_balance ="0" ;
    public static String dashboard_patient ="0" ;
    public static String dashboard_pending_interview ="0" ;
    public static String dashboard_total_interview ="0" ;
    public static String dashboard_appointments ="0" ;
    public static String dashboard_reviews ="0" ;
    public static String dashboard_total_reviews ="0" ;
    public static String dashboard_total_calls ="0" ;
    public static String dashboard_total_review ="0" ;

    public static BookedAppointment my_patients_details ;

    public static BookedAppointment call_bookedAppointment ;

    public static MediaPlayer mediaPlayer ;

    //public static SettingModel setting_model ;
    public static SettingPojo setting_razor_pay_model;
    public static SettingPojo setting_agora_id_model;
}
