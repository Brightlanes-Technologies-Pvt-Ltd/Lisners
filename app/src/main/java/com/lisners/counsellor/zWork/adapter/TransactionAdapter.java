package com.lisners.counsellor.zWork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.R;
import com.lisners.counsellor.databinding.ItemProgressBinding;
import com.lisners.counsellor.databinding.ItemTransactionHistoryBinding;
import com.lisners.counsellor.zWork.adapter.viewholder.FooterViewHolder;
import com.lisners.counsellor.zWork.restApi.pojo.walletHistory.Datum;
import com.lisners.counsellor.zWork.utils.DateUtil;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<Datum> resultList;
    private final Context context;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public boolean isFooterShow = false;


    public TransactionAdapter(List<Datum> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {

            ItemProgressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_progress, parent, false);
            return new FooterViewHolder(binding);
        } else if (viewType == TYPE_ITEM) {
            ItemTransactionHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_transaction_history, parent, false);
            return new GenericViewHolder(binding);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            ((FooterViewHolder) holder).bind(isFooterShow);

        } else if (holder instanceof GenericViewHolder) {
            ((GenericViewHolder) holder).bind(resultList.get(position));
        }


    }


    @Override
    public int getItemViewType(int position) {
        return (position == resultList.size()) ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        // System.out.println("I am call");
        return resultList.size() + 1;
    }


    /*
      Helpers
      _________________________________________________________________________________________________
       */
    void add(Datum r) {
        resultList.add(r);
        notifyItemInserted(resultList.size() - 1);
    }

    public void addAll(List<Datum> moveResults) {
        for (Datum result : moveResults) {
            add(result);
        }
    }

    public Integer getListSize() {
        return this.resultList.size();
    }


    class GenericViewHolder extends RecyclerView.ViewHolder {

        ItemTransactionHistoryBinding binding;

        GenericViewHolder(ItemTransactionHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });


        }

        void bind(Datum datum) {


            binding.tvTransactionName.setText(datum.getUserName());
            binding.tvTransactionDes.setText(datum.getUserFrom());
            binding.tvTransactionTime.setText(DateUtil.dateFormatter(datum.getCreatedAt(),"dd-MM-yyyy HH:mm:ss","dd MMM yyyy hh:mm a"));


            if (datum.getCredit() > 0) {
                binding.tvTransactionCredit.setText("+" + datum.getCredit() + "");
                binding.tvTransactionCredit.setVisibility(View.VISIBLE);
                binding.tvTransactionDebit.setVisibility(View.GONE);

            } else if (datum.getDebit() > 0) {
                if (datum.getStatus() == 1)
                    binding.tvTransactionDebit.setText("-" + datum.getDebit() + "");
                else {
                    binding.tvTransactionDebit.setText("-" + datum.getDebit() + "\nPending");
                    binding.tvTransactionDebit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                }
                binding.tvTransactionDebit.setVisibility(View.VISIBLE);
                binding.tvTransactionCredit.setVisibility(View.GONE);

            }


        }

    }
    }
