package com.zhangteng.imagepicker.config;


/**
 * 媒体选择器弹出类型枚举类
 */
public enum ImagePickerEnum {
    /**
     * BOTH：弹出选择栏
     * PHOTO_PICKER：直接跳转图片选择器
     * CAMERA：直接跳转照相机
     */
    BOTH(0),
    PHOTO_PICKER(1),
    CAMERA(2);

    public int getType() {
        return type;
    }

    private final int type;

    ImagePickerEnum(Integer code) {
        this.type = code;
    }
}
