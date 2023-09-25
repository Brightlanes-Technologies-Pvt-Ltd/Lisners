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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    protected Context context;
    private LayoutInflater inflater;
    private ArrayList<BookedAppointment> joblist;
    private final String TYPE = "";
    ReviewAdapter.OnItemClickListener listener;


    public interface OnItemClickListener {
        void onClick(BookedAppointment jobs, int pos);
    }

    public ReviewAdapter(Context context, ReviewAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;

        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.joblist = new ArrayList<>();
        }
    }


    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review_list, parent, false);
        ReviewAdapter.MyViewHolder holder = new ReviewAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ReviewAdapter.MyViewHolder holder, int position) {
        BookedAppointment item = joblist.get(position);
        User user = item.getUser();

        if (user != null) {
            holder.tv_name.setText(user.getName());
            if (user.getProfile_image() != null ) {
                UtilsFunctions.SetLOGO(context, user.getProfile_image(), holder.iv_profile);
                holder.tv_short_name.setVisibility(View.GONE);
            } else {
                holder.tv_short_name.setText(UtilsFunctions.getFistLastChar(user.getName()));
                holder.tv_short_name.setVisibility(View.VISIBLE);
            }

        }

        holder.tv_ratting.setText(item.getRating());
        if (item.getComment() == null) {
            holder.tv_comment.setText("");
        } else {
            holder.tv_comment.setText(item.getComment());
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
        ImageView iv_profile;
        TextView tvPrice, tv_name, tv_comment, tv_ratting, tv_short_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_short_name = itemView.findViewById(R.id.tv_short_name);
            tv_ratting = itemView.findViewById(R.id.tv_ratting);

        }

    }
}
