package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.ModelSpacialization;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.UtilsFunctions;


import java.util.ArrayList;

public class MultiCheckAdaptar extends RecyclerView.Adapter<MultiCheckAdaptar.MyViewHolder>  {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<ModelSpacialization> joblist   ;
    private final String TYPE = "";
    private final int count = 0 ;
    MultiCheckAdaptar.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(ModelSpacialization jobs, int pos , boolean type);
    }

    public MultiCheckAdaptar(Context context, ArrayList<ModelSpacialization> list, MultiCheckAdaptar.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist= new ArrayList<>();
            this.joblist.addAll(list);

         }
    }

    public MultiCheckAdaptar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_ckeck_item , parent, false);
        MultiCheckAdaptar.MyViewHolder holder = new MultiCheckAdaptar.MyViewHolder(view);
        return holder;
    }

    public void updateList(ArrayList<ModelSpacialization>  list ){
        joblist.clear();
        joblist.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull final MultiCheckAdaptar.MyViewHolder holder, int position) {
        ModelSpacialization item = joblist.get(position);
        holder.txt_item.setText(UtilsFunctions.splitCamelCase(item.getName()));

        holder.txt_item.setChecked(item.isCheck()) ;

        holder.txt_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("CHECK",holder.getAdapterPosition()+" "+isChecked);
                   listener.onClick(item, holder.getAdapterPosition(), isChecked);
                }
        });
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       CheckBox txt_item ;
          public MyViewHolder(View itemView) {
            super(itemView);
              txt_item = itemView.findViewById(R.id.txt_item);

        }

    }
}
