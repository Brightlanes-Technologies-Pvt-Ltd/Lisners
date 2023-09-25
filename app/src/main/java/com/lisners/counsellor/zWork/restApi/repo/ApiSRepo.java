package com.lisners.counsellor.zWork.restApi.repo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.gson.Gson;
import com.lisners.counsellor.ApiModal.APIErrorModel;
import com.lisners.counsellor.ApiModal.AppointmentListModel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.restApi.api.ApiS;
import com.lisners.counsellor.zWork.restApi.pojo.DashboardPojo;
import com.lisners.counsellor.zWork.restApi.pojo.ProfessionDatum;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;
import com.lisners.counsellor.zWork.restApi.pojo.Withdrawal;
import com.lisners.counsellor.zWork.restApi.pojo.walletHistory.WalletHistoryPojo;
import com.lisners.counsellor.zWork.utils.ApiErrorUtils;
import com.lisners.counsellor.zWork.utils.config.MainApplication;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiSRepo {
    private final Application mApplication;
    private final ApiS api;


    public ApiSRepo(Application application) {
        this.mApplication = application;
        this.api = MainApplication.get(application).getAppComponent().getApiS();

    }


    public LiveData<BasePojo<User>> imageUpdate(File file) {

        final MutableLiveData<BasePojo<User>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        MultipartBody.Part imageBody;
        if (file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/*"),
                            file
                    );
            imageBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"),
                            ""
                    );
            imageBody = MultipartBody.Part.createFormData("image", "", requestFile);
        }



        Call<BasePojo<User>> call = api.imageUpdate(imageBody);
        call.enqueue(new Callback<BasePojo<User>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<User>> call, @NotNull Response<BasePojo<User>> response) {
                BasePojo<User> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();
                } else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                } else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);
                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<User>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));
            }
        });

        return responseLiveData;
    }



    public LiveData<BasePojo<WalletHistoryPojo>> getWalletHistory(int page) {

        final MutableLiveData<BasePojo<WalletHistoryPojo>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<WalletHistoryPojo>> call = api.getWalletHistory(page);
        call.enqueue(new Callback<BasePojo<WalletHistoryPojo>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<WalletHistoryPojo>> call, @NotNull Response<BasePojo<WalletHistoryPojo>> response) {
                BasePojo<WalletHistoryPojo> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                }
                else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                }
                else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<WalletHistoryPojo>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }





    public LiveData<BasePojo<Withdrawal>> withdrawRequest(Map<String, String> params) {

        final MutableLiveData<BasePojo<Withdrawal>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<Withdrawal>> call = api.withdrawRequest(params);
        call.enqueue(new Callback<BasePojo<Withdrawal>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<Withdrawal>> call, @NotNull Response<BasePojo<Withdrawal>> response) {
                BasePojo<Withdrawal> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                }
                else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                }
                else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<Withdrawal>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }



    public LiveData<BasePojo<SettingPojo>> getSetting(String slug) {

        final MutableLiveData<BasePojo<SettingPojo>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<SettingPojo>> call = api.getSetting(slug);
        call.enqueue(new Callback<BasePojo<SettingPojo>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<SettingPojo>> call, @NotNull Response<BasePojo<SettingPojo>> response) {
                BasePojo<SettingPojo> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                } else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                } else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<SettingPojo>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }



    public LiveData<BasePojo<List<ProfessionDatum>>> getProfession() {

        final MutableLiveData<BasePojo<List<ProfessionDatum>>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<List<ProfessionDatum>>> call = api.getProfession();
        call.enqueue(new Callback<BasePojo<List<ProfessionDatum>>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<List<ProfessionDatum>>> call, @NotNull Response<BasePojo<List<ProfessionDatum>>> response) {
                BasePojo<List<ProfessionDatum>> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                }
                else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                }
                else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<List<ProfessionDatum>>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }


    public LiveData<BasePojo<User>> getProfile() {

        final MutableLiveData<BasePojo<User>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<User>> call = api.getProfile();
        call.enqueue(new Callback<BasePojo<User>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<User>> call, @NotNull Response<BasePojo<User>> response) {
                BasePojo<User> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                }
                else if(response.code() == 401){
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.UNAUTHORIZED, BasePojo.ErrorCode.UNAUTHORIZED);
                }
                else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                } else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<User>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }

    public LiveData<BasePojo<AppointmentListModel>> getPendingAppointments() {

        final MutableLiveData<BasePojo<AppointmentListModel>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<AppointmentListModel>> call = api.getPendingAppointments();
        call.enqueue(new Callback<BasePojo<AppointmentListModel>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<AppointmentListModel>> call, @NotNull Response<BasePojo<AppointmentListModel>> response) {
                BasePojo<AppointmentListModel> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                } else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                } else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<AppointmentListModel>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }


    public LiveData<BasePojo<DashboardPojo>> getDashboard() {

        final MutableLiveData<BasePojo<DashboardPojo>> responseLiveData = new MutableLiveData<>();

        if (!MainApplication.get(mApplication).getMerlinsBeard().isConnected()) {
            responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.NO_INTERNET, BasePojo.ErrorCode.NO_INTERNET));
            return responseLiveData;
        }


        Call<BasePojo<DashboardPojo>> call = api.getDashboard();
        call.enqueue(new Callback<BasePojo<DashboardPojo>>() {
            @Override
            public void onResponse(@NotNull Call<BasePojo<DashboardPojo>> call, @NotNull Response<BasePojo<DashboardPojo>> response) {
                BasePojo<DashboardPojo> resModel;

                if (response.code() == 200 && response.body() != null) {
                    resModel = response.body();

                } else if (response.code() == 422) {
                    APIErrorModel apiErrorModel = null;
                    try {
                        apiErrorModel = new Gson().fromJson(response.errorBody().string(), APIErrorModel.class);
                        if (apiErrorModel.getMessage() != null)
                            resModel = ApiErrorUtils.getErrorData(apiErrorModel.getMessage(), 0);
                        else {
                            resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        }

                    } catch (IOException e) {
                        resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);

                        e.printStackTrace();
                    }
                } else {
                    resModel = ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED);


                }
                responseLiveData.postValue(resModel);
            }

            @Override
            public void onFailure(@NotNull Call<BasePojo<DashboardPojo>> call, @NotNull Throwable t) {
                t.printStackTrace();
                responseLiveData.postValue(ApiErrorUtils.getErrorData(BasePojo.ErrorMessage.SOME_ERROR_OCCURRED, BasePojo.ErrorCode.SOME_ERROR_OCCURRED));


            }
        });

        return responseLiveData;
    }



}
