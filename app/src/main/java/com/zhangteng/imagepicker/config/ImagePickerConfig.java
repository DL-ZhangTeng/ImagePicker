package com.zhangteng.imagepicker.config;

import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.imageloader.ImageLoader;
import com.zhangteng.imagepicker.widget.JCameraView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/18.
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
     * 单位：毫秒 ,默认20秒
     */
    private int maxVideoLength;

    /**
     * 单位：MB
     */
    private int maxVideoSize;
    /**
     *
     */
    private ImagePickerEnum imagePickerType;
    private Builder builder;

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
        this.provider = builder.provider;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.isMirror = builder.isMirror;
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.maxImageSize = builder.maxImageSize;
        this.maxVideoLength = builder.maxVideoLength;
        this.maxVideoSize = builder.maxVideoSize;
        this.imagePickerType = builder.imagePickerType;
        this.builder = builder;
    }

    public static class Builder implements Serializable {

        private static ImagePickerConfig imagePickerConfig;
        private ImageLoader imageLoader = new GlideImageLoader();
        private IHandlerCallBack iHandlerCallBack = new HandlerCallBack();
        private boolean multiSelect = true;
        private int maxImageSelectable = 9;
        private int maxVideoSelectable = 1;
        private boolean isShowCamera = true;
        private String filePath = "/imagePicker/ImagePickerPictures";
        private String provider = "com.zhangteng.imagepicker.fileprovider";
        private ArrayList<String> pathList = new ArrayList<>();
        private boolean isVideoPicker = false;
        private boolean isMirror = false;
        private int maxWidth = 1920;
        private int maxHeight = 1920;
        private int maxImageSize = 15;
        private int maxVideoLength = 20000;
        private int maxVideoSize = 20;
        private ImagePickerEnum imagePickerType = ImagePickerEnum.BOTH;

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

        public Builder multiSelect(boolean multiSelect, int maxImageSelectable) {
            this.multiSelect = multiSelect;
            this.maxImageSelectable = maxImageSelectable;
            return this;
        }

        public Builder maxImageSelectable(int maxImageSelectable) {
            this.maxImageSelectable = maxImageSelectable;
            if (!isVideoPicker) {
                if (maxImageSelectable <= 0) {
                    this.maxVideoSelectable = 1;
                } else {
                    this.maxVideoSelectable = 0;
                }
            }
            return this;
        }


        public Builder maxVideoSelectable(int maxVideoSelectable) {
            this.maxVideoSelectable = maxVideoSelectable;
            if (isVideoPicker) {
                if (maxVideoSelectable <= 0) {
                    this.maxImageSelectable = 9;
                } else {
                    this.maxImageSelectable = 0;
                }
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

    public int getMaxImageSelectable() {
        return maxImageSelectable;
    }

    public void setMaxImageSelectable(int maxImageSelectable) {
        this.maxImageSelectable = maxImageSelectable;
    }

    public int getMaxVideoSelectable() {
        return maxVideoSelectable;
    }

    public void setMaxVideoSelectable(int maxVideoSelectable) {
        this.maxVideoSelectable = maxVideoSelectable;
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

    public void setVideoPicker(boolean videoPicker) {
        isVideoPicker = videoPicker;
    }

    public boolean isMirror() {
        return isMirror;
    }

    public void setMirror(boolean mirror) {
        isMirror = mirror;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public int getMaxVideoLength() {
        return maxVideoLength;
    }

    public void setMaxVideoLength(int maxVideoLength) {
        this.maxVideoLength = maxVideoLength;
    }

    public int getMaxVideoSize() {
        return maxVideoSize;
    }

    public void setMaxVideoSize(int maxVideoSize) {
        this.maxVideoSize = maxVideoSize;
    }

    public ImagePickerEnum getImagePickerType() {
        return imagePickerType;
    }

    public void setImagePickerType(ImagePickerEnum imagePickerType) {
        this.imagePickerType = imagePickerType;
    }

    public int getCameraMediaType() {
        if (getMaxImageSelectable() == 0 && getMaxVideoSelectable() > 0) {
            return JCameraView.BUTTON_STATE_ONLY_RECORDER;
        } else if (getMaxImageSelectable() > 0 && getMaxVideoSelectable() == 0) {
            return JCameraView.BUTTON_STATE_ONLY_CAPTURE;
        }
        return JCameraView.BUTTON_STATE_BOTH;
    }
}
