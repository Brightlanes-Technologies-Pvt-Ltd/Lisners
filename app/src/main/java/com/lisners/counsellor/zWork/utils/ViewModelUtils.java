package com.lisners.counsellor.zWork.utils;






import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;


public class ViewModelUtils {

    public static <T extends AndroidViewModel> T getViewModel(Class<T> clazz, FragmentActivity activity) {
       return ViewModelProviders.of(activity).get(clazz);

    }

    public static <T extends AndroidViewModel> T getViewModel(Class<T> clazz, Fragment fragment) {
        return ViewModelProviders.of(fragment).get(clazz);
    }

}
