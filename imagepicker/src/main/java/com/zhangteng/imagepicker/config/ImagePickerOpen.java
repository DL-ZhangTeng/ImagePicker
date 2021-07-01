package com.zhangteng.imagepicker.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.activitys.CameraActivity;
import com.zhangteng.imagepicker.activitys.CameraDialogFragment;
import com.zhangteng.imagepicker.activitys.ImagePickerActivity;
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.imagepicker.utils.NullUtill;
import com.zhangteng.imagepicker.utils.ToastUtil;
import com.zhangteng.imagepicker.widget.JCameraView;

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
                .permission(getPermissions())
                .callback(new Callback() {
                    @Override
                    public void success(Activity permissionActivity) {
                        openNoPermission(mActivity);
                    }

                    @Override
                    public void failure(Activity permissionActivity) {
                        ToastUtil.toastShort(mActivity, getPermissionFailureString(mActivity));
                    }

                    @Override
                    public void nonExecution(Activity permissionActivity) {
                        openNoPermission(mActivity);
                    }
                })
                .build();
        androidPermission.execute();
    }

    /**
     * ImagePickerEnum.PHOTO_PICKER 只启动照片选择
     * ImagePickerEnum.CAMERA 只启动相机
     * 其他 启动下方弹框
     */
    public void open(FragmentActivity mActivity, int requestCode) {
        AndroidPermission androidPermission = new AndroidPermission.Buidler()
                .with(mActivity)
                .permission(getPermissions())
                .callback(new Callback() {
                    @Override
                    public void success(Activity permissionActivity) {
                        openNoPermission(mActivity, requestCode);
                    }

                    @Override
                    public void failure(Activity permissionActivity) {
                        ToastUtil.toastShort(mActivity, getPermissionFailureString(mActivity));
                    }

                    @Override
                    public void nonExecution(Activity permissionActivity) {
                        openNoPermission(mActivity, requestCode);
                    }
                })
                .build();
        androidPermission.execute();
    }

    /**
     * 通过回调获取返回结果
     */
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
     * 通过Intent获取返回结果
     */
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

    /**
     * 获取所需权限数组
     */
    private String[] getPermissions() {
        String[] permissions;
        if (!imagePickerConfig.isShowCamera()) {
            permissions = Permission.Group.STORAGE;
        } else if (imagePickerConfig.getCameraMediaType() == JCameraView.BUTTON_STATE_ONLY_CAPTURE) {
            permissions = new String[]{Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA};
        } else {
            permissions = new String[]{Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.RECORD_AUDIO};
        }
        return permissions;
    }

    /**
     * 获取所需权限失败提示语
     */
    private String getPermissionFailureString(FragmentActivity mActivity) {
        String permissionFailure;
        if (!imagePickerConfig.isShowCamera()) {
            permissionFailure = mActivity.getResources().getString(R.string.image_picker_permission_storage);
        } else if (imagePickerConfig.getCameraMediaType() == JCameraView.BUTTON_STATE_ONLY_CAPTURE) {
            permissionFailure = mActivity.getResources().getString(R.string.image_picker_permission_camera);
        } else {
            permissionFailure = mActivity.getResources().getString(R.string.image_picker_permission_record);
        }
        return permissionFailure;
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
        FileUtils.createFile(imagePickerConfig.getFilePath());

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

    /**
     * @param pathList 默认选中的图片集合
     * @return this
     */
    public ImagePickerOpen pathList(List<String> pathList) {
        imagePickerConfig.getPathList().clear();
        if (!NullUtill.isEmpty(pathList)) {
            imagePickerConfig.getPathList().addAll(pathList);
        }
        return this;
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

