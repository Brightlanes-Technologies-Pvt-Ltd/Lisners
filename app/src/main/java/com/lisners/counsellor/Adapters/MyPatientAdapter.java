package com.lisners.counsellor.Adapters;//package com.lisners.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.UtilsFunctions;

import java.util.ArrayList;

public class MyPatientAdapter extends RecyclerView.Adapter<MyPatientAdapter.MyViewHolder>  {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<BookedAppointment> joblist;
    private final String TYPE = "";
    MyPatientAdapter.OnItemClickListener listener;


    public interface OnItemClickListener {
        void onClick(BookedAppointment jobs, int pos );
    }

    public MyPatientAdapter(Context context, MyPatientAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;

        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist = new ArrayList<>();
        }
    }


    public MyPatientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_patients_list , parent, false);
        MyPatientAdapter.MyViewHolder holder = new MyPatientAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyPatientAdapter.MyViewHolder holder, int position) {
        BookedAppointment item = joblist.get(position);
        User user =  item.getUser();
         if(user!=null){
            holder.tv_name.setText(UtilsFunctions.splitCamelCase(user.getName()));
            UtilsFunctions.SetLOGO(context,user.getProfile_image(),holder.iv_profile);
        }
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(item,holder.getAdapterPosition());
            }
        });

    }
    public void updateListFirst( ArrayList<BookedAppointment> joblist){
        this.joblist.clear();
        this.joblist.addAll(joblist) ;
        notifyDataSetChanged();
    }


    public void updateList( ArrayList<BookedAppointment> joblist){
       this.joblist.addAll(joblist) ;
       notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       ImageView iv_profile ;
        TextView   tv_name ;
          public MyViewHolder(View itemView) {
            super(itemView);
              iv_profile = itemView.findViewById(R.id.iv_profile);
              tv_name = itemView.findViewById(R.id.tv_name);
          }

    }
}
