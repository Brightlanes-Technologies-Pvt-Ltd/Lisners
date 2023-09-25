package com.lisners.counsellor.Activity.Home.HomeStack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.URLs;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    private final String TAG = "ROZAR_PAY";
    StoreData storeData;
    String payment;
    User user;
    DProgressbar dProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);
        storeData = new StoreData(RazorpayActivity.this);
        payment = getIntent().getStringExtra("PRICE");
        dProgressbar = new DProgressbar(this);
        storeData.getData(ConstantValues.USER_DATA, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                if (val != null && payment != null) {
                    {
                        user = new Gson().fromJson(val, User.class);
                        startPayment();
                        // addToWallet("sadsfdsfdsfs");
                    }
                }
            }

            @Override
            public void onFail() {

            }
        });


    }


    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(GlobalData.setting_razor_pay_model.getValue());
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Patient App");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getMobile_no());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        // payment successfull pay_DGU19rDsInjcF2
        addToWallet(s);
        //Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "error code " + i + " -- Payment failed " + s);
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);

        }
        finish();
    }

    private void addToWallet(String pid) {
        Log.e("ENTER", "addToWallet");
        Map<String, String> params = new HashMap<>();
        params.put("amount", payment);
        params.put("payment_id", pid);
        dProgressbar.show();
        PostApiHandler handler = new PostApiHandler(RazorpayActivity.this, URLs.ADD_PAYMENT, params, new PostApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                Log.e("ENTER", new Gson().toJson(jsonObject));
                dProgressbar.dismiss();
                if (jsonObject.has("status")) {
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    if (dataObj.has("wallet"))
                        GlobalData.dashboard_balance = dataObj.getString("wallet");
                    finish();
                    Toast.makeText(RazorpayActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RazorpayActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ANError anError) {
                dProgressbar.dismiss();
            }
        });
        handler.execute();
    }
}