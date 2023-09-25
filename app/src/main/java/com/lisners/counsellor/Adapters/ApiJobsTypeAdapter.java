//package com.lisners.Adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.snatchjobs.DataModel.JobModel;
//import com.example.snatchjobs.R;
//import com.lisners.ApiModal.ModelPanding;
//
//import java.util.ArrayList;
//
//public class ApiJobsTypeAdapter extends RecyclerView.Adapter<ApiJobsTypeAdapter.MyViewHolder>  {
//    protected Context context;
//    private LayoutInflater inflater;
//    private ArrayList<ModelPanding> joblist;
//    private String TYPE = "";
//    ApiJobsTypeAdapter.OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onClick(ModelPanding jobs, int pos );
//    }
//
//    public ApiJobsTypeAdapter(Context context, ArrayList<ModelPanding> list, ApiJobsTypeAdapter.OnItemClickListener listener) {
//        this.context = context;
//        this.listener = listener;
//        if (context != null) {
//            inflater = LayoutInflater.from(context);
//            this.joblist = list;
//        }
//    }
//
//
//    public ApiJobsTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.card_job_type , parent, false);
//        ApiJobsTypeAdapter.MyViewHolder holder = new ApiJobsTypeAdapter.MyViewHolder(view);
//        return holder;
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull final ApiJobsTypeAdapter.MyViewHolder holder, final int position) {
//        ModelJobTabItem item = joblist.get(position);
//        holder.view_image.setBackgroundResource(item.getLogo());
//        holder.txt_job_title.setText(item.getTitle());
//        if(item.getCount()>0)
//       {
//           holder.tv_saveJobCount.setVisibility(View.VISIBLE);
//           holder.tv_saveJobCount.setText(item.getCount()+"");
//       }
//     holder.itemView.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View v) {
//             listener.onClick(item , position );
//         }
//     });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return joblist.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        View view_image ;
//        TextView txt_job_title , tv_saveJobCount ;
//          public MyViewHolder(View itemView) {
//            super(itemView);
//              view_image = itemView.findViewById(R.id.view_image);
//              txt_job_title = itemView.findViewById(R.id.txt_job_title);
//              tv_saveJobCount = itemView.findViewById(R.id.tv_saveJobCount);
//        }
//
//    }
//}
