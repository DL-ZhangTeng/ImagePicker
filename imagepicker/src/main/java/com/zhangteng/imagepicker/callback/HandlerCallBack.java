package com.zhangteng.imagepicker.callback;

import android.util.Log;

import com.zhangteng.imagepicker.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swing on 2018/4/18.
 */
public class HandlerCallBack implements IHandlerCallBack {
    private String TAG = "---ImagePicker---";
    List<ImageInfo> photoList = new ArrayList<>();

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: 开启");
    }

    @Override
    public void onSuccess(List<ImageInfo> photoList) {
        this.photoList = photoList;
        Log.i(TAG, "onSuccess: 返回数据");
    }

    @Override
    public void onCancel() {
        Log.i(TAG, "onCancel: 取消");
    }

    @Override
    public void onFinish(List<ImageInfo> selectImage) {

    }

    @Override
    public void onError() {
        Log.i(TAG, "onError: 出错");
    }
}
