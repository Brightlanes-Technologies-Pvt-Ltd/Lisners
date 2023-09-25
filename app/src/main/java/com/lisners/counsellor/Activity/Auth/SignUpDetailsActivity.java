package com.lisners.counsellor.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.databinding.ActivitySignUpDetailsBinding;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.restApi.pojo.ProfessionDatum;
import com.lisners.counsellor.zWork.restApi.viewmodel.SignUpViewModel;
import com.lisners.counsellor.zWork.utils.ViewModelUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class SignUpDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHeader;
    private Button btnSUpDetailSubmit;
    EditText edit_name, /*edit_clinic_name,*/
            edit_email, edit_city, edit_address, edit_specialization, edit_pass, edit_confirm_pass;
    String st_name,  st_email, st_city, st_speci, st_address, st_pass, st_cfm_pass;
    CheckBox checkSUpDetailAgreeTerms;
    boolean isCheckedPcy = false;
    StoreData storeData;
    ArrayList<ModelSpacialization> spacializations;
    MultiCheckDropDwon speci_dropdown;

    RadioButton radioMale, radioFemale;


    ActivitySignUpDetailsBinding binding;
    SignUpViewModel signUpVM;
    List<ProfessionDatum> professionDatumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivitySignUpDetailsBinding) DataBindingUtil.setContentView(this, R.layout.activity_sign_up_details);

        //setContentView(R.layout.activity_sign_up_details);

        signUpVM = ViewModelUtils.getViewModel(SignUpViewModel.class, this);
        getProfessions();
        init();

        tvHeader.setText("Create an Account");
        btnSUpDetailSubmit.setOnClickListener(this);
    }

    private void getProfessions() {
        signUpVM
                .getProfession()
                .observe(this, response -> {


                    if (response.getStatus() && response.getData() != null) {
                        setProfession(response.getData());
                        //showMainLayout();
                    } else {
                        setProfession(new ArrayList<>());
                    }


                });


    }


    private void init() {

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        tvHeader = findViewById(R.id.tvHeader);
        btnSUpDetailSubmit = findViewById(R.id.btnSUpDetailSubmit);
        storeData = new StoreData(this);
        edit_name = findViewById(R.id.etSUpDetailFirstName);
        /*edit_clinic_name = findViewById(R.id.etSUpDetailClinicName);*/
        edit_email = findViewById(R.id.etSUpDetailEmail);
        edit_city = findViewById(R.id.etSUpDetailCity);
        edit_specialization = findViewById(R.id.etSUpDetailSpecialist);
        edit_address = findViewById(R.id.etSUpDetailAddress);
        edit_pass = findViewById(R.id.etSUpDetailPassword);
        edit_confirm_pass = findViewById(R.id.etSUpDetailCnfPassword);
        checkSUpDetailAgreeTerms = findViewById(R.id.checkSUpDetailAgreeTerms);
        edit_specialization.setOnClickListener(this);

        checkSUpDetailAgreeTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedPcy = isChecked;
            }
        });


    }


    private void setProfession(List<ProfessionDatum> professionData) {


        binding.spnProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Timber.e("Class : %d", position);
                binding.tiProfession.setError(null);
                binding.tiProfession.setVisibility(View.GONE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        professionDatumList.clear();
        professionDatumList.addAll(professionData);

        ProfessionDatum datum = new ProfessionDatum(0, "--select option--");
        professionDatumList.add(0, datum);


        List<String> spinnerList = new ArrayList<>();
        for (int i = 0; i < professionDatumList.size(); i++) {
            spinnerList.add(professionDatumList.get(i).getTitle());
        }
        binding.spnProfession.setSpinnerData(spinnerList);


    }

    public void signUpApi() {
        st_name = edit_name.getText().toString().trim();
        /*st_clinic = edit_clinic_name.getText().toString();*/
        st_email = edit_email.getText().toString().trim();
        st_city = edit_city.getText().toString().trim();
        st_speci = edit_specialization.getText().toString();
        st_address = edit_address.getText().toString().trim();
        st_pass = edit_pass.getText().toString();
        st_cfm_pass = edit_confirm_pass.getText().toString();

        int profession_id = professionDatumList.get(binding.spnProfession.getBinding().spnMain.getSelectedItemPosition()).getId();


        if (st_name.isEmpty())
            edit_name.setError("Enter Name");
        /*else if (st_clinic.isEmpty())
            edit_clinic_name.setError("Enter Clinic Name");*/
        else if (profession_id <= 0) {
            binding.tiProfession.setVisibility(View.VISIBLE);
            binding.tiProfession.setError("Please select a profession");
        } else if (st_email.isEmpty())
            edit_email.setError("Enter Email");
        else if (!UtilsFunctions.isValidEmail(st_email))
            edit_email.setError("Invalid Email");
        else if (st_city.isEmpty())
            edit_city.setError("Enter City");
        else if (st_speci.isEmpty())
            edit_specialization.setError("Select Specialization");
        else if (st_address.isEmpty())
            edit_address.setError("Enter Address");
        else if (st_pass.isEmpty())
            edit_pass.setError("Enter Password");
        else if (st_pass.length() < 8)
            edit_confirm_pass.setError("Your password must be more than 8 characters");
        else if (st_cfm_pass.isEmpty())
            edit_confirm_pass.setError("Enter Confirm Password");
        else if (!st_pass.equals(st_cfm_pass))
            edit_confirm_pass.setError("Confirm password is not the same as password");
        else if (!isCheckedPcy)
            Toast.makeText(this, "Please check Terms of services and Privacy Policy", Toast.LENGTH_SHORT).show();
        else {
            DProgressbar dProgressbar = new DProgressbar(this);
            dProgressbar.show();

            Map<String, String> params = new HashMap<>();
            params.put("name", st_name);
            params.put("profession_id", "" + profession_id);
            params.put("email", st_email);
            params.put("city", st_city);
            params.put("address", st_address);
            params.put("gender", radioMale.isChecked() ? "male" : "female");

            int idx = 0;
            for (ModelSpacialization s : spacializations) {
                if (s.isCheck()) {
                    params.put("specialization_id[" + idx + "]", s.getId() + "");
                    idx++;
                }
            }

            params.put("password", st_pass);
            params.put("confirm_password", st_cfm_pass);

            PostApiHandler postApiHandler = new PostApiHandler(this, URLs.SIGNUP_UPDATE, params, new PostApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    Log.e("jsonObject", new Gson().toJson(jsonObject));
                    dProgressbar.dismiss();
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        storeData.setData(ConstantValues.USER_DATA, jsonObject.getString("data"), new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                Intent i = new Intent(SignUpDetailsActivity.this, MyChargesActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        });
                        dProgressbar.dismiss();
                    } else {
                        if (jsonObject.has("type") && !jsonObject.getString("type").isEmpty() && jsonObject.getString("type").equalsIgnoreCase("validation")) {
                            JSONObject jsonObjet = jsonObject.getJSONObject("errors");
                            if (jsonObjet.has("name")) {
                                edit_name.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("name")));
                                edit_name.requestFocus();
                            }

                            if (jsonObjet.has("profession_id")) {
                                binding.tiProfession.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("profession_id")));
                                binding.tiProfession.requestFocus();
                                binding.tiProfession.setVisibility(View.VISIBLE);
                            }
                            if (jsonObjet.has("clinic_name")) {
                                /*edit_clinic_name.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("clinic_name")));
                                edit_clinic_name.requestFocus();*/
                            }
                            if (jsonObjet.has("city")) {
                                edit_city.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("city")));
                                edit_city.requestFocus();
                            }
                            if (jsonObjet.has("email")) {
                                edit_email.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("email")));
                                edit_email.requestFocus();

                            }
                            if (jsonObjet.has("address")) {
                                edit_address.setError(UtilsFunctions.errorShow(jsonObjet.getJSONArray("address")));
                                edit_address.requestFocus();
                            }
                        } else {
                            Toast.makeText(SignUpDetailsActivity.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                        /*JSONObject errObj = jsonObject.getJSONObject("errors");
                        for (Iterator<String> it = errObj.keys(); it.hasNext(); ) {
                            String key = it.next();
                            if(key!=null)
                                Toast.makeText(SignUpDetailsActivity.this, UtilsFunctions.errorShow(errObj.getJSONArray(key)), Toast.LENGTH_SHORT).show();
                        }*/
                }

                @Override
                public void onError(ANError error) {
                    Toast.makeText(SignUpDetailsActivity.this,
                            "Server Error", Toast.LENGTH_SHORT).show();
                    dProgressbar.dismiss();
                }
            });
            postApiHandler.execute();
        }


    }

    boolean dataLoading = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSUpDetailSubmit:
                signUpApi();
                break;
            case R.id.etSUpDetailSpecialist:
                if (spacializations != null && !spacializations.isEmpty() && speci_dropdown != null) {
                    speci_dropdown.show();

                } else {
                    spacializations = new ArrayList<>();
                    if (dataLoading) {
                        dataLoading = false;
                        GetApiHandler apiHandler = new GetApiHandler(this, URLs.GET_SPECIALIZATION, new GetApiHandler.OnClickListener() {
                            @Override
                            public void onResponse(JSONObject jsonObject) throws JSONException {
                                dataLoading = true;
                                Log.e("D", new Gson().toJson(jsonObject));
                                if (jsonObject.getBoolean("status")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ModelSpacialization s = new Gson().fromJson(jsonArray.getString(i), ModelSpacialization.class);
                                        spacializations.add(s);
                                    }
                                    Log.e("LINst", spacializations.size() + "");
                                    speci_dropdown = new MultiCheckDropDwon(SignUpDetailsActivity.this, "Select  Specialization", spacializations, new MultiCheckDropDwon.OnItemClickListener() {
                                        @Override
                                        public void onClick(ArrayList<ModelSpacialization> selected_spaci) {
                                            spacializations = selected_spaci;
                                            String s = "";
                                            for (ModelSpacialization spacialization : selected_spaci) {
                                                if (spacialization.isCheck()) {
                                                    if (s.isEmpty()) s = spacialization.getName();
                                                    else s = s + ", " + spacialization.getName();
                                                }
                                            }
                                            edit_specialization.setText(s);
                                        }
                                    });

                                }

                            }

                            @Override
                            public void onError() {
                                dataLoading = true;
                            }
                        });
                        apiHandler.execute();

                    }
                }

                break;
        }
    }
}