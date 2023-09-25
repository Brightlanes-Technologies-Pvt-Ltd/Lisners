package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.ApiModal.ModellistItem;
import com.lisners.counsellor.R;

import java.util.ArrayList;

public class SingleSelectionAdaptar extends RecyclerView.Adapter<SingleSelectionAdaptar.MyViewHolder>  {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<ModellistItem> joblist   ;
    private final String TYPE = "";
    private final int count = 0 ;
    SingleSelectionAdaptar.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(ModellistItem jobs, int pos , boolean type);
    }

    public SingleSelectionAdaptar(Context context, ArrayList<ModellistItem> list, SingleSelectionAdaptar.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist= new ArrayList<>();
            this.joblist.addAll(list);

         }
    }

    public SingleSelectionAdaptar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_single_item  , parent, false);
        SingleSelectionAdaptar.MyViewHolder holder = new SingleSelectionAdaptar.MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleSelectionAdaptar.MyViewHolder holder, int position) {
        ModellistItem item = joblist.get(position);
        holder.txt_item.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(item, holder.getAdapterPosition(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       TextView txt_item ;
          public MyViewHolder(View itemView) {
            super(itemView);
              txt_item = itemView.findViewById(R.id.tv_single_item);

        }

    }
}
