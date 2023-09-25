package com.lisners.counsellor.zWork.restApi.api;

import com.google.gson.JsonObject;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.pojo.DashboardPojo;
import com.lisners.counsellor.zWork.restApi.pojo.ProfessionDatum;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;
import com.lisners.counsellor.zWork.restApi.pojo.Withdrawal;
import com.lisners.counsellor.zWork.restApi.pojo.walletHistory.WalletHistoryPojo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiS {






    @Multipart
    @POST("profile-image-update")
    Call<BasePojo<User>> imageUpdate(@Part MultipartBody.Part file);

    @GET("get-transaction-list")
    Call<BasePojo<WalletHistoryPojo>> getWalletHistory(@Query("page") int page);

    @FormUrlEncoded
    @POST("withdraw-requests")
    Call<BasePojo<Withdrawal>> withdrawRequest(@FieldMap Map<String, String> params);



    @GET("get-profile")
    Call<BasePojo<User>> getProfile();


    @GET("get-pending-appointments")
    Call<BasePojo<AppointmentListModel>> getPendingAppointments();


    @GET("dashboard")
    Call<BasePojo<DashboardPojo>> getDashboard();

    @FormUrlEncoded
    @POST("https://admin.lisners.com/api/v1/setting")
    Call<BasePojo<SettingPojo>> getSetting(@Field("slug") String slug);

    @GET("https://admin.lisners.com/api/v1/professions")
    Call<BasePojo<List<ProfessionDatum>>> getProfession();





}
