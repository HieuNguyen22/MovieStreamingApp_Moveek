package com.example.projectmoveek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.R;
import com.example.projectmoveek.model.NotifyModel;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ItemViewNotifyHolder>{

    private Context mContext;
    private List<NotifyModel> mList;
    private DatabaseHelper databaseHelper;

    public NotifyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<NotifyModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewNotifyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false);

        return new ItemViewNotifyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewNotifyHolder holder, int position) {
        NotifyModel item = mList.get(position);
        if(item == null) return;

        holder.tvTitle.setText(item.getTitle());
        holder.tvParagrapth.setText(item.getParagrapth());
        holder.tvTime.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        if(mList != null)
            return mList.size();
        return 0;
    }

    public class ItemViewNotifyHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle, tvParagrapth, tvTime;
        public ItemViewNotifyHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title_noti);
            tvParagrapth = itemView.findViewById(R.id.tv_paragraph_noti);
            tvTime = itemView.findViewById(R.id.tv_time_noti);
        }
    }
}
