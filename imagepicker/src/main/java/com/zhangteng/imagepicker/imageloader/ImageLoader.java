package com.zhangteng.imagepicker.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Swing on 2018/4/18.
 */
public interface ImageLoader {
    void loadImage(Context context, ImageView imageView, String uri);
}
