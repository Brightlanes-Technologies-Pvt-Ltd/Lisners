package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.ModelFaq;
import com.lisners.counsellor.R;
import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private ArrayList<ModelFaq> imageModelArrayList;
    private final int idx = 0;
    private int lastExpandedPosition = 0;

    public FaqAdapter(Context ctx, ArrayList<ModelFaq> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public  FaqAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_group, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    public void updateList(ArrayList<ModelFaq> faqlist) {
        imageModelArrayList = faqlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final  FaqAdapter.MyViewHolder holder,  int position) {
        ModelFaq modelFaq = imageModelArrayList.get(position);
        holder.tv_title.setText(modelFaq.getFaq_question());
        holder.tv_subTitle.setText(modelFaq.getFaq_answer());

        if (lastExpandedPosition == position) {
            holder.tv_subTitle.setVisibility(View.VISIBLE);
            holder.btn_down.setVisibility(View.GONE);
            holder.btn_up.setVisibility(View.VISIBLE);
        } else {
            holder.tv_subTitle.setVisibility(View.GONE);
            holder.btn_down.setVisibility(View.VISIBLE);
            holder.btn_up.setVisibility(View.GONE);
        }

        holder.fav_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastExpandedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CardView fav_card;
        TextView tv_title, tv_subTitle;
        View btn_down, btn_up;

        public MyViewHolder(View itemView) {
            super(itemView);
            fav_card = (CardView) itemView.findViewById(R.id.fav_card);
            tv_title = (TextView) itemView.findViewById(R.id.group_title);
            tv_subTitle = (TextView) itemView.findViewById(R.id.group_sub_title);
            btn_down = (View) itemView.findViewById(R.id.btn_down);
            btn_up = (View) itemView.findViewById(R.id.btn_up);
        }

    }
}