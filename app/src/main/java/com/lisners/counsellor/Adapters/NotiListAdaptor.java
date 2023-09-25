package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.NotiInfoModel;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.UtilsFunctions;

import java.util.ArrayList;

public class NotiListAdaptor extends RecyclerView.Adapter< NotiListAdaptor.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    ArrayList<NotiInfoModel> list;
    OnItemClickListener listener ;
    public NotiListAdaptor(Context context ,OnItemClickListener clickListener ) {
        this.context = context;
        if (context != null) {
            this.listener = clickListener;
            inflater = LayoutInflater.from(context);
            this.list = new ArrayList<>();
        }
    }

    public interface OnItemClickListener {
        void onClick(NotiInfoModel jobs, int pos );
    }

    public void updateList( ArrayList<NotiInfoModel> lists)
    {
        this.list.addAll(lists);
        notifyDataSetChanged();
    }

    public NotiListAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification, parent, false);
         NotiListAdaptor.MyViewHolder holder = new  NotiListAdaptor.MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotiListAdaptor.MyViewHolder holder, int position) {

        NotiInfoModel infoModel = list.get(position);
         if(infoModel.getData()!=null) {
             holder.tv_title.setText(infoModel.getData().getTitle()+"");
             holder.tv_description.setText(infoModel.getData().getMessage()+"");
             holder.tv_date.setText(infoModel.getDateFormte());
             if(infoModel.getData().getUrl()!=null)
             UtilsFunctions.SetLOGO(context,infoModel.getData().getUrl(),holder.iv_image);
             else
                 holder.iv_image.setVisibility(View.GONE);
         }
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(listener!=null) listener.onClick(infoModel,holder.getAdapterPosition());
             }
         });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_description, tv_date;
        ImageView  iv_image ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_image = itemView.findViewById(R.id.iv_image);

        }
    }
}
