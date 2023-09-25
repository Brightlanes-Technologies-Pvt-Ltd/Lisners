package com.lisners.counsellor.Activity.Home.HomeStack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.ApiModal.SettingModel;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.restApi.pojo.SettingPojo;
import com.lisners.counsellor.zWork.restApi.viewmodel.SettingViewModel;
import com.lisners.counsellor.zWork.utils.ViewModelUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class AddMoneyWalletFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    ImageButton btn_header_left;
    Button btn_add_money;
    EditText edit_price;
    TextView tvWalletMoney2;
    StoreData storeData;

    SettingViewModel settingVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_money_wallet, container, false);
        // Inflate the layout for this fragment
        init(view);
        return view;
    }

    private void init(View view) {

        settingVM = ViewModelUtils.getViewModel(SettingViewModel.class, this);
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_add_money = view.findViewById(R.id.btn_add_money);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        edit_price = view.findViewById(R.id.edit_price);
        tvWalletMoney2 = view.findViewById(R.id.tvWalletMoney2);
        tvWalletMoney2.setText(GlobalData.dashboard_balance);
        btn_add_money.setOnClickListener(this);
        btn_header_left.setOnClickListener(this);
        storeData = new StoreData(getContext());
        tvHeader.setText("Add Money");


    }

    private void addMoney() {
        String price = edit_price.getText().toString();
        if (!price.isEmpty()) {
            Intent intent = new Intent(getContext(), RazorpayActivity.class);
            intent.putExtra("PRICE", price);
            startActivity(intent);
        } else
            Toast.makeText(getContext(), "Enter amount", Toast.LENGTH_SHORT).show();
    }

    public void getSetting() {


        settingVM
                .getSetting("razor_pay_app_id")
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getStatus() && response.getData() != null) {


                        GlobalData.setting_razor_pay_model = response.getData();
                        storeData.setData(ConstantValues.USER_SETTING_RAZOR_PAY, new Gson().toJson(response.getData()), new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                addMoney();
                            }
                        });

                    }

                });

        /*GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_SETTING, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("status") && jsonObject.has("data")) {
                    GlobalData.setting_model = new Gson().fromJson(jsonObject.getString("data"), SettingModel.class);
                    storeData.setData(ConstantValues.USER_SETTING, jsonObject.getString("data"), new StoreData.SetListener() {
                        @Override
                        public void setOK() {
                            addMoney();
                        }
                    });
                }
            }

            @Override
            public void onError() {
            }
        });
        apiHandler.execute();*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_add_money:
                if (GlobalData.setting_razor_pay_model != null) {
                    addMoney();
                } else {
                    storeData.getData(ConstantValues.USER_SETTING_RAZOR_PAY, new StoreData.GetListener() {
                        @Override
                        public void getOK(String val) {
                            if (val != null) {
                                GlobalData.setting_razor_pay_model = new Gson().fromJson(val, SettingPojo.class);
                                addMoney();
                            } else getSetting();
                        }

                        @Override
                        public void onFail() {
                            getSetting();
                        }
                    });
                }

                break;
        }
    }
}