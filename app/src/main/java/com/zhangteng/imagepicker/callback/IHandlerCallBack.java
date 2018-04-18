package com.zhangteng.imagepicker.callback;

import java.util.List;

/**
 * Created by swing on 2018/4/18.
 */
public interface IHandlerCallBack {

    void onStart();

    void onSuccess(List<String> selectImage);

    void onCancel();

    void onFinish();

    void onError();

}
