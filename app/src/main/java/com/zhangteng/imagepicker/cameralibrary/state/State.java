package com.zhangteng.imagepicker.cameralibrary.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.zhangteng.imagepicker.widget.CameraInterface;

public interface State {

    void start(SurfaceHolder holder, float screenProp);

    void stop();

    void focus(float x, float y, CameraInterface.FocusCallback callback);

    void switich(SurfaceHolder holder, float screenProp);

    void restart();

    void capture(boolean isMirror);

    void record(Surface surface, float screenProp);

    void stopRecord(boolean isShort, long time);

    void cancel(SurfaceHolder holder, float screenProp);

    void confirm();

    void zoom(float zoom, int type);

    void flash(String mode);
}
