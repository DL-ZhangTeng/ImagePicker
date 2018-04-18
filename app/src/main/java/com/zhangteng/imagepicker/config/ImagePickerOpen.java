package com.zhangteng.imagepicker.config;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.zhangteng.imagepicker.activitys.ImagePickerActivity;
import com.zhangteng.imagepicker.utils.FileUtils;

/**
 * Created by swing on 2018/4/18.
 */
public class ImagePickerOpen {
    private static ImagePickerOpen imagePickerOpen;
    private static String TAG = "ImagePicker";
    private ImagePickerConfig imagePickerConfig;

    public static ImagePickerOpen getInstance() {
        if (imagePickerOpen == null) {
            imagePickerOpen = new ImagePickerOpen();
        }
        return imagePickerOpen;
    }


    public void open(Activity mActivity) {
        if (imagePickerOpen.imagePickerConfig == null) {
            Log.e(TAG, "请配置 imagePickerConfig");
            return;
        }
        if (imagePickerOpen.imagePickerConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (TextUtils.isEmpty(imagePickerOpen.imagePickerConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }
        if (imagePickerOpen.imagePickerConfig.getiHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        FileUtils.createFile(imagePickerOpen.imagePickerConfig.getFilePath());

        Intent intent = new Intent(mActivity, ImagePickerActivity.class);
        mActivity.startActivity(intent);
    }

    public void openCamera(Activity mActivity) {
        if (imagePickerOpen.imagePickerConfig == null) {
            Log.e(TAG, "请配置 imagePickerConfig");
            return;
        }
        if (imagePickerOpen.imagePickerConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (TextUtils.isEmpty(imagePickerOpen.imagePickerConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }

        FileUtils.createFile(imagePickerOpen.imagePickerConfig.getFilePath());
        imagePickerOpen.imagePickerConfig.setOpenCamera(true);

        Intent intent = new Intent(mActivity, ImagePickerActivity.class);
        mActivity.startActivity(intent);
    }


    public ImagePickerOpen setImagePickerConfig(ImagePickerConfig imagePickerConfig) {
        this.imagePickerConfig = imagePickerConfig;
        return this;
    }

    public ImagePickerConfig getImagePickerConfig() {
        if (imagePickerConfig == null) {
            imagePickerConfig = new ImagePickerConfig(new ImagePickerConfig.Builder());
        }
        return imagePickerConfig;
    }
}
