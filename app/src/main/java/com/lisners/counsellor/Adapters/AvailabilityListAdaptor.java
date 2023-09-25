package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.lisners.counsellor.ApiModal.ModelDaySlot;
import com.lisners.counsellor.ApiModal.ModelTimeSlot;
import com.lisners.counsellor.R;

import java.util.ArrayList;

public class AvailabilityListAdaptor extends RecyclerView.Adapter<AvailabilityListAdaptor.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    ArrayList<ModelDaySlot> modelDaySlots ;

    private AvailabilityTimeAdaptor.TimingListWatcher listWatcher;
    public AvailabilityListAdaptor(Context context, ArrayList<ModelDaySlot> modelDaySlots,AvailabilityTimeAdaptor.TimingListWatcher listWatcher) {
        this.context = context;
        this.listWatcher = listWatcher;

        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.modelDaySlots = modelDaySlots;
        }
    }

    public AvailabilityListAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_availability_list_item, parent, false);
        AvailabilityListAdaptor.MyViewHolder holder = new AvailabilityListAdaptor.MyViewHolder(view);
        return holder;
    }
    public void onUpdateList( ArrayList<ModelDaySlot>  slots){
        modelDaySlots.clear();
        modelDaySlots.addAll(slots);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull final AvailabilityListAdaptor.MyViewHolder holder, final int position) {
        ModelDaySlot daySlot  = modelDaySlots.get(position);
//        if( daySlot.getTime_slot().size()==0) {
//            holder.textView.setVisibility(View.GONE);
//            holder.lv_item.setVisibility(View.GONE);
//        }
        holder.textView.setText(daySlot.getDay());
        holder.availabilityAdaptor.onUpdateList(daySlot.getTime_slot());


    }

    @Override
    public int getItemCount() {
        return modelDaySlots.size();
    }


    public ArrayList<ModelDaySlot> getModelDaySlots() {
        return modelDaySlots;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
         RecyclerView recyclerView ;
         LinearLayout lv_item ;
        AvailabilityTimeAdaptor availabilityAdaptor ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_day);
            recyclerView = itemView.findViewById(R.id.rv_days);
            lv_item = itemView.findViewById(R.id.lv_item);

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);

            /*GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, 2);
            gridLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation*/
            recyclerView.setLayoutManager(layoutManager);
            ArrayList<ModelTimeSlot> modelTimeSlots = new ArrayList<>();

            availabilityAdaptor = new AvailabilityTimeAdaptor(context, modelTimeSlots ,listWatcher);
            recyclerView.setAdapter(availabilityAdaptor);
        }
    }
}
