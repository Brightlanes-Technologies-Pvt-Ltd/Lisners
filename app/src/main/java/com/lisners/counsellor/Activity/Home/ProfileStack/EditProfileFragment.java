package com.lisners.counsellor.Activity.Home.ProfileStack;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Auth.MultiCheckDropDwon;
import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.ProfileSpecializationAdaptor;
import com.lisners.counsellor.Adapters.SpacilizationAdaptor;
import com.lisners.counsellor.ApiModal.CounselorProfile;
import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.utils.ActivityResultBus;
import com.lisners.counsellor.utils.ActivityResultEvent;
import com.lisners.counsellor.utils.ConstantValues;
import com.lisners.counsellor.utils.StoreData;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.restApi.viewmodel.LoginViewModel;
import com.lisners.counsellor.zWork.utils.ViewModelUtils;
import com.lisners.counsellor.zWork.utils.helperClasses.ImageSelectorHelper;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    TextView tvHeader, tv_profile_name, tv_add_sp, tv_text_error, tv_profession;
    ImageButton btn_header_left;
    RecyclerView rv_spacilization;

    EditText edit_full_name, /*edit_clinic_name*/
            edit_email, edit_city, edit_address;
    Button btn_update;
    ImageView iv_edit_profile, iv_profile;
    StoreData storeData;
    DProgressbar dProgressbar;
    String CP = Manifest.permission.CAMERA;
    String SP = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    List<String> PERMISSIONS = new ArrayList<>();
    File FileImagePath;

    CheckBox checkSUpDetailAgreeTerms;
    boolean isCheckedPcy = false;
    ArrayList<SpacializationMedel> spacializationMedels;
    ProfileSpecializationAdaptor spacilizationAdaptor;
    ArrayList<ModelSpacialization> spacializations;
    MultiCheckDropDwon speci_dropdown;
    RadioButton radioMale, radioFemale;
    RadioGroup rgOption;


    ImageSelectorHelper userImageSelectorHelper;
    LoginViewModel loginVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        spacializationMedels = new ArrayList<>();
        init(view);
        getProfile();
        return view;
    }

    private void init(View view) {
        loginVM = ViewModelUtils.getViewModel(LoginViewModel.class, this);
        userImageSelectorHelper = new ImageSelectorHelper(this, getContext(), getActivity(), new ImageSelectorHelper.ImageSelectorListener() {
            @Override
            public void onImageGet(Bitmap imageBitmap, File imageFile) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff that updates the UI

                        updateUserImage(imageFile, imageBitmap);

                    }
                });

            }
        });


        tv_profession = view.findViewById(R.id.tv_profession);

        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);
        rgOption = view.findViewById(R.id.rgOption);

        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        storeData = new StoreData(getContext());
        dProgressbar = new DProgressbar(getContext());
        rv_spacilization = view.findViewById(R.id.rv_spacilization);
        tv_profile_name = view.findViewById(R.id.tv_profile_name);
        tv_add_sp = view.findViewById(R.id.tv_add_sp);
        tv_text_error = view.findViewById(R.id.tv_text_error);

        iv_profile = view.findViewById(R.id.iv_profile);
        iv_edit_profile = view.findViewById(R.id.iv_edit_profile);
        edit_full_name = view.findViewById(R.id.edit_full_name);
        /*edit_clinic_name = view.findViewById(R.id.edit_clinic_name);*/
        edit_email = view.findViewById(R.id.edit_email);
        edit_city = view.findViewById(R.id.edit_city);
        edit_address = view.findViewById(R.id.edit_address);
        btn_update = view.findViewById(R.id.btn_update_profile);
        btn_update.setOnClickListener(this);
        iv_edit_profile.setOnClickListener(this);
        btn_header_left.setOnClickListener(this);
        tv_add_sp.setOnClickListener(this);

        tvHeader.setText("Edit Profile");
    }


    public void getProfile() {
        storeData.getData(ConstantValues.USER_DATA_UPDATE, new StoreData.GetListener() {
            @Override
            public void getOK(String val) {
                storeData.getData(ConstantValues.USER_DATA, new StoreData.GetListener() {
                    @Override
                    public void getOK(String val) {
                        if (val != null) {
                            showProfile(val);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }

            @Override
            public void onFail() {
                dProgressbar.show();
                GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_PROFILE, new GetApiHandler.OnClickListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        if (jsonObject.has("status") && jsonObject.has("data")) {
                            showProfile(jsonObject.getString("data"));
                            Map<String, String> params = new HashMap<>();
                            params.put(ConstantValues.USER_DATA, jsonObject.getString("data"));
                            params.put(ConstantValues.USER_DATA_UPDATE, "YES");
                            storeData.setMultiData(params, new StoreData.SetListener() {
                                @Override
                                public void setOK() {
                                    dProgressbar.dismiss();
                                }
                            });

                        }
                        dProgressbar.dismiss();
                    }

                    @Override
                    public void onError() {
                        dProgressbar.dismiss();
                    }
                });
                apiHandler.execute();
            }
        });
    }

    private void showProfile(String val) {
        User user = new Gson().fromJson(val, User.class);
        CounselorProfile counselorProfile = user.getCounselor_profile();
        spacializationMedels = user.getSpecialization();
        UtilsFunctions.SetLOGO(getContext(), user.getProfile_image(), iv_profile);
        tv_profile_name.setText(UtilsFunctions.splitCamelCase(user.getName()));
        edit_full_name.setText(UtilsFunctions.splitCamelCase(user.getName()));
       /* if (counselorProfile != null)
            edit_clinic_name.setText(counselorProfile.getClinic_name());*/
        edit_email.setText(user.getEmail());
        edit_city.setText(user.getCity());
        edit_address.setText(user.getAddress());

        if (user.getGender() != null) {
            if (user.getGender().equals("male")) {
                radioMale.setChecked(true);
            } else {
                radioFemale.setChecked(true);

            }
        }

        if (user.getCounselor_profile().getProfession() != null) {
            tv_profession.setText(user.getCounselor_profile().getProfession().getTitle());
        }


        if (spacializationMedels != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
            spacilizationAdaptor = new ProfileSpecializationAdaptor(getContext(), spacializationMedels);
            rv_spacilization.setAdapter(spacilizationAdaptor);
            rv_spacilization.setLayoutManager(linearLayoutManager);

        }
    }

    public void saveProfile() {

        String st_name = edit_full_name.getText().toString();
        /*String st_clinic = edit_clinic_name.getText().toString();*/
        String st_clinic = "";
        String st_email = edit_email.getText().toString();
        String st_city = edit_city.getText().toString();
        String st_address = edit_address.getText().toString();
        if (st_name.isEmpty())
            edit_full_name.setError("Enter Name");
//        else if(st_clinic.isEmpty())
//            edit_clinic_name.setError("Enter Clinic Name");
        else if (st_email.isEmpty())
            edit_email.setError("Enter Email");
        else if (!UtilsFunctions.isValidEmail(st_email))
            edit_email.setError("Invalid Email");
        else if (st_city.isEmpty())
            edit_city.setError("Enter City");
        else if (st_address.isEmpty())
            edit_address.setError("Enter Address");
        else if (rgOption.getCheckedRadioButtonId() != R.id.radioMale && rgOption.getCheckedRadioButtonId() != R.id.radioFemale)
            Toast.makeText(getActivity(), "Please select gender", Toast.LENGTH_SHORT).show();
        else if (spacilizationAdaptor != null && spacilizationAdaptor.getItemCount() <= 0)
            tv_text_error.setText("Please select at least 1 value.");
        else {
            tv_text_error.setText("");
            DProgressbar dProgressbar = new DProgressbar(getContext());
            dProgressbar.show();
            Map<String, String> params = new HashMap<>();
            params.put("name", st_name);
            /*if (!st_clinic.isEmpty())*/
            params.put("clinic_name", st_clinic);
            params.put("email", st_email);
            params.put("city", st_city);
            params.put("address", st_address);
            params.put("gender", radioMale.isChecked() ? "male" : "female");
            int sp = 0;
            for (SpacializationMedel medel : spacializationMedels) {
                params.put("specialization_id[" + sp + "]", medel.getId() + "");
                sp++;
            }

            PostApiHandler postApiHandler = new PostApiHandler(getContext(), URLs.PROFILE_UPDATE, params, new PostApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if (jsonObject.has("status") && jsonObject.has("data")) {
                        storeData.setData(ConstantValues.USER_DATA, jsonObject.getString("data"), new StoreData.SetListener() {
                            @Override
                            public void setOK() {
                                ((HomeActivity) getContext()).drawerEvents();
                                getFragmentManager().popBackStack();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    dProgressbar.dismiss();

                }

                @Override
                public void onError(ANError error) {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                    dProgressbar.dismiss();
                }
            });
            postApiHandler.execute();
        }
    }

    boolean dataLoading = true;

    public void showSpecilizeDropdwon() {

        if (spacializations != null && !spacializations.isEmpty() && speci_dropdown != null) {
            speci_dropdown.show();

        } else {
            spacializations = new ArrayList<>();
            if (dataLoading) {
                dataLoading = false;
                GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.GET_SPECIALIZATION, new GetApiHandler.OnClickListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        dataLoading = true;
                        Log.e("D", new Gson().toJson(jsonObject));
                        if (jsonObject.getBoolean("status")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ModelSpacialization s = new Gson().fromJson(jsonArray.getString(i), ModelSpacialization.class);
                                for (int j = 0; j < spacializationMedels.size(); j++) {
                                    if (s.getId() == spacializationMedels.get(j).getId())
                                        s.setCheck(true);
                                }
                                spacializations.add(s);
                            }
                            Log.e("LINst", spacializations.size() + "");

                            speci_dropdown = new MultiCheckDropDwon(getContext(), "Select  Specialization", spacializations, new MultiCheckDropDwon.OnItemClickListener() {
                                @Override
                                public void onClick(ArrayList<ModelSpacialization> selected_spaci) {
                                    tv_text_error.setText("");
                                    spacializations = selected_spaci;
                                    String s = "";
                                    if (spacializationMedels != null)
                                        spacializationMedels.clear();
                                    else
                                        spacializationMedels = new ArrayList<>();
                                    for (ModelSpacialization spacialization : selected_spaci) {

                                        if (spacialization.isCheck()) {
                                            SpacializationMedel spacializationMedel = new SpacializationMedel(spacialization.getId(), spacialization.getName());
                                            spacializationMedels.add(spacializationMedel);

                                        }
                                    }

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                                    spacilizationAdaptor = new ProfileSpecializationAdaptor(getContext(), spacializationMedels);
                                    rv_spacilization.setAdapter(spacilizationAdaptor);
                                    rv_spacilization.setLayoutManager(linearLayoutManager);

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
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getPermission() {
        boolean storagePermission = hasPermissions(getContext(), SP);
        boolean cameraPermission = hasPermissions(getContext(), CP);

        Log.v("Permission", String.valueOf(storagePermission));
        Log.v("Permission", String.valueOf(cameraPermission));
        PERMISSIONS.add(SP);
        PERMISSIONS.add(CP);

        if (!storagePermission || !cameraPermission) {
            requestPermissions(PERMISSIONS.toArray(new String[PERMISSIONS.size()]), 2);
        } else {
            selectImage();
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    // getActivity() use so onActivityResult get right requestCode not create by it self
                    getActivity().startActivityForResult(takePicture, ConstantValues.RESULT_CAMERA_OK);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // getActivity() use so onActivityResult get right requestCode not create by it self
                    getActivity().startActivityForResult(pickPhoto, ConstantValues.RESULT_GALLERY_OK);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private final Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0 || grantResults == null) {
            *//*If result is null*//*
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            *//*If We accept permission*//*
            selectImage();
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            *//*If We Decline permission*//*
        }
    }*/

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            userImageSelectorHelper.onActivityResult(requestCode, resultCode, data);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userImageSelectorHelper.onClickPickImage();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
            case R.id.iv_edit_profile:
                userImageSelectorHelper.onClickPickImage();
                //getPermission();
                break;
            case R.id.btn_update_profile:
                saveProfile();
                break;
            case R.id.tv_add_sp:
                showSpecilizeDropdwon();
                break;

        }
    }


    private void updateUserImage(File imageFile, Bitmap imageBitmap) {

        dProgressbar.show();

        loginVM
                .imageUpdate(
                        imageFile
                )
                .observe(this, response -> {
                    dProgressbar.dismiss();


                    try {


                        if (response.getStatus() && response.getData() != null) {
                            storeData.setData(ConstantValues.USER_DATA, new Gson().toJson(response.getData()), new StoreData.SetListener() {
                                @Override
                                public void setOK() {
                                    Glide.with(getActivity()).load(imageBitmap).into(iv_profile);
                                    ((HomeActivity) getContext()).drawerEvents();
                                    getFragmentManager().popBackStack();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                });
    }

    private void uploadPicture(File image) {
        StoreData storeData = new StoreData(getContext());
        DProgressbar dProgressbar = new DProgressbar(getContext());
        dProgressbar.show();
        AndroidNetworking.upload(URLs.SET_PROFILE)
                .addMultipartFile("image", image)
                .addHeaders("Authorization", "Bearer " + storeData.getToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dProgressbar.dismiss();
                        // do anything with response
                        Log.e("response", new Gson().toJson(response));

                        try {


                            if (response.has("status") && response.has("data")) {
                                storeData.setData(ConstantValues.USER_DATA, response.getString("data"), new StoreData.SetListener() {
                                    @Override
                                    public void setOK() {
                                        ((HomeActivity) getContext()).drawerEvents();
                                        getFragmentManager().popBackStack();
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        dProgressbar.dismiss();
                        // handle error
                    }
                });
    }


/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("dk", "dk");
        Log.e("smdksd", resultCode + " " + requestCode);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (requestCode == ConstantValues.RESULT_CAMERA_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        iv_profile.setImageBitmap(selectedImage);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getContext(), selectedImage);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        FileImagePath = new File(getRealPathFromURI(tempUri));
                        uploadPicture();
                    }
                    break;
                case 1:
                    if (requestCode == ConstantValues.RESULT_GALLERY_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Log.e("picturePath", picturePath);
//                                profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                // use glide libery because when image size is bigger image not show
                                Glide.with(this).load(picturePath).into(iv_profile);
                                FileImagePath = new File(picturePath);
                                uploadPicture();
//                                onSaveProfileImage(new File(picturePath));
//                                uploadBitmap( BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
*/

}