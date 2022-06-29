package com.example.projectmoveek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectmoveek.R;
import com.example.projectmoveek.model.CastJoinMovieModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastJoinAdapter extends RecyclerView.Adapter<CastJoinAdapter.CastJoinViewHolder>{

    private Context mContext;
    private List<CastJoinMovieModel> mList;

    public CastJoinAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<CastJoinMovieModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastJoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_join_view_holder, parent, false);

        return new CastJoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastJoinViewHolder holder, int position) {
        CastJoinMovieModel model = mList.get(position);

        // Set data
        Glide.with(mContext).load(model.getImage()).into(holder.imgCastAvatar);
        holder.tvCastName.setText(model.getName());
        holder.tvCharName.setText(model.getCharName());

    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public class CastJoinViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgCastAvatar;
        private TextView tvCastName, tvCharName;

        public CastJoinViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCastAvatar = itemView.findViewById(R.id.img_cast_avatar);
            tvCastName = itemView.findViewById(R.id.tv_cast_name);
            tvCharName = itemView.findViewById(R.id.tv_char_name);
        }
    }
}
