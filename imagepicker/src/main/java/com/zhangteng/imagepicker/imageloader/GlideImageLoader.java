package com.zhangteng.imagepicker.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.imagepicker.R;
import com.zhangteng.utils.ImageLoader;

/**
 * Created by Swing on 2018/4/18.
 */
public class GlideImageLoader implements ImageLoader {
    @Override
    public void loadImage(Context context, ImageView imageView, String uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.image_picker_photo)
                        .centerCrop())
                .into(imageView);
    }
}
