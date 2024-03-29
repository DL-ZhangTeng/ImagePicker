package com.zhangteng.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerEnum;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePickerConfig imagePickerConfig = new ImagePickerConfig.Builder()
                .provider(getPackageName() + ".FileProvider")
                .imageLoader(new GlideImageLoader()) //图片加载器
                .iHandlerCallBack(new HandlerCallBack())    //图片选择器生命周期监听（直接打开摄像头时无效）
                .multiSelect(true)                 //是否多选
                .isShowCamera(true)
                .isVideoPicker(true)              //是否选择视频 默认false
                .isImagePicker(true)
                .imagePickerType(ImagePickerEnum.CAMERA) //选择器打开类型
                .isMirror(false)                              //是否旋转镜头
                .maxImageSelectable(9)                        //图片可选择数
                .maxHeight(1920)                              //图片最大高度
                .maxWidth(1920)                               //图片最大宽度
                .maxImageSize(15)                             //图片最大大小Mb
                .maxVideoLength(5 * 1000)
                .maxVideoSize(180)
                .isCrop(true)
                .pathList(new ArrayList<>())
                .pickerThemeColorRes(R.color.image_picker_white)
                .pickerTitleColorRes(R.color.image_picker_text_black)
                .cropThemeColorRes(R.color.image_picker_white)
                .cropTitleColorRes(R.color.image_picker_text_black)
                .pickerBackRes(R.mipmap.image_picker_back_black)
                .pickerFolderRes(R.mipmap.image_picker_folder_black)
                .build();

        findViewById(R.id.iv).setOnClickListener(v -> {
//            imagePickerConfig.getPathList().clear();
            ImagePickerOpen.getInstance()
                    .setImagePickerConfig(imagePickerConfig)
                    .pathList(new ArrayList<>())
                    .open(this, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            List<String> paths = ImagePickerOpen.getResultData(this, resultCode, data);
            ImagePickerOpen.getInstance().getImagePickerConfig().getImageLoader().loadImage(this, findViewById(R.id.iv), paths.get(0));
        }
    }
}
