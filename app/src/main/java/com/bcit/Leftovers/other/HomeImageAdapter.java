package com.bcit.Leftovers.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bcit.Leftovers.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.util.List;


/**
 * Created by Siyuan on 2016/11/22.
 */

public class HomeImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    public static List<Recipe> data;

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
        Log.d("123456789", data.size()+"");
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
        if (data.get(position).getMainImage() != null){
            Glide.with(context)
                    .load(data.get(position).getMainImage())
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(((MyViewHolder) holder).iv);
        }
    }

    @Override
    public int getItemCount() {
        if (data != null){
            return data.size();
        }else{
            return 0;
        }
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
        private CircleTextImageView iv;

        public MyViewHolder(View view) {
            super(view);
            iv = (CircleTextImageView) view.findViewById(R.id.iv);
        }
    }
}
