package com.lisners.counsellor.Adapters;//package com.lisners.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.AppointmentModel;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.DateUtil;

import java.util.ArrayList;

public class PatientHistoryAdapter extends RecyclerView.Adapter<PatientHistoryAdapter.MyViewHolder> {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<BookedAppointment> joblist;
    private final String TYPE = "";
    PatientHistoryAdapter.OnItemClickListener listener;


    public interface OnItemClickListener {
        void onClick(BookedAppointment jobs, int pos);
    }

    public PatientHistoryAdapter(Context context, PatientHistoryAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;

        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist = new ArrayList<>();
        }
    }


    public PatientHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_patient_call_history_list, parent, false);
        PatientHistoryAdapter.MyViewHolder holder = new PatientHistoryAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final PatientHistoryAdapter.MyViewHolder holder, int position) {
        BookedAppointment item = joblist.get(position);
        AppointmentModel appointmentdetails = item.getAppointment_detail();

        try {
            holder.tv_price.setText("₹ " + item.getShare_amount());
            holder.tv_time.setText(item.getCall_time() + " sec");

            if (item.getDate() != null && appointmentdetails != null) {
                String date = DateUtil.dateFormatter(item.getDate(), "dd-MM-yyyy", "dd MMM yyyy");
                String timeRange = String.format("%s - %s", DateUtil.dateFormatter(appointmentdetails.getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(appointmentdetails.getEnd_time(), "HH:mm:ss", "hh:mm a"));
                holder.tv_date_time.setText(String.format("%s\n%s", date, timeRange));
            } else {
                holder.tv_date_time.setText("");
            }
        } catch (Exception e) {
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

        TextView tv_date_time, tv_price, tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            tv_time = itemView.findViewById(R.id.tv_time);
        }

    }
}
