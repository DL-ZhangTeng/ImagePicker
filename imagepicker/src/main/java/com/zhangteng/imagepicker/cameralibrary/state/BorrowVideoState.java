package com.zhangteng.imagepicker.cameralibrary.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.zhangteng.imagepicker.widget.CameraInterface;
import com.zhangteng.imagepicker.widget.JCameraView;
import com.zhangteng.imagepicker.utils.LogUtil;

public class BorrowVideoState implements State {
    private final String TAG = "BorrowVideoState";
    private CameraMachine machine;

    public BorrowVideoState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void start(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void stop() {

    }

    @Override
    public void focus(float x, float y, CameraInterface.FocusCallback callback) {

    }


    @Override
    public void switich(SurfaceHolder holder, float screenProp) {

    }

    @Override
    public void restart() {

    }

    @Override
    public void capture(boolean isMirror) {

    }

    @Override
    public void record(Surface surface, float screenProp) {

    }

    @Override
    public void stopRecord(boolean isShort, long time) {

    }

    @Override
    public void cancel(SurfaceHolder holder, float screenProp) {
        machine.getView().resetState(JCameraView.TYPE_VIDEO);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void confirm() {
        machine.getView().confirmState(JCameraView.TYPE_VIDEO);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void zoom(float zoom, int type) {
        LogUtil.i(TAG, "zoom");
    }

    @Override
    public void flash(String mode) {

    }
}
