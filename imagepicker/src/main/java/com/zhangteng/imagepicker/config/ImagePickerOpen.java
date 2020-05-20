package com.zhangteng.imagepicker.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.imagepicker.activitys.CameraActivity;
import com.zhangteng.imagepicker.activitys.CameraDialogFragment;
import com.zhangteng.imagepicker.activitys.ImagePickerActivity;
import com.zhangteng.imagepicker.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Swing on 2018/4/18.
 */
public class ImagePickerOpen {
    private static ImagePickerOpen imagePickerOpen;
    private static String TAG = "ImagePicker";
    private ImagePickerConfig imagePickerConfig;
    private CameraDialogFragment cameraDialogFragment;

    public static ImagePickerOpen getInstance() {
        if (imagePickerOpen == null) {
            imagePickerOpen = new ImagePickerOpen();
        }
        return imagePickerOpen;
    }

    /**
     * ImagePickerEnum.PHOTO_PICKER 只启动照片选择
     * ImagePickerEnum.CAMERA 只启动相机
     * 其他 启动下方弹框
     */
    public void open(FragmentActivity mActivity) {
        AndroidPermission androidPermission = new AndroidPermission.Buidler()
                .with(mActivity)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.RECORD_AUDIO)
                .callback(new Callback() {
                    @Override
                    public void success() {
                        openNoPermission(mActivity);
                    }

                    @Override
                    public void failure() {
                        openNoPermission(mActivity);
                    }

                    @Override
                    public void nonExecution() {
                        openNoPermission(mActivity);
                    }
                })
                .build();
        androidPermission.excute();
    }

    private void openNoPermission(FragmentActivity mActivity) {
        if (imagePickerConfig.getImagePickerType() == ImagePickerEnum.PHOTO_PICKER) {
            openImagePicker(mActivity, Constant.PICKER_RESULT_CODE);
        } else if (imagePickerConfig.getImagePickerType() == ImagePickerEnum.CAMERA) {
            openCamera(mActivity, Constant.CAMERA_RESULT_CODE);
        } else {
            if (mActivity != null) {
                if (cameraDialogFragment == null) {
                    cameraDialogFragment = CameraDialogFragment.newInstance(Constant.CAMERA_RESULT_CODE, Constant.PICKER_RESULT_CODE);
                }
                cameraDialogFragment.show(mActivity.getSupportFragmentManager(), "cameraDialogFragment");
            }
        }
    }

    /**
     * ImagePickerEnum.PHOTO_PICKER 只启动照片选择
     * ImagePickerEnum.CAMERA 只启动相机
     * 其他 启动下方弹框
     */
    public void open(FragmentActivity mActivity, int requestCode) {
        AndroidPermission androidPermission = new AndroidPermission.Buidler()
                .with(mActivity)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .callback(new Callback() {
                    @Override
                    public void success() {
                        openNoPermission(mActivity, requestCode);
                    }

                    @Override
                    public void failure() {
                        openNoPermission(mActivity, requestCode);
                    }

                    @Override
                    public void nonExecution() {
                        openNoPermission(mActivity, requestCode);
                    }
                })
                .build();
        androidPermission.excute();
    }

    private void openNoPermission(FragmentActivity mActivity, int requestCode) {
        if (imagePickerConfig.getImagePickerType() == ImagePickerEnum.PHOTO_PICKER) {
            openImagePicker(mActivity, requestCode);
        } else if (imagePickerConfig.getImagePickerType() == ImagePickerEnum.CAMERA) {
            openCamera(mActivity, requestCode);
        } else {
            if (mActivity != null) {
                if (cameraDialogFragment == null) {
                    cameraDialogFragment = CameraDialogFragment.newInstance(requestCode, requestCode);
                }
                cameraDialogFragment.show(mActivity.getSupportFragmentManager(), "cameraDialogFragment");
            }
        }
    }

    public void openCamera(Activity activity, int requestCode) {
        if (imagePickerConfig == null) {
            Log.e(TAG, "请配置 imagePickerConfig");
            return;
        }
        if (imagePickerConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (TextUtils.isEmpty(imagePickerConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }
        if (imagePickerConfig.getiHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, CameraActivity.class);
        intent.putExtra(Constant.BUTTON_STATE, imagePickerConfig.getCameraMediaType());
        intent.putExtra(Constant.DURATION, imagePickerConfig.getMaxVideoLength());
        intent.putExtra(Constant.IS_MIRROR, imagePickerConfig.isMirror());
        activity.startActivityForResult(intent, requestCode);
    }

    public void openImagePicker(Activity activity, int requestCode) {
        if (imagePickerConfig == null) {
            Log.e(TAG, "请配置 imagePickerConfig");
            return;
        }
        if (imagePickerConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (TextUtils.isEmpty(imagePickerConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }
        if (imagePickerConfig.getiHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        FileUtils.createFile(activity, imagePickerConfig.getFilePath());

        Intent intent = new Intent(activity, ImagePickerActivity.class);
        activity.startActivityForResult(intent, requestCode);
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

    public static List<String> getResultData(Context context, int requestCode, int resultCode, @Nullable Intent data) {
        List<String> result = new ArrayList<>();
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.PICKER_RESULT_CODE) {
                result = data.getStringArrayListExtra(Constant.PICKER_PATH);
            } else if (requestCode == Constant.CAMERA_RESULT_CODE) {
                result = data.getStringArrayListExtra(Constant.CAMERA_PATH);
            }
        }
        if (resultCode == Constant.CAMERA_ERROR_CODE) {
            Toast.makeText(context, "请检查相机权限", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

}

