package com.lisners.counsellor.zWork.utils.aModel;

public class CallData {
    String bookAppointmentId;
    String counsellorId;
    String patientId;

    public CallData(String bookAppointmentId, String counsellorId, String patientId) {
        this.bookAppointmentId = bookAppointmentId;
        this.counsellorId = counsellorId;
        this.patientId = patientId;
    }

    public String getBookAppointmentId() {
        return bookAppointmentId;
    }

    public void setBookAppointmentId(String bookAppointmentId) {
        this.bookAppointmentId = bookAppointmentId;
    }

    public String getCounsellorId() {
        return counsellorId;
    }

    public void setCounsellorId(String counsellorId) {
        this.counsellorId = counsellorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}

