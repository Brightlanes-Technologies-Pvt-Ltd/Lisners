package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.ModelTimeSlot;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DeleteApiHandler;
import com.lisners.counsellor.utils.DeleteApiVolliy;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityTimeAdaptor extends RecyclerView.Adapter<AvailabilityTimeAdaptor.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    ArrayList<ModelTimeSlot> timeSlots;
    private TimingListWatcher listWatcher;

    public interface TimingListWatcher{
        void onTimingListEmpty();
    }

    public AvailabilityTimeAdaptor(Context context, ArrayList<ModelTimeSlot> list,TimingListWatcher listWatcher) {
        this.context = context;
        this.listWatcher = listWatcher;
        if (context != null) {
            inflater = LayoutInflater.from(context);
            timeSlots = list;
        }
    }

    public AvailabilityTimeAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_availability, parent, false);
        AvailabilityTimeAdaptor.MyViewHolder holder = new AvailabilityTimeAdaptor.MyViewHolder(view);
        return holder;
    }


    public void onClose(ModelTimeSlot slot,int position){
        DeleteApiVolliy apiVolliy = new DeleteApiVolliy(context, URLs.SET_AVAILABLE_DELETE+"/"+slot.getId(), new DeleteApiVolliy.ApiListener() {
            @Override
            public void onResponse(String response) {
                timeSlots.remove(position);
                notifyDataSetChanged();
                if(timeSlots.isEmpty()){
                    listWatcher.onTimingListEmpty();
                }


            }
            @Override
            public void onError() {
                Log.e("sdsd","sds");
                notifyDataSetChanged();
            }
        });
        apiVolliy.callApi();
    }
    public void onUpdateList( ArrayList<ModelTimeSlot>  slots){
        timeSlots.clear();
        timeSlots.addAll(slots);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AvailabilityTimeAdaptor.MyViewHolder holder, int position) {

        ModelTimeSlot slot = timeSlots.get(position);

        String bw = UtilsFunctions.convertTime(slot.getStart_time())+" - "+UtilsFunctions.convertTime(slot.getEnd_time());

        holder.textView.setText(bw);
        holder.pb_progress.setVisibility(View.GONE);
        holder.ib_close.setVisibility(View.VISIBLE);
        holder.ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClose(slot,holder.getAdapterPosition());
                holder.pb_progress.setVisibility(View.VISIBLE);
                holder.ib_close.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
         ImageButton ib_close ;
         ProgressBar pb_progress ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_time);
            ib_close = itemView.findViewById(R.id.ib_close);
            pb_progress = itemView.findViewById(R.id.pb_progress);
        }
    }
}
