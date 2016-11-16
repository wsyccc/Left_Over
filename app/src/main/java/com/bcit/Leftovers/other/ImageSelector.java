package com.bcit.Leftovers.other;

import android.content.Context;
import android.widget.Toast;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.BaseResultEvent;

/**
 * Created by Siyuan on 2016/11/15.
 */

public class ImageSelector {

    public ImageSelector(final Context context){
        RxGalleryFinal
                .with(context)
                .image()
                .radio()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<BaseResultEvent>() {
                    @Override
                    protected void onEvent(BaseResultEvent baseResultEvent) throws Exception {
                        Toast.makeText(context, " ", Toast.LENGTH_LONG).show();
                    }
                }).openGallery();
    }

}
