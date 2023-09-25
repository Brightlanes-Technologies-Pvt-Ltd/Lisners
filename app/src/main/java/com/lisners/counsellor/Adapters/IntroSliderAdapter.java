package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lisners.counsellor.ApiModal.ModelWelcomeBanner;
import com.lisners.counsellor.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class IntroSliderAdapter extends SliderViewAdapter<IntroSliderAdapter.SliderAdapterVH> {
    private final Context context;
    private ArrayList<ModelWelcomeBanner> mSliderItems = new ArrayList<>();

    public IntroSliderAdapter(Context context, ArrayList<ModelWelcomeBanner> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    public void renewItems(ArrayList<ModelWelcomeBanner> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(ModelWelcomeBanner sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.intro_slide, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        ModelWelcomeBanner modelWelcomeBanner = mSliderItems.get(position);
        viewHolder.itemView.setBackgroundResource(modelWelcomeBanner.getLogo());
        viewHolder.txt_title.setText(modelWelcomeBanner.getTitle());
        viewHolder.txt_des.setText(modelWelcomeBanner.getDes());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        TextView txt_title, txt_des;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.iv_auto_image_slider);
            this.txt_title = itemView.findViewById(R.id.txt_title);
            this.txt_des = itemView.findViewById(R.id.txt_description_slider);
        }
    }

}
