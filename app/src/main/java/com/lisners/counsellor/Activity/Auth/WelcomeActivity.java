package com.lisners.counsellor.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lisners.counsellor.Adapters.IntroSliderAdapter;
import com.lisners.counsellor.ApiModal.ModelWelcomeBanner;
import com.lisners.counsellor.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    SliderView sliderView;
    Button btn_sign_in, btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        sliderView = findViewById(R.id.imageSlider);
        ArrayList<ModelWelcomeBanner> list = new ArrayList<>();
        ModelWelcomeBanner banner1 = new ModelWelcomeBanner(R.drawable.slide_three, getResources().getString(R.string.slide_1_title), getResources().getString(R.string.slide_1_desc));
        list.add(banner1);
        ModelWelcomeBanner banner2 = new ModelWelcomeBanner(R.drawable.slide_two, getResources().getString(R.string.slide_2_title), getResources().getString(R.string.slide_2_desc));
        list.add(banner2);
        ModelWelcomeBanner banner3 = new ModelWelcomeBanner(R.drawable.slide_four, getResources().getString(R.string.slide_3_title), getResources().getString(R.string.slide_3_desc));
        list.add(banner3);
        IntroSliderAdapter adapter = new IntroSliderAdapter(WelcomeActivity.this, list);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                Intent intentSignIn = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intentSignIn);
                break;
            case R.id.btn_sign_up:
                Intent intentSignUp = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);
                break;
        }
    }
}