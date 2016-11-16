package com.bcit.Leftovers.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.Toast;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

/**
 * Created by Siyuan on 2016/11/16.
 */

public class ImageSelector {

    private Activity activity;
    private Context context;
    public ImageSelector(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void init(){
        RxGalleryFinal
                .with(context)
                .cropOvalDimmedLayer(true)
                .crop()
                .radio()
                .image()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Toast.makeText(context, imageRadioResultEvent.getResult().getThumbnailBigPath(), Toast.LENGTH_LONG).show();
                    }
                }).openGallery();
    }
}
