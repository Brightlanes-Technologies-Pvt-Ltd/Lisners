package com.lisners.counsellor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.ApiModal.TransactionCard;
import com.lisners.counsellor.R;

import java.util.ArrayList;

public class TransactionAdaptor extends RecyclerView.Adapter<TransactionAdaptor.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    ArrayList<TransactionCard> list;

    public TransactionAdaptor(Context context) {
        this.context = context;
        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.list = new ArrayList<>();
        }
    }

    public void updateList( ArrayList<TransactionCard> lists)
    {
        this.list.addAll(lists);
        notifyDataSetChanged();
    }

    public  TransactionAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_transaction_history, parent, false);
        TransactionAdaptor.MyViewHolder holder = new  TransactionAdaptor.MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final  TransactionAdaptor.MyViewHolder holder, final int position) {
        TransactionCard appointmentModel = list.get(position);
        holder.tv_transactionName.setText(appointmentModel.getUser_name());
        holder.tv_transactionTime.setText(appointmentModel.getUser_from());
        if(appointmentModel.getCredit()>0) {
            holder.tv_transactionCredit.setText("+" +appointmentModel.getCredit() + "");
            holder.tv_transactionCredit.setVisibility(View.VISIBLE);
            holder.tv_transactionDebit.setVisibility(View.GONE);

        }
        else if(appointmentModel.getDebit()>0){
            if(appointmentModel.getStatus()==1)
            holder.tv_transactionDebit.setText("-" +appointmentModel.getDebit() + "");
            else {
                holder.tv_transactionDebit.setText("-" + appointmentModel.getDebit() + "\nPending");
                holder.tv_transactionDebit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            holder.tv_transactionDebit.setVisibility(View.VISIBLE);
            holder.tv_transactionCredit.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_transactionName, tv_transactionTime, tv_transactionCredit, tv_transactionDebit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_transactionName = itemView.findViewById(R.id.tv_transactionName);
            tv_transactionTime = itemView.findViewById(R.id.tv_transactionTime);
            tv_transactionCredit = itemView.findViewById(R.id.tv_transactionCredit);
            tv_transactionDebit = itemView.findViewById(R.id.tv_transactionDebit);
        }
    }
}
