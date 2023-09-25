package com.lisners.counsellor.Adapters;//package com.lisners.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.DateUtil;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder>  {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<BookedAppointment> joblist;
    private final String TYPE = "";
    ReportAdapter.OnItemClickListener listener;


    public interface OnItemClickListener {
        void onClick(BookedAppointment jobs, int pos );
    }

    public ReportAdapter(Context context, ReportAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;

        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist = new ArrayList<>();
        }
    }


    public ReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.report_item , parent, false);
        ReportAdapter.MyViewHolder holder = new ReportAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ReportAdapter.MyViewHolder holder,int position) {
        BookedAppointment item = joblist.get(position);
        User user =  item.getUser();
        ArrayList<SpacializationMedel> specializations = item.getSpecialization();
        AppointmentModel appointmentdetails = item.getAppointment_detail();

        if (item.getDate() != null && appointmentdetails != null) {
            String date = DateUtil.dateFormatter(item.getDate(), "dd-MM-yyyy", "dd MMM yyyy");
            String timeRange = String.format("%s - %s", DateUtil.dateFormatter(appointmentdetails.getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(appointmentdetails.getEnd_time(), "HH:mm:ss", "hh:mm a"));
            holder.tv_date_time.setText(String.format("%s | %s", date, timeRange));
        } else {
            holder.tv_date_time.setText("");
        }

        if(item.getCall_date()!=null) {
            holder.tv_sec_call.setText(item.getCall_time()+"sec");
        }
        if(user!=null){
            holder.tv_name.setText(user.getName());
            holder.tvPrice.setText("₹ " +item.getShare_amount());
           // UtilsFunctions.SetLOGO(context,user.getProfile_image(),holder.iv_profile);
        }

        //holder.tv_spacilization.setText(UtilsFunctions.getSpecializeString(specializations));
        holder.tv_spacilization.setText(item.getSpecialization_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(item,holder.getAdapterPosition());
            }
        });

        if (item.getCall_type().equalsIgnoreCase("video")) {
            holder.ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_svg_video_camera));
        } else {
            holder.ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_svg_phone));
        }

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
       ImageView ivPhoneIcon;
        TextView tvPrice , tv_name ,tv_spacilization , tv_date_time ,tv_sec_call;
          public MyViewHolder(View itemView) {
            super(itemView);
            //  iv_profile = itemView.findViewById(R.id.iv_profile);
              ivPhoneIcon = itemView.findViewById(R.id.iv_phone_icon);
              tvPrice = itemView.findViewById(R.id.tvPrice);
              tv_name = itemView.findViewById(R.id.tv_name);
              tv_spacilization = itemView.findViewById(R.id.tv_spacilization);
              tv_date_time = itemView.findViewById(R.id.tv_date_time);
              tv_sec_call = itemView.findViewById(R.id.tv_sec_call);
        }

    }
}
