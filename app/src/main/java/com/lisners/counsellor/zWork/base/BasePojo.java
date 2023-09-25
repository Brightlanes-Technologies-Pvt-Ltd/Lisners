package com.lisners.counsellor.zWork.base;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasePojo<T> {


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JsonObject getErrors() {
        return errors;
    }

    public void setErrors(JsonObject errors) {
        this.errors = errors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public @interface ErrorCode {
        public static final int NO_INTERNET = 5;
        public static final int SOME_ERROR_OCCURRED = 6;
        public static final int UNAUTHORIZED = 7;
    }

    public @interface ErrorMessage {
        public static final String NO_INTERNET = "Please make sure you're connected to the internet";
        public static final String SOME_ERROR_OCCURRED = "Some Error Occurred";
        public static final String UNAUTHORIZED = "UnAuthorized";
    }



    private int errorStatus ;



    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("errors")
    @Expose
    private JsonObject errors;

    @SerializedName("data")
    @Expose
    private T data;


    @SerializedName("token")
    @Expose
    private String token;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(int errorStatus) {
        this.errorStatus = errorStatus;
    }

}




