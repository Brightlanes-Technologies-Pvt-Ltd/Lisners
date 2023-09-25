package com.lisners.counsellor.zWork.adapter.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.lisners.counsellor.databinding.ItemProgressBinding;


public class FooterViewHolder extends ViewHolder {

    ItemProgressBinding binding;

    public FooterViewHolder(ItemProgressBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    public void bind(boolean isFooterShow) {
        if (isFooterShow) {
            binding.loadMoreProgress.setVisibility(View.VISIBLE);
        } else {
            binding.loadMoreProgress.setVisibility(View.GONE);
        }
    }

}
