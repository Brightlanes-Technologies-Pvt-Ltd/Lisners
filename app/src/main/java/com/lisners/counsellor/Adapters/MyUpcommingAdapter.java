package com.lisners.counsellor.Adapters;//package com.lisners.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class MyUpcommingAdapter extends RecyclerView.Adapter<MyUpcommingAdapter.MyViewHolder> {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<BookedAppointment> joblist;
    private final String TYPE = "";
    MyUpcommingAdapter.OnItemClickListener listener;


    public interface OnItemClickListener {
        void onClick(BookedAppointment jobs, int pos);
    }

    public MyUpcommingAdapter(Context context, MyUpcommingAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;

        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist = new ArrayList<>();
        }
    }


    public MyUpcommingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_appointment, parent, false);
        MyUpcommingAdapter.MyViewHolder holder = new MyUpcommingAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyUpcommingAdapter.MyViewHolder holder, int position) {
        BookedAppointment item = joblist.get(position);
        AppointmentModel appointmentdetails = item.getAppointment_detail();
        User user = item.getUser();
        ArrayList<SpacializationMedel> specializations = item.getSpecialization();
        holder.tvPrice.setVisibility(View.GONE);
        if (item.getDate() != null)
            holder.tv_date.setText(DateUtil.dateFormatter(item.getDate(), "dd-MM-yyyy", "dd MMM yyyy"));
        else {
            holder.tv_date.setText("");
        }
        if (appointmentdetails != null) {
            holder.tv_time_range.setText(String.format("%s - %s", DateUtil.dateFormatter(appointmentdetails.getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(appointmentdetails.getEnd_time(), "HH:mm:ss", "hh:mm a")));
        } else {
            holder.tv_time_range.setText("");
        }
        if (user != null) {
            holder.tv_name.setText(UtilsFunctions.splitCamelCase(user.getName()));
            holder.tvPrice.setText(String.format("₹ %s ", item.getShare_amount()));
            if (user.getProfile_image() != null ) {
                UtilsFunctions.SetLOGO(context, user.getProfile_image(), holder.iv_profile);
                holder.tv_short_name.setVisibility(View.GONE);
            } else
                holder.tv_short_name.setText(UtilsFunctions.getFistLastChar(user.getName()));
        }

        //holder.tv_spacilization.setText(UtilsFunctions.getSpecializeString(specializations));
        holder.tv_spacilization.setText(item.getSpecialization_name());

        holder.status.setText(item.getStatus_label());
        if (item.getStatus() == 3) {
            holder.status_card.setVisibility(View.VISIBLE);
            holder.status_card.setCardBackgroundColor(context.getResources().getColor(R.color.cancel_color));
        } else if (item.getStatus() == 2) {
            holder.status_card.setVisibility(View.VISIBLE);
            holder.status_card.setCardBackgroundColor(context.getResources().getColor(R.color.complete_color));
        } else if (item.getStatus() == 1) {
            holder.status_card.setVisibility(View.VISIBLE);
            holder.status_card.setCardBackgroundColor(context.getResources().getColor(R.color.pending_color));
        } else {
            holder.status_card.setVisibility(View.GONE);
            holder.status_card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }



        if (item.getCall_type().equalsIgnoreCase("video")) {
            holder.ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_svg_video_camera));
        } else {
            holder.ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_svg_phone));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(item, holder.getAdapterPosition());
            }
        });

    }

    public void updateListFirst(ArrayList<BookedAppointment> joblist) {
        this.joblist.clear();
        this.joblist.addAll(joblist);
        notifyDataSetChanged();
    }


    public void updateList(ArrayList<BookedAppointment> joblist) {
        this.joblist.addAll(joblist);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile,ivPhoneIcon;
        TextView tvPrice, tv_name, tv_spacilization, tv_date, tv_time_range, tv_short_name, status;
        CardView status_card;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivPhoneIcon = itemView.findViewById(R.id.iv_phone_icon);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_spacilization = itemView.findViewById(R.id.tv_spacilization);
            tv_time_range = itemView.findViewById(R.id.tv_time_range);
            tv_short_name = itemView.findViewById(R.id.tv_short_name);
            status_card = itemView.findViewById(R.id.status_card);
            status = itemView.findViewById(R.id.status);
        }

    }
}
