package com.lisners.counsellor.Activity.Home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.error.ANError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.MyChargesActivity;
import com.lisners.counsellor.Activity.Auth.SignUpDetailsActivity;
import com.lisners.counsellor.Activity.Auth.WelcomeActivity;
import com.lisners.counsellor.Activity.Home.AppointmentStack.AppointmentFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.AddPrescriptionFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyAvailabilityFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyPatientsFragment;
import com.lisners.counsellor.Activity.Home.AvailabilityStack.MyReviewFragment;
import com.lisners.counsellor.Activity.Home.HomeStack.HomeFragment;
import com.lisners.counsellor.Activity.Home.HomeStack.NotificationFragment;
import com.lisners.counsellor.Activity.Home.HomeStack.ReportsFragment;
import com.lisners.counsellor.Activity.Home.HomeStack.WalletHistoryFragment;
import com.lisners.counsellor.Activity.Home.ProfileStack.ProfileFragment;
import com.lisners.counsellor.Activity.Home.ProfileStack.SettingsFragment;
import com.lisners.counsellor.Activity.activity.RingActivity;
import com.lisners.counsellor.Activity.call.CallAcceptDialog;
import com.lisners.counsellor.Activity.call.newA.AgoraVideoCallActivity;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.CounselorProfile;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ActivityResultBus;
import com.lisners.counsellor.utils.ActivityResultEvent;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.base.BasePojo;
import com.lisners.counsellor.zWork.base.SocketSingleton;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;
import com.lisners.counsellor.zWork.restApi.viewmodel.SettingViewModel;
import com.lisners.counsellor.zWork.utils.ViewModelUtils;
import com.lisners.counsellor.zWork.utils.aModel.CallData;
import com.lisners.counsellor.zWork.utils.aModel.On_Off_Data;
import com.lisners.counsellor.zWork.utils.config.MainApplication;
import com.lisners.counsellor.zWork.utils.helperClasses.ConnectionMessageLayout;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, Connectable, Disconnectable, Bindable {
    BottomNavigationView bottom_tab_navigation;
    NavigationView navigationView;
    FragmentManager fm;
    DrawerLayout mDrawer;
    StoreData storeData;
    Switch sw_header;
    ProgressBar sw_header_loader;
    User user;
    DProgressbar dProgressbar;
    boolean sw_flag = false;
    AlertDialog alertDialog = null;

    Merlin merlin;

    SettingViewModel settingVM;


    public static Intent makeCallOverIntent(Context context, int book_id) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setAction("call_over");
        intent.putExtra("isCallOver", true);
        intent.putExtra("book_id", book_id);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        storeData = new StoreData(this);
        dProgressbar = new DProgressbar(this);


        merlin = new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks().build(this);


        init();
    }

    private void init() {

        settingVM = ViewModelUtils.getViewModel(SettingViewModel.class, this);

        fm = this.getSupportFragmentManager();
        mDrawer = findViewById(R.id.drawer_layout);
        bottom_tab_navigation = findViewById(R.id.bottomNavigationView);
        bottom_tab_navigation.setOnNavigationItemSelectedListener(this);
        navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        loadFragment(new HomeFragment());
        drawerEvents();
        getProfile();
        //getNotificatiom();
        receiveNotification();
        connectToSocket();

        checkIFVideoCallOver();

    }

    private void checkIFVideoCallOver() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("call_over") && intent.getBooleanExtra("isCallOver", false)) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                AddPrescriptionFragment appointmentDetailsFragment = new AddPrescriptionFragment();
                Bundle bundle = new Bundle();
                int bookId = intent.getIntExtra("book_id", 0);
                bundle.putString("BOOK_ID", bookId + "");
                bundle.putString("HIDE", "HIDE");
                appointmentDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, appointmentDetailsFragment);
                transaction.addToBackStack("HomeFragment");
                transaction.commit();

            }

        }
    }


    /*private void onChangeStatus() {
        int sw_val = user.getIs_online() == 1 ? 0 : 1;


        Map<String, String> params = new HashMap<>();
        params.put("is_online", sw_val + "");
        if (sw_header_loader != null)
            sw_header_loader.setVisibility(View.VISIBLE);
        PostApiHandler postApiHandler = new PostApiHandler(this, URLs.UPDATE_ONLINE_STATUS, params, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                if (sw_header_loader != null)
                    sw_header_loader.setVisibility(View.GONE);
                if (jsonObject.has("status")) {
                    sw_header.setChecked(sw_val == 1);
                    user.setIs_online(sw_val);
                } else
                    sw_header.setChecked(sw_val == 1);
            }

            @Override
            public void onError(ANError error) {
                if (sw_header_loader != null)
                    sw_header_loader.setVisibility(View.GONE);
                sw_header.setChecked(sw_val == 1);
            }
        });
        postApiHandler.execute();
    }*/


    private void onChangeStatus() {
        int sw_val = user.getIs_online() == 1 ? 0 : 1;

        attemptSendCounsellorStatus(sw_val);
        sw_header.setChecked(sw_val == 1);
        user.setIs_online(sw_val);

    }

    public void drawerEvents() {
        View header = navigationView.getHeaderView(0);
        ImageView iv_photo = header.findViewById(R.id.iv_header);
        TextView tv_title = header.findViewById(R.id.tv_header_title);
        TextView tv_des = header.findViewById(R.id.tv_header_des);
        TextView iv_char_drawer = header.findViewById(R.id.iv_char_drawer);
        sw_header_loader = header.findViewById(R.id.sw_header_loader);
        sw_header = header.findViewById(R.id.sw_header);

        sw_header.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw_flag)
                    onChangeStatus();
            }
        });

        if (user != null)
            sw_header.setChecked(user.getIs_online() == 1);

        storeData.getData(ConstantValues.USER_DATA, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                Log.e("USER", val);
                User user = new Gson().fromJson(val, User.class);
                CounselorProfile counselorProfile = user.getCounselor_profile();

                if (user.getName() != null)
                    tv_title.setText(UtilsFunctions.splitCamelCase(user.getName()));
               /* if (counselorProfile != null && counselorProfile.getClinic_name() != null) {
                    tv_des.setText(counselorProfile.getClinic_name());
                    tv_des.setVisibility(View.VISIBLE);
                }*/
                if (user.getProfile_image() != null) {
                    UtilsFunctions.SetLOGOWithRoundedCorners(HomeActivity.this, user.getProfile_image(), iv_photo, 20);
                    iv_char_drawer.setVisibility(View.GONE);
                } else {
                    iv_char_drawer.setText(UtilsFunctions.getFistLastChar(user.getName()));
                    iv_char_drawer.setVisibility(View.VISIBLE);
                }

                if (user.getDeleted_at() != null) {
                    userLogOut();
                }

            }

            @Override
            public void onFail() {
                Log.e("USER", "Fail");
            }
        });

    }

    public void closeDrawer() {
        mDrawer.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer() {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    private boolean loadFragment(Fragment fragment) {
        String tag;
        if (fragment instanceof HomeFragment) {
            tag = "home";
        } else {
            tag = "";
        }

        try {
            if (fragment != null) {
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void userLogOut() {
        dProgressbar.show();
        if (mSocket != null)
            mSocket.disconnect();

        Map<String, String> params = new HashMap<>();
        PostApiHandler postApiHandler = new PostApiHandler(HomeActivity.this, URLs.LOGOUT, params, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                dProgressbar.dismiss();

                if (jsonObject.has("status")) {

                    storeData.clearData(ConstantValues.USER_TOKEN);
                    Intent i = new Intent(HomeActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    if (mSocket != null)
                        mSocket.connect();
                }
            }

            @Override
            public void onError(ANError error) {
                dProgressbar.dismiss();
                storeData.clearData(ConstantValues.USER_TOKEN);
                Intent i = new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        postApiHandler.execute();
    }


    private void callNewScreen(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack("HomeFragment");
        transaction.commit();
    }


    private void receiveNotification() {
        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("calling_intent")) {

            //handle calling
            String data = intent.getStringExtra("calling_data");
            Map<String, String> appointment = new Gson().fromJson(data, Map.class);

          /*  CallAcceptDialog acceptDialog = new CallAcceptDialog(HomeActivity.this, new CallAcceptDialog.OnClickListener() {
                @Override
                public void onAccept() {
                        *//*Intent intent1 = new Intent(HomeActivity.this, VideoCallScreen.class);
                        intent1.putExtra("CALLING_ACTION", action);*//*
                    Intent intent1 = AgoraVideoCallActivity.makeIntent(HomeActivity.this, data);
                    startActivityForResult(intent1, 500);
                }

                @Override
                public void onReject(BookedAppointment bookedAppointment) {
                    attemptSendCallRejectToSocket(appointment.get("appointment"), "" + bookedAppointment.getCounselor_id(), "" + bookedAppointment.getUser_id());
                }
            });

            Log.e("receive noti bId==>", data);
            acceptDialog.checkAndShow(data);*/

        } else if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("deepLink_intent")) {
            String data = getIntent().getStringExtra("data");
            Map<String, String> dataMap = new Gson().fromJson(data, Map.class);

            if (dataMap.get("type").equals("appointment")) {
                bottom_tab_navigation.setSelectedItemId(R.id.menu_action_appointment);

            } else if (dataMap.get("type").equals("profile")) {
                bottom_tab_navigation.setSelectedItemId(R.id.menu_action_profile);

            } else if (dataMap.get("type").equals("review")) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MyReviewFragment());
                transaction.addToBackStack("HomeFragment");
                transaction.commit();

            } else if (dataMap.get("type").equals("payment_receive")) {
                bottom_tab_navigation.setSelectedItemId(R.id.nav_wallet);
            } else if (dataMap.get("type").equals("transaction_list")) {
                bottom_tab_navigation.setSelectedItemId(R.id.nav_wallet);

            }


        }

    }

    /*private void getNotificatiom() {
        String action = getIntent().getStringExtra("CALLING_ACTION");
        Log.e("[CALLING_ACTION]", action + "");
        if (action != null) {
            Map<String, String> noti = new Gson().fromJson(action, Map.class);
            if (noti.containsKey("type") && noti.get("type").equals("calling")) {
                CallAcceptDialog acceptDialog = new CallAcceptDialog(this, new CallAcceptDialog.OnClickListener() {
                    @Override
                    public void onAccept() {
                        *//*Intent intent1 = new Intent(HomeActivity.this, VideoCallScreen.class);
                        intent1.putExtra("CALLING_ACTION", action);*//*
                        Intent intent1 = AgoraVideoCallActivity.makeIntent(HomeActivity.this, action);
                        startActivity(intent1);
                    }

                    @Override
                    public void onReject() {

                    }
                });
                acceptDialog.checkAndShow(action);
            } else if (noti.containsKey("transaction_list"))
                callNewScreen(new WalletHistoryFragment());
            else if (noti.containsKey("appointment"))
                callNewScreen(new AppointmentFragment());

            else if (noti.containsKey("review"))
                callNewScreen(new MyReviewFragment());

        }
    }*/


    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Calling---->", "Incoming Call");


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (intent.getAction().equals("calling_intent")) {
                        String callingData = intent.getStringExtra("calling_data");

                        /*Handler h = new Handler();
                        long delayInMilliseconds = 2000;
                        h.postDelayed(new Runnable() {
                            public void run() {
                                getNotificationManager().cancel(notification_id);
                            }
                        }, delayInMilliseconds);*/


                        /*final CallAcceptDialog acceptDialog = new CallAcceptDialog(HomeActivity.this, new CallAcceptDialog.OnClickListener() {
                            @Override
                            public void onAccept() {
                                try {
                            *//*Intent intent1 = new Intent(HomeActivity.this, VideoCallScreen.class);
                            intent1.putExtra("CALLING_ACTION", action);*//*

                                    Intent intent1 = AgoraVideoCallActivity.makeIntent(HomeActivity.this, callingData);
                                    startActivity(intent1);

                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onReject(BookedAppointment bookedAppointment) {
                                attemptSendCallRejectToSocket("" + bookedAppointment.getId(), "" + bookedAppointment.getCounselor_id(), "" + bookedAppointment.getUser_id());
                            }
                        });
                        Log.e("broadcast bId==>", callingData);
                        acceptDialog.checkAndShow(callingData);*/


                        startActivity(RingActivity.makeIntent(HomeActivity.this, callingData));
                    }
                }
            });

        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {

            case R.id.menu_action_home:
                bottom_tab_navigation.getMenu().setGroupCheckable(0, true, true);
                fragment = new HomeFragment();
                break;

            case R.id.menu_action_appointment:
                bottom_tab_navigation.getMenu().setGroupCheckable(0, true, true);
                fragment = new AppointmentFragment();
                break;

            case R.id.menu_action_availability:
                bottom_tab_navigation.getMenu().setGroupCheckable(0, true, true);
                fragment = new MyAvailabilityFragment();
                break;

            case R.id.menu_action_profile:
                bottom_tab_navigation.getMenu().setGroupCheckable(0, true, true);
                fragment = new ProfileFragment();
                break;

            case R.id.nav_home:
                item.setChecked(true);
                closeDrawer();
                //fragment = new HomeFragment();
                bottom_tab_navigation.setSelectedItemId(R.id.menu_action_home);
                return true;

            case R.id.nav_appointments:
                item.setChecked(true);
                closeDrawer();
                //callNewScreen(new AppointmentFragment());
                bottom_tab_navigation.setSelectedItemId(R.id.menu_action_appointment);
                return true;

            case R.id.nav_wallet:
                item.setChecked(true);
                closeDrawer();
                callNewScreen(new WalletHistoryFragment());
                bottom_tab_navigation.getMenu().setGroupCheckable(0, false, true);
                return true;

            case R.id.nav_set_availability:
                item.setChecked(true);
                closeDrawer();
                /*  callNewScreen(new MyAvailabilityFragment());*/
                bottom_tab_navigation.setSelectedItemId(R.id.menu_action_availability);
                return true;

            case R.id.nav_reports:
                item.setChecked(true);
                closeDrawer();
                callNewScreen(new ReportsFragment());
                bottom_tab_navigation.getMenu().setGroupCheckable(0, false, true);
                return true;

            case R.id.nav_noti:
                item.setChecked(true);
                closeDrawer();
                callNewScreen(new NotificationFragment());
                bottom_tab_navigation.getMenu().setGroupCheckable(0, false, true);
                return true;

            case R.id.nav_profile:
                item.setChecked(true);
                closeDrawer();
                /*callNewScreen(new ProfileFragment());*/
                bottom_tab_navigation.setSelectedItemId(R.id.menu_action_profile);
                return true;

            case R.id.nav_setting:
                callNewScreen(new SettingsFragment());
                item.setChecked(true);
                closeDrawer();
                bottom_tab_navigation.getMenu().setGroupCheckable(0, false, true);

                return true;

            case R.id.nav_patients:
                callNewScreen(new MyPatientsFragment());
                item.setChecked(true);
                closeDrawer();
                bottom_tab_navigation.getMenu().setGroupCheckable(0, false, true);
                return true;


            case R.id.nav_faq:
                Intent intent1 = new Intent(HomeActivity.this, Faqs.class);
                startActivity(intent1);
                return true;

            case R.id.nav_contact:
                closeDrawer();
                Intent intent = new Intent(this, WebScreen.class);
                intent.putExtra("SLUG", "contact");
                startActivity(intent);
                return true;

            case R.id.nav_term_condition:
                closeDrawer();
                Intent intent2 = new Intent(this, WebScreen.class);
                intent2.putExtra("SLUG", "terms-conditions");
                startActivity(intent2);
                return true;

            case R.id.nav_about_us:
                closeDrawer();
                Intent intent3 = new Intent(this, WebScreen.class);
                intent3.putExtra("SLUG", "about-us");
                startActivity(intent3);
                return false;

            case R.id.nav_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to logout?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                userLogOut();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alertDialog.hide();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                closeDrawer();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));

        if (resultCode == ConstantValues.RESULT_CALL && data != null) {

          /*  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            AddPrescriptionFragment appointmentDetailsFragment = new AddPrescriptionFragment();
            Bundle bundle = new Bundle();
            int bookId = data.getIntExtra("book_id", 0);
            bundle.putString("BOOK_ID", bookId + "");
            bundle.putString("HIDE", "HIDE");
            appointmentDetailsFragment.setArguments(bundle);
            transaction.replace(R.id.fragment_container, appointmentDetailsFragment);
            transaction.addToBackStack("HomeFragment");
            transaction.commit();*/


            /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new P());
            transaction.addToBackStack("HomeFragment");
            transaction.commit();*/

        }
    }


    public void getProfile() {


        settingVM
                .getProfile()
                .observe(HomeActivity.this, response -> {
                    if (response.getStatus() && response.getData() != null) {

                        user = response.getData();
                        if (sw_header != null)
                            sw_header.setChecked(user.getIs_online() == 1);
                        if (user.getEmail() == null)
                            startActivity(new Intent(HomeActivity.this, SignUpDetailsActivity.class));
                        else if (user.getCounselor_profile() == null)
                            startActivity(new Intent(HomeActivity.this, MyChargesActivity.class));
                        else {
                            storeData.setData(ConstantValues.USER_DATA, new Gson().toJson(response.getData()), new StoreData.SetListener() {
                                @Override
                                public void setOK() {
                                    sw_flag = true;
                                }
                            });
                        }

                    } else {
                        if (response.getErrorStatus() == BasePojo.ErrorCode.UNAUTHORIZED) {
                            userLogOut();
                        }

                    }

                });


      /*  GetApiHandler apiHandler = new GetApiHandler(this, URLs.GET_PROFILE, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    user = new Gson().fromJson(jsonObject.getString("data"), User.class);
                    if (sw_header != null)
                        sw_header.setChecked(user.getIs_online() == 1);
                    if (user.getEmail() == null)
                        startActivity(new Intent(HomeActivity.this, SignUpDetailsActivity.class));
                    else if (user.getCounselor_profile() == null)
                        startActivity(new Intent(HomeActivity.this, MyChargesActivity.class));
                    else {
                        storeData.setData(ConstantValues.USER_DATA, jsonObject.getString("data"), new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                sw_flag = true;
                            }
                        });
                    }

                }
            }

            @Override
            public void onError() {
            }
        });
        apiHandler.execute();*/
        getSetting();
    }

    public void getSetting() {

        final String[] oldRazorPayId = {""};
        final String[] oldAgoraAppId = {""};
        storeData.getData(ConstantValues.USER_SETTING_RAZOR_PAY, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                if (!val.isEmpty()) {
                    GlobalData.setting_razor_pay_model = new Gson().fromJson(val, SettingPojo.class);
                    oldRazorPayId[0] = GlobalData.setting_razor_pay_model.getValue();
                }

            }

            @Override
            public void onFail() {


            }
        });


        storeData.getData(ConstantValues.USER_SETTING_AGORA_APP_ID, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                if (!val.isEmpty()) {
                    Log.e("print==>", "" + val);
                    GlobalData.setting_agora_id_model = new Gson().fromJson(val, SettingPojo.class);
                    oldAgoraAppId[0] = GlobalData.setting_agora_id_model.getValue();
                }

            }

            @Override
            public void onFail() {


            }
        });
        /*storeData.getData(ConstantValues.USER_SETTING_RAZOR_PAY, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                GlobalData.setting_razor_pay_model = new Gson().fromJson(val, SettingPojo.class);

            }

            @Override
            public void onFail() {

                PostApiHandler apiHandler = new PostApiHandler(HomeActivity.this, URLs.GET_SETTING, null, new PostApiHandler.OnClickListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        if (jsonObject.has("status") && jsonObject.has("data")) {
                            GlobalData.setting_model = new Gson().fromJson(jsonObject.getString("data"), SettingModel.class);
                            storeData.setData(ConstantValues.USER_SETTING, jsonObject.getString("data"), new StoreData.SetListener() {
                                @Override
                                public void setOK() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                    }
                });

                apiHandler.execute();
            }
        });
*/

        settingVM
                .getSetting("razor_pay_app_id")
                .observe(HomeActivity.this, response -> {
                    if (response.getStatus() && response.getData() != null) {


                        GlobalData.setting_razor_pay_model = response.getData();
                        storeData.setData(ConstantValues.USER_SETTING_RAZOR_PAY, new Gson().toJson(response.getData()), new StoreData.SetListener() {
                            @Override
                            public void setOK() {

                            }
                        });

                    }

                });


        settingVM
                .getSetting("agora_app_id")
                .observe(HomeActivity.this, response -> {
                    if (response.getStatus() && response.getData() != null) {


                        if (!oldAgoraAppId[0].equals(response.getData().getValue())) {
                            GlobalData.setting_agora_id_model = response.getData();

                            storeData.setData(ConstantValues.USER_SETTING_AGORA_APP_ID, new Gson().toJson(response.getData()), new StoreData.SetListener() {
                                @Override
                                public void setOK() {

                                }
                            });
                        }

                    }

                });



       /* settingVM
                .getSetting("razor_pay_app_id")
                .observe(HomeActivity.this, response -> {
                    if (response.getStatus() && response.getData() != null) {


                        GlobalData.setting_razor_pay_model = response.getData();
                        storeData.setData(ConstantValues.USER_SETTING_RAZOR_PAY, new Gson().toJson(response.getData()), new StoreData.SetListener() {
                            @Override
                            public void setOK() {

                            }
                        });

                    }

                });


        settingVM
                .getSetting("agora_app_id")
                .observe(HomeActivity.this, response -> {
                    if (response.getStatus() && response.getData() != null) {


                        GlobalData.setting_agora_id_model = response.getData();
                        storeData.setData(ConstantValues.USER_SETTING_AGORA_APP_ID, response.getData().getValue(), new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                if (application().rtcEngine() == null) {
                                    application().initAgoraEngine(response.getData().getValue());
                                }
                            }
                        });

                    }

                });*/


       /* PostApiHandler apiHandler = new PostApiHandler(this, URLs.GET_SETTING, null, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    GlobalData.setting_model = new Gson().fromJson(jsonObject.getString("data"), SettingModel.class);
                    storeData.setData(ConstantValues.USER_SETTING, jsonObject.getString("data"), new StoreData.SetListener() {
                        @Override
                        public void setOK() {

                        }
                    });
                }
            }

            @Override
            public void onError(ANError error) {

            }
        });

        apiHandler.execute();*/
    }

    public MainApplication application() {
        return (MainApplication) getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("calling_intent"));


        merlin.bind();
        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);
        merlin.registerBindable(this);
    }

    @Override
    protected void onPause() {
        merlin.unbind();
        super.onPause();

    }


    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public void onConnect() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ConnectionMessageLayout.closeLayout((TextView) findViewById(R.id.textview), HomeActivity.this);

            }
        });
    }

    @Override
    public void onDisconnect() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ConnectionMessageLayout.showLayout((TextView) findViewById(R.id.textview), HomeActivity.this);

            }
        });

    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentByTag("home") != null && getSupportFragmentManager().findFragmentByTag("home").isResumed())
            backPressed();
        else
            super.onBackPressed();

    }

    void backPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(HomeActivity.this, "Tap back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                Timber.e("backdrops ====>");
            }
        }, 4000);

    }


    Socket mSocket;

    void connectToSocket() {
        SocketSingleton socketSingleton = SocketSingleton.getSync(HomeActivity.this);
        mSocket = socketSingleton.getSocket();

        mSocket.on("counslar-call-cut", onCallCutSocketEvent);
        mSocket.on("counslar-online", onOnlineOfflineSocketEvent);
        mSocket.on("counslar-offline", onOnlineOfflineSocketEvent);

        mSocket.connect();
    }

    private Emitter.Listener onCallCutSocketEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/

                    //((TextView) findViewById(R.id.tv)).setText("" + new Gson().toJson(args));


                    // add the message to view

                }
            });
        }
    };

    private Emitter.Listener onOnlineOfflineSocketEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/

                    //((TextView) findViewById(R.id.tv)).setText("" + new Gson().toJson(args));


                    // add the message to view

                }
            });
        }
    };


    private void attemptSendCounsellorStatus(int status) {
        On_Off_Data on_off_data = new On_Off_Data(status);
        String msg = new Gson().toJson(on_off_data);
        if (status == 1)
            mSocket.emit("counslar-online", msg);
        else
            mSocket.emit("counslar-offline", msg);

    }

    private void attemptSendCallRejectToSocket(String bookAppointmentId, String counsellorId, String patientId) {

        CallData callData = new CallData(bookAppointmentId, counsellorId, patientId);
        String msg = new Gson().toJson(callData);
        mSocket.emit("counslar-call-cut", msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        mSocket.disconnect();
        mSocket.off("counslar-call-cut", onCallCutSocketEvent);
        mSocket.off("counslar-online", onOnlineOfflineSocketEvent);
        mSocket.off("counslar-offline", onOnlineOfflineSocketEvent);
    }


    private NotificationManager getNotificationManager() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel chan1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel("channel_default",
                    "Default", NotificationManager.IMPORTANCE_HIGH);
            chan1.setLightColor(Color.GREEN);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(chan1);
        }
        return mNotificationManager;
    }
}
