package com.zhangteng.imagepicker.config;

import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.imageloader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/18.
 */
public class ImagePickerConfig {
    private ImageLoader imageLoader;    // 图片加载器
    private IHandlerCallBack iHandlerCallBack;

    private boolean multiSelect;        // 是否开启多选  默认 ： false
    private int maxSize;                // 配置开启多选时 最大可选择的图片数量。   默认：9
    private boolean isShowCamera;       // 是否开启相机 默认：true
    private String provider;            // 兼容android 7.0 设置
    private String filePath;            // 拍照以及截图后 存放的位置。    默认：/imagePicker/Pictures
    private ArrayList<String> pathList;      // 已选择照片的路径
    private boolean isOpenCamera;             // 是否直接开启相机    默认：false
    private boolean isVideoPicker;
    private Builder builder;

    public ImagePickerConfig(Builder builder) {
        setBuilder(builder);
    }

    private void setBuilder(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.multiSelect = builder.multiSelect;
        this.maxSize = builder.maxSize;
        this.isShowCamera = builder.isShowCamera;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.isOpenCamera = builder.isOpenCamera;
        this.isVideoPicker = builder.isVideoPicker;
        this.provider = builder.provider;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.builder = builder;
    }

    public static class Builder implements Serializable {

        private static ImagePickerConfig imagePickerConfig;

        private ImageLoader imageLoader = new GlideImageLoader();
        private IHandlerCallBack iHandlerCallBack = new HandlerCallBack();

        private boolean multiSelect = true;
        private int maxSize = 9;
        private boolean isShowCamera = true;
        private String filePath = "/imagePicker/ImagePickerPictures";

        private String provider = "com.zhangteng.imagepicker.fileprovider";

        private ArrayList<String> pathList = new ArrayList<>();

        private boolean isOpenCamera = false;

        private boolean isVideoPicker = false;

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder iHandlerCallBack(IHandlerCallBack iHandlerCallBack) {
            this.iHandlerCallBack = iHandlerCallBack;
            return this;
        }

        public Builder imageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }


        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder multiSelect(boolean multiSelect, int maxSize) {
            this.multiSelect = multiSelect;
            this.maxSize = maxSize;
            return this;
        }

        public Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder isShowCamera(boolean isShowCamera) {
            this.isShowCamera = isShowCamera;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder isOpenCamera(boolean isOpenCamera) {
            this.isOpenCamera = isOpenCamera;
            return this;
        }

        public Builder isVideoPicker(boolean isVideoPicker){
            this.isVideoPicker = isVideoPicker;
            return this;
        }

        public Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public ImagePickerConfig build() {
            if (imagePickerConfig == null) {
                imagePickerConfig = new ImagePickerConfig(this);
            } else {
                imagePickerConfig.setBuilder(this);
            }
            return imagePickerConfig;
        }

    }

    public IHandlerCallBack getiHandlerCallBack() {
        return iHandlerCallBack;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public String getProvider() {
        return provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public boolean isOpenCamera() {
        return isOpenCamera;
    }

    public void setOpenCamera(boolean openCamera) {
        isOpenCamera = openCamera;
    }

    public boolean isVideoPicker() {
        return isVideoPicker;
    }

    public void setVideoPicker(boolean videoPicker) {
        isVideoPicker = videoPicker;
    }
}
