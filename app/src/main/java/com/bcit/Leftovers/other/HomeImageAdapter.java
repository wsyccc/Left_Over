package com.bcit.Leftovers.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bcit.Leftovers.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;


/**
 * Created by Siyuan on 2016/11/22.
 */

public class HomeImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private List<Recipe> data;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public HomeImageAdapter(Context context, List<Recipe> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context
        ).inflate(R.layout.home_items, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context)
                .load(data.get(position).getMainImage())
                .crossFade()
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((MyViewHolder) holder).iv);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(v);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageButton iv;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageButton) view.findViewById(R.id.iv);
        }
    }
}
