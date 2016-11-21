package com.bcit.Leftovers.other;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bcit.Leftovers.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Siyuan on 2016/11/20.
 */

public class Home_Fragment_Info extends
        RecyclerView.Adapter<Home_Fragment_Info.ViewHolder>{

    private List<String> imageUrl;
    private Context context;
    private LayoutInflater inflater;

    public Home_Fragment_Info(Context context, List<String> urls){
        this.context = context;
        this.imageUrl = urls;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_repo, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView1;
        public ImageView imageView2;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView_first);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView_second);
        }
    }
}
