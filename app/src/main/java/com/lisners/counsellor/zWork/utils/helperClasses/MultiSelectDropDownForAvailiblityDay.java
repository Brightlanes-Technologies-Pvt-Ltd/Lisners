package com.lisners.counsellor.zWork.utils.helperClasses;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.Adapters.SingleSelectionAdaptar;
import com.lisners.counsellor.ApiModal.ModellistItem;
import com.lisners.counsellor.R;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectDropDownForAvailiblityDay {

    Dialog dialog;
    SelectionAdapter adaptor;

    OnOkClickListener onOkClickListener;

    public interface OnOkClickListener {
        void onClick(List<ModellistItem> list, String text);
    }

    public MultiSelectDropDownForAvailiblityDay(Context context, ArrayList<ModellistItem> industries2, OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailogbox_multidropdown);
        final RecyclerView indview = (RecyclerView) dialog.findViewById(R.id.dialog_list);
        final TextView title = (TextView) dialog.findViewById(R.id.tv_dialogTitle);
        final Button button_ok = (Button) dialog.findViewById(R.id.btn_ok);
        title.setVisibility(View.VISIBLE);
        title.setText("Select Day");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        indview.setLayoutManager(linearLayoutManager);

        /*adaptor = new SingleSelectionAdaptar(context, industries2, new SingleSelectionAdaptar.OnItemClickListener() {
            @Override
            public void onClick(ModellistItem jobs, int pos, boolean type) {

                if (pos == 0) {
                    onAllDayClickListener.onClick(jobs);
                    dialog.dismiss();
                }
            }
        });*/


        adaptor = new SelectionAdapter(industries2, context);


        dialog.setCancelable(false);
        indview.setAdapter(adaptor);
        dialog.show();

        ImageButton close_btn_ind = (ImageButton) dialog.findViewById(R.id.btn_dialog_close);
        close_btn_ind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder text = new StringBuilder();
                List<ModellistItem> list = new ArrayList<>();
                list.clear();
                for (int i = 1; i < adaptor.getResultList().size(); i++) {
                    if (adaptor.resultList.get(i).isSelected()) {
                        list.add(adaptor.resultList.get(i));
                        text.append(adaptor.resultList.get(i).getName());
                        text.append(",");
                    }
                }

                try {
                    text.replace(text.lastIndexOf(","), text.lastIndexOf(",") + 1, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!list.isEmpty()) {
                    onOkClickListener.onClick(list, text.toString());
                } else {
                    onOkClickListener.onClick(list, text.toString());
                }
                dialog.dismiss();

            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }


    private class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.MyViewHolder> {

        private final List<ModellistItem> resultList;
        Context context;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvItem;
            public CheckBox chkSelected;

            public MyViewHolder(View view) {
                super(view);
                tvItem = view.findViewById(R.id.tv_single_item);
                chkSelected = view.findViewById(R.id.chkSelected);

                chkSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        CheckBox cb = (CheckBox) view;
                        ModellistItem cropModel = (ModellistItem) cb.getTag();

                        cropModel.setSelected(cb.isChecked());
                        resultList.get(getAdapterPosition()).setSelected(cb.isChecked());


                        if (getAdapterPosition() == 0) {
                            if (cb.isChecked()) {
                                for (int i = 1; i < resultList.size(); i++) {
                                    resultList.get(i).setSelected(true);
                                    notifyItemChanged(i);
                                }
                            } else {
                                for (int i = 1; i < resultList.size(); i++) {
                                    resultList.get(i).setSelected(false);
                                    notifyItemChanged(i);
                                }
                            }
                        }


                    }
                });



                /*tvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getAdapterPosition() == 0) {
                            for (int i = 1; i < resultList.size(); i++) {
                                resultList.get(i).setSelected(true);
                                notifyItemChanged(i);
                            }

                        }
                    }
                });*/

            }
        }


        public SelectionAdapter(List<ModellistItem> resultList, Context context) {
            this.resultList = resultList;
            this.context = context;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_multi_select_check, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ModellistItem locationSearchModel = resultList.get(position);

            /*if (position == 0) {
                holder.chkSelected.setVisibility(View.INVISIBLE);
                holder.chkSelected.setEnabled(false);
            } else {
                holder.chkSelected.setVisibility(View.VISIBLE);
                holder.chkSelected.setEnabled(true);
            }*/
            holder.tvItem.setText(locationSearchModel.getName());
            holder.chkSelected.setTag(resultList.get(position));
            holder.chkSelected.setChecked(locationSearchModel.isSelected());


        }

        @Override
        public int getItemCount() {
            return resultList.size();
        }


        public List<ModellistItem> getResultList() {
            return resultList;
        }
    }

}
