package com.zhangteng.imagepicker.callback;

import com.zhangteng.imagepicker.bean.ImageInfo;

import java.util.List;

/**
 * Created by Swing on 2018/4/18.
 */
public interface IHandlerCallBack {

    void onStart();

    void onSuccess(List<ImageInfo> selectImage);

    void onCancel();

    void onFinish(List<ImageInfo> selectImage);

    void onError();

}
