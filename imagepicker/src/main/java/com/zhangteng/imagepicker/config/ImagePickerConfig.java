package com.zhangteng.imagepicker.config;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.zhangteng.imagepicker.BuildConfig;
import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.imageloader.ImageLoader;
import com.zhangteng.imagepicker.widget.JCameraView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swing on 2018/4/18.
 */
public class ImagePickerConfig {
    /**
     * 图片加载器
     */
    private ImageLoader imageLoader;
    /**
     * 回调
     */
    private IHandlerCallBack iHandlerCallBack;
    /**
     * 是否开启多选  默认 ： false
     */
    private boolean multiSelect;
    /**
     * 最大图片可选择的数目
     */
    private int maxImageSelectable;

    /**
     * 最大视频可选择的数目
     */
    private int maxVideoSelectable;
    /**
     * 是否开启相机 默认：true
     */
    private boolean isShowCamera;
    /**
     * 兼容android 7.0 设置
     */
    private String provider;
    /**
     * 拍照以及截图后 存放的位置。    默认：/imagePicker/Pictures
     */
    private String filePath;
    /**
     * 已选择照片的路径
     */
    private ArrayList<String> pathList;
    /**
     * 是否选择视频  默认：false
     */
    private boolean isVideoPicker;
    /**
     * 是否选择图片  默认：true
     */
    private boolean isImagePicker;
    /**
     * 前置摄像头拍摄是否启用镜像 默认开启
     */
    private boolean isMirror;
    /**
     * 最大图片宽度
     */
    private int maxWidth;
    /**
     * 最大图片高度
     */
    private int maxHeight;

    /**
     * 单位：MB , 默认15MB
     */
    private int maxImageSize;

    /**
     * 单位：毫秒 ,默认15秒(包含15秒)
     */
    private int maxVideoLength;

    /**
     * 单位：MB
     */
    private int maxVideoSize;
    /**
     * 弹出类型
     */
    private ImagePickerEnum imagePickerType;
    /**
     * 是否开启剪裁
     */
    private boolean isCrop;
    /**
     * 剪裁比率（w/h）
     */
    private float cropAspectRatio;
    /**
     * 选择器主题色
     */
    @ColorRes
    private int pickerThemeColorRes;
    /**
     * 剪裁器主题色
     */
    @ColorRes
    private int cropThemeColorRes;
    /**
     * 选择器标题色
     */
    @ColorRes
    private int pickerTitleColorRes;
    /**
     * 剪裁器标题色
     */
    @ColorRes
    private int cropTitleColorRes;

    /**
     * 选择器返回按钮
     */
    @DrawableRes
    private int pickerBackRes;

    public ImagePickerConfig(Builder builder) {
        setBuilder(builder);
    }

    private void setBuilder(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.multiSelect = builder.multiSelect;
        this.maxImageSelectable = builder.maxImageSelectable;
        this.maxVideoSelectable = builder.maxVideoSelectable;
        this.isShowCamera = builder.isShowCamera;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.isVideoPicker = builder.isVideoPicker;
        this.isImagePicker = builder.isImagePicker;
        this.provider = builder.provider;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.isMirror = builder.isMirror;
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.maxImageSize = builder.maxImageSize;
        this.maxVideoLength = builder.maxVideoLength;
        this.maxVideoSize = builder.maxVideoSize;
        this.imagePickerType = builder.imagePickerType;
        this.isCrop = builder.isCrop;
        this.cropAspectRatio = builder.cropAspectRatio;
        this.pickerThemeColorRes = builder.pickerThemeColorRes;
        this.pickerTitleColorRes = builder.pickerTitleColorRes;
        this.cropThemeColorRes = builder.cropThemeColorRes;
        this.cropTitleColorRes = builder.cropTitleColorRes;
        this.pickerBackRes = builder.pickerBackRes;
    }

    public static class Builder implements Serializable {
        private ImageLoader imageLoader = new GlideImageLoader();
        private IHandlerCallBack iHandlerCallBack = new HandlerCallBack();
        private boolean multiSelect = true;
        private int maxImageSelectable = 9;
        private int maxVideoSelectable = 1;
        private boolean isShowCamera = true;
        private String filePath = "/" + BuildConfig.LIBRARY_PACKAGE_NAME + "/imagePicker/ImagePickerPictures";
        private String provider = BuildConfig.LIBRARY_PACKAGE_NAME + ".FileProvider";
        private ArrayList<String> pathList = new ArrayList<>();
        private boolean isVideoPicker = true;
        private boolean isImagePicker = true;
        private boolean isMirror = true;
        private int maxWidth = 1920;
        private int maxHeight = 1920;
        private int maxImageSize = 15;
        private int maxVideoLength = 15000;
        private int maxVideoSize = 45;
        private ImagePickerEnum imagePickerType = ImagePickerEnum.BOTH;
        private boolean isCrop = false;
        private float cropAspectRatio = 0;
        @ColorRes
        private int pickerThemeColorRes = R.color.image_picker_theme;
        @ColorRes
        private int cropThemeColorRes = R.color.image_picker_theme;
        @ColorRes
        private int pickerTitleColorRes = R.color.image_picker_title;
        @ColorRes
        private int cropTitleColorRes = R.color.image_picker_title;
        @DrawableRes
        private int pickerBackRes = R.mipmap.image_picker_back_white;

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
            if (!multiSelect) {
                this.maxImageSelectable = 1;
                this.maxVideoSelectable = 1;
            }
            return this;
        }

        public Builder maxImageSelectable(int maxImageSelectable) {
            this.maxImageSelectable = maxImageSelectable;
            if (!multiSelect) {
                this.maxImageSelectable = 1;
            }
            return this;
        }


        public Builder maxVideoSelectable(int maxVideoSelectable) {
            this.maxVideoSelectable = maxVideoSelectable;
            if (!multiSelect) {
                this.maxVideoSelectable = 1;
            }
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

        public Builder isVideoPicker(boolean isVideoPicker) {
            this.isVideoPicker = isVideoPicker;
            return this;
        }

        public Builder isImagePicker(boolean isImagePicker) {
            this.isImagePicker = isImagePicker;
            return this;
        }

        public Builder isMirror(boolean isMirror) {
            this.isMirror = isMirror;
            return this;
        }

        public Builder maxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder maxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder maxImageSize(int maxImageSize) {
            this.maxImageSize = maxImageSize;
            return this;
        }

        public Builder maxVideoLength(int maxVideoLength) {
            this.maxVideoLength = maxVideoLength;
            return this;
        }

        public Builder maxVideoSize(int maxVideoSize) {
            this.maxVideoSize = maxVideoSize;
            return this;
        }

        public Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public Builder imagePickerType(ImagePickerEnum imagePickerType) {
            this.imagePickerType = imagePickerType;
            return this;
        }

        public Builder isCrop(boolean isCrop) {
            this.isCrop = isCrop;
            return this;
        }

        public Builder isCrop(boolean isCrop, float cropAspectRatio) {
            this.isCrop = isCrop;
            this.cropAspectRatio = cropAspectRatio;
            return this;
        }

        public Builder pickerThemeColorRes(@ColorRes int pickerThemeColorRes) {
            this.pickerThemeColorRes = pickerThemeColorRes;
            return this;
        }

        public Builder cropThemeColorRes(@ColorRes int cropThemeColorRes) {
            this.cropThemeColorRes = cropThemeColorRes;
            return this;
        }

        public Builder pickerTitleColorRes(@ColorRes int pickerTitleColorRes) {
            this.pickerTitleColorRes = pickerTitleColorRes;
            return this;
        }

        public Builder cropTitleColorRes(@ColorRes int cropTitleColorRes) {
            this.cropTitleColorRes = cropTitleColorRes;
            return this;
        }

        public Builder pickerBackRes(@DrawableRes int pickerBackRes) {
            this.pickerBackRes = pickerBackRes;
            return this;
        }

        public ImagePickerConfig build() {
            return new ImagePickerConfig(this);
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

    public int getMaxImageSelectable() {
        return maxImageSelectable;
    }

    public int getMaxVideoSelectable() {
        return maxVideoSelectable;
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

    public boolean isVideoPicker() {
        return isVideoPicker;
    }

    public boolean isImagePicker() {
        return isImagePicker;
    }

    public boolean isMirror() {
        return isMirror;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxImageSize() {
        return maxImageSize;
    }

    public int getMaxVideoLength() {
        return maxVideoLength;
    }

    public int getMaxVideoSize() {
        return maxVideoSize;
    }

    public ImagePickerEnum getImagePickerType() {
        return imagePickerType;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public float getCropAspectRatio() {
        return cropAspectRatio;
    }

    public int getPickerThemeColorRes() {
        return pickerThemeColorRes;
    }

    public int getCropThemeColorRes() {
        return cropThemeColorRes;
    }

    public int getPickerTitleColorRes() {
        return pickerTitleColorRes;
    }

    public int getCropTitleColorRes() {
        return cropTitleColorRes;
    }

    public int getPickerBackRes() {
        return pickerBackRes;
    }

    public int getCameraMediaType() {
        if (isVideoPicker() && !isImagePicker()) {
            return JCameraView.BUTTON_STATE_ONLY_RECORDER;
        } else if (isImagePicker() && !isVideoPicker()) {
            return JCameraView.BUTTON_STATE_ONLY_CAPTURE;
        } else {
            return JCameraView.BUTTON_STATE_BOTH;
        }
    }
}
