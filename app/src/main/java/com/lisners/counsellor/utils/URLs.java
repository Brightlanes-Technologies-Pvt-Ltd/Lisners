package com.lisners.counsellor.utils;

public class URLs {

        public static final String DOMAIN_URL = "https://admin.lisners.com/api/v1" ; // "http://13.235.77.46";
        public static final String ROOT_URL = DOMAIN_URL+"/counselors/";



        //AUTH
        public static final String SEND_LOGIN = ROOT_URL+"login" ;
        public static final String SEND_OTP_SIGN_UP = ROOT_URL+"signup" ;
        public static final String SEND_VERIFY_SIGN_UP = ROOT_URL+"signup-otp" ;
        public static final String SIGNUP_UPDATE = ROOT_URL+"signup-update" ;
        public static final String GET_SPECIALIZATION = DOMAIN_URL+"/specializations" ;
        public static final String GET_LANGUAGES = DOMAIN_URL+"/languages" ;
        public static final String FORGOT_CHECK_MOBILE =ROOT_URL+ "check-mobile" ;
        public static final String FORGOT_VERIFY =ROOT_URL+ "login-with-otp" ;
        public static final String NEW_PASSWORD =ROOT_URL+ "new-password" ;
        public static final String UPDATE_PASSWORD =  ROOT_URL+"update-password" ;
        public static final String SET_CHANGES = ROOT_URL+"charges" ;
        public static final String GET_REVIEW = ROOT_URL+"get-petient-reviews" ;

        //PROFILE
        public static final String GET_PROFILE =  ROOT_URL+"get-profile" ;
        public static final String SET_PROFILE =  ROOT_URL+"profile-image-update" ;
        public static final String PROFILE_UPDATE = ROOT_URL+"profile-update" ;
        public static final String GET_NOTIFICATION = ROOT_URL+"notifications" ;
        public static final String URL_FAQ = DOMAIN_URL+"/faq" ;
        public static final String GET_PAGES =  DOMAIN_URL+"/page-content" ;
        public static final String SET_REPORT_PDF =  ROOT_URL+"download-complete-appointments" ;
        public static final String LOGOUT =  ROOT_URL+"logout" ;

        //Available
        public static final String GET_WEEK_DAYS =  ROOT_URL+"get-week-days" ;
        public static final String GET_AVAILABLE =  ROOT_URL+"get-appointments" ;
        public static final String SET_AVAILABLE =  ROOT_URL+"appointments" ;
        public static final String SET_AVAILABLE_DELETE =  ROOT_URL+"appointment_details" ;
        public static final String SET_BOOK_APPOINTMENT=  ROOT_URL+"get-bookappointment" ;

        // Appointment
        public static final String GET_PENDING=  ROOT_URL+"get-pending-appointments" ;
        public static final String GET_COMPLETE=  ROOT_URL+"get-complete-appointments" ;
        public static final String GET_PATIENTS=  ROOT_URL+"get-petients" ;
        public static final String GET_PATIENTS_DETAILS=  ROOT_URL+"get-petient-details" ;
        public static final String SET_PRESCRIPTION =  ROOT_URL+"prescription/" ;
        public static final String UPDATE_ONLINE_STATUS = ROOT_URL+"update-online-status" ;

        // HOME DASHBOARD
        public static final String GET_DASHBOARD=  ROOT_URL+"dashboard" ;
        public static final String GET_SETTING=  DOMAIN_URL+"/settings" ;


        // PATIENT
        public static final String SET_PATIENTS =  ROOT_URL+"get-petients" ;

        //WALLET
        public static final String GET_TRANSACTION =  ROOT_URL+"get-transaction-list?page=" ;
        public static final String ADD_PAYMENT =  ROOT_URL+"add-payment" ;
        public static final String GET_WALLET =  ROOT_URL+"get-wallet" ;
        public static final String WITHDRAW_REQUEST = ROOT_URL +"withdraw-requests";
}
