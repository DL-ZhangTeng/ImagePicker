package com.zhangteng.imagepicker.activitys;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.config.Constant;
import com.zhangteng.imagepicker.config.ImagePickerOpen;


/**
 * 选择拍照
 */
public class CameraDialogFragment extends DialogFragment {

    private ImageView ivTakePhoto;
    private ImageView ivPickPhoto;
    private ImageView ivCancel;
    private int cameraRequestCode = Constant.CAMERA_RESULT_CODE;
    private int pickerRequestCode = Constant.PICKER_RESULT_CODE;

    public CameraDialogFragment() {
    }

    public static CameraDialogFragment newInstance(int cameraRequestCode, int pickerRequestCode) {
        CameraDialogFragment cameraDialogFragment = new CameraDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cameraRequestcode", cameraRequestCode);
        bundle.putInt("pickerRequestCode", pickerRequestCode);
        cameraDialogFragment.setArguments(bundle);
        return cameraDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            cameraRequestCode = getArguments().getInt("cameraRequestcode");
            pickerRequestCode = getArguments().getInt("pickerRequestCode");
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.camera_select_photo_popout, null);
        initView(view);
        return view;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.PhotoDialog;
        super.onActivityCreated(arg0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView(View view) {
        ivTakePhoto = view.findViewById(R.id.iv_take_photo);
        ivTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerOpen.getInstance().openCamera(CameraDialogFragment.this.getActivity(), cameraRequestCode);
                dismiss();
            }
        });
        ivPickPhoto = view.findViewById(R.id.iv_pick_photo);
        ivPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerOpen.getInstance().openImagePicker(CameraDialogFragment.this.getActivity(), pickerRequestCode);
                dismiss();
            }
        });
        ivCancel = view.findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
