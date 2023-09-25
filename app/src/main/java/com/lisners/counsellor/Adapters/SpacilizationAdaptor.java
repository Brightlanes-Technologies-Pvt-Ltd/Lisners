package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.R;

public class SpacilizationAdaptor extends RecyclerView.Adapter<SpacilizationAdaptor.MyViewHolder> {
    Context context ;
    private LayoutInflater inflater;
    String[] strings ;


    public SpacilizationAdaptor(Context context, String[] list ) {
        this.context = context;
         if (context != null) {
            inflater = LayoutInflater.from(context);
            strings =list ;
        }
    }

     public SpacilizationAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = inflater.inflate(R.layout.item_specilization, parent, false);
         SpacilizationAdaptor.MyViewHolder holder = new SpacilizationAdaptor.MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull final SpacilizationAdaptor.MyViewHolder holder, final int position) {
      holder.textView.setText(strings[position]);
      holder.btn_close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
    }

    @Override
    public int getItemCount() {
        return  strings.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
    TextView textView ;
        ImageButton btn_close ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            btn_close = itemView.findViewById(R.id.btn_close);
        }
    }
}
