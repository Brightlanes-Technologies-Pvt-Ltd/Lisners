package com.lisners.counsellor.zWork.utils.helperClasses;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.PopupWindow;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lisners.counsellor.R;
import com.lisners.counsellor.zWork.utils.config.AppConfig;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImageSelectorHelper {


    public interface ImageSelectorListener {
        void onImageGet(Bitmap imageBitmap, File imageFile);
    }


    private Context context;
    private Context cntxForCropper;
    Fragment fragment;
    private ImageSelectorListener imageSelectorListener;


    private final int SELECT_GALLERY_IMAGE_REQUEST_CODE = 400;
    private final int CAMERA_REQUEST_CODE = 500;
    private PopupWindow popup = null;
    private String mCurrentPhotoPath;


    public ImageSelectorHelper(Context context, ImageSelectorListener imageSelectorListener) {
        this.context = context;
        this.imageSelectorListener = imageSelectorListener;
    }

    public ImageSelectorHelper(Fragment fragment, Context cntxForCropper, Context context, ImageSelectorListener imageSelectorListener) {
        this.context = context;
        this.fragment = fragment;
        this.cntxForCropper = cntxForCropper;
        this.imageSelectorListener = imageSelectorListener;
    }

    public void onClickPickImage() {

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                openUploadOptionPopup();
            }
        } else {
            openUploadOptionPopup();
        }
    }

    private void openUploadOptionPopup() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    try {
                        getCameraImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    ((Activity) context).startActivityForResult(Intent.createChooser(intent, ""), SELECT_GALLERY_IMAGE_REQUEST_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }


    private void getCameraImage() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir   /* directory */
        );

        Uri photoURI = FileProvider.getUriForFile(context,
                AppConfig.APP_AUTHORITY,
                image);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("uri", "" + photoURI);
        Log.e("uri", "" + mCurrentPhotoPath);

        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        ((Activity) context).startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Glide.with(context)
                    .asBitmap()
                    .load(mCurrentPhotoPath)
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher)
                            .fallback(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .override(800, 800))
                    .listener(new RequestListener<Bitmap>() {
                        public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            try {
                                String date = String.valueOf(Calendar.getInstance().getTimeInMillis());
                                String fname = "Image-" + date + ".jpg";
                                File f = new File(context.getCacheDir(), fname);
                                f.createNewFile();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                                byte[] bitmapdata = bos.toByteArray();
                                FileOutputStream fos = new FileOutputStream(f);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();


                                if (fragment != null) {
                                    Uri uri = Uri.fromFile(f);
                                    CropImage.activity(uri)
                                            .setFixAspectRatio(true)
                                            .setOutputUri(uri)
                                            .start(cntxForCropper, fragment);

                                } else {
                                    imageSelectorListener.onImageGet(resource, f);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    }).submit();
        }

        if (requestCode == SELECT_GALLERY_IMAGE_REQUEST_CODE && data != null) {
            Glide.with(context)
                    .asBitmap()
                    .load(data.getData())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher)
                            .fallback(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .override(800, 800))
                    .listener(new RequestListener<Bitmap>() {
                        public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            try {
                                String date = String.valueOf(Calendar.getInstance().getTimeInMillis());
                                String fname = "Image-" + date + ".jpg";
                                File f = new File(context.getCacheDir(), fname);
                                f.createNewFile();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                                byte[] bitmapdata = bos.toByteArray();
                                FileOutputStream fos = new FileOutputStream(f);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();

                                if (fragment != null) {
                                    Uri uri = Uri.fromFile(f);
                                    CropImage.activity(uri)
                                            .setFixAspectRatio(true)
                                            .setOutputUri(uri)
                                            .start(cntxForCropper, fragment);

                                } else {
                                    imageSelectorListener.onImageGet(resource, f);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    }).submit();
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri finalImageResultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), finalImageResultUri);
                    File file = new File(finalImageResultUri.getPath());
                    Log.e("pathOfImage", "====>" + file.getName());
                    imageSelectorListener.onImageGet(bitmap, file);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


}
