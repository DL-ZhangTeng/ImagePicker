package com.zhangteng.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.config.Constant;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerEnum;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePickerConfig imagePickerConfig = new ImagePickerConfig.Builder()
                .imageLoader(new GlideImageLoader()) //图片加载器
                .iHandlerCallBack(new HandlerCallBack())    //图片选择器生命周期监听（直接打开摄像头时无效）
                .multiSelect(true)                 //是否多选
                .isVideoPicker(true)              //是否选择视频 默认false
                .isImagePicker(true)
                .imagePickerType(ImagePickerEnum.PHOTO_PICKER) //选择器打开类型
                .isMirror(false)                              //是否旋转镜头
                .maxImageSelectable(9)                        //图片可选择数
                .maxHeight(1920)                              //图片最大高度
                .maxWidth(1920)                               //图片最大宽度
                .maxImageSize(15)                             //图片最大大小Mb
                .maxVideoLength(5 * 1000)
                .maxVideoSize(180)
                .isCrop(true)
                .pickerThemeColorRes(R.color.colorAccent)
                .pickerTitleColorRes(R.color.image_picker_white)
                .cropThemeColorRes(R.color.colorAccent)
                .cropTitleColorRes(R.color.colorPrimary)
                .pickerBackRes(R.mipmap.image_picker_back_black)
                .build();

        findViewById(R.id.iv).setOnClickListener(v -> {
            ImagePickerOpen.getInstance()
                    .setImagePickerConfig(imagePickerConfig)
                    .open(this, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            ArrayList<String> paths = data.getStringArrayListExtra(Constant.PICKER_PATH);
            new GlideImageLoader().loadImage(this, findViewById(R.id.iv), paths.get(0));
        }
    }
}
