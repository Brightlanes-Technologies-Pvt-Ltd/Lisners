package com.lisners.counsellor.zWork.utils.helperClasses;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lisners.counsellor.R;


public class ConnectionMessageLayout {


    public static void showLayout(TextView textView, Context context) {
        textView.setText("No Internet");
        textView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        showView(textView);
    }

    public static void closeLayout(final TextView textView, Context context) {
        if (textView.getVisibility() == View.VISIBLE) {
            textView.setText("Internet Connected");
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */
                @Override
                public void run() {
                    closeView(textView);


                }

            }, 500);
        }
    }


    private static void closeView(final TextView textView) {
        int finalHeight = textView.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, textView);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                // Height=0, but it set visibility to GONE
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private static void showView(TextView textView) {
        textView.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, textView.getMeasuredHeight(), textView);
        mAnimator.start();
    }

    private static ValueAnimator slideAnimator(int start, int end, final TextView textView) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = textView
                        .getLayoutParams();
                layoutParams.height = value;
                textView.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}