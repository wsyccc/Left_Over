package com.bcit.Leftovers.other;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bcit.Leftovers.R;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;


public class ImageHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
        imageView.setImageResource(R.drawable.bg_circle);
        Glide.with(context).load(data).into(imageView);
    }
}