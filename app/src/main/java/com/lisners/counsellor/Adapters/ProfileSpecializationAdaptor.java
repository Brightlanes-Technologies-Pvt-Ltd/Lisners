package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.R;

import java.util.ArrayList;

public class ProfileSpecializationAdaptor extends RecyclerView.Adapter<ProfileSpecializationAdaptor.MyViewHolder> {
    Context context ;
    private LayoutInflater inflater;
    ArrayList<SpacializationMedel> medellist ;


    public ProfileSpecializationAdaptor(Context context, ArrayList<SpacializationMedel> list ) {
        this.context = context;
         if (context != null) {
            inflater = LayoutInflater.from(context);
             medellist =list ;
        }
    }

     public ProfileSpecializationAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = inflater.inflate(R.layout.item_specilization, parent, false);
         ProfileSpecializationAdaptor.MyViewHolder holder = new ProfileSpecializationAdaptor.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ProfileSpecializationAdaptor.MyViewHolder holder,int position) {
        SpacializationMedel spacializationMede = medellist.get(position);
      holder.textView.setText(spacializationMede.getTitle());
      holder.btn_close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              medellist.remove(holder.getAdapterPosition());
              notifyDataSetChanged();
          }
      });
    }

    @Override
    public int getItemCount() {
        return medellist.size();
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
