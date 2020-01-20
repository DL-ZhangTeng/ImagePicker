package com.zhangteng.imagepicker.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.adapter.FolderListAdapter;
import com.zhangteng.imagepicker.adapter.ImagePickerAdapter;
import com.zhangteng.imagepicker.base.BaseActivity;
import com.zhangteng.imagepicker.bean.FolderInfo;
import com.zhangteng.imagepicker.bean.ImageInfo;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.Constant;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.loader.ImageLoaderCallBacks;
import com.zhangteng.imagepicker.loader.LoaderCallBacks;
import com.zhangteng.imagepicker.loader.MediaHandler;
import com.zhangteng.imagepicker.loader.VideoLoaderCallBacks;
import com.zhangteng.imagepicker.utils.ScreenUtils;
import com.zhangteng.imagepicker.widget.FolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends BaseActivity implements LoaderCallBacks {
    private RecyclerView mRecyclerViewImageList;
    private LinearLayout mLinearLaoyutBack;
    private TextView mTextViewFolder;
    private TextView mTextViewFinish;
    private FolderPopupWindow mFolderPopupWindow;
    private RelativeLayout mRelativeLayout;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private Context mContext;
    /**
     * 文件夹列表
     */
    private ArrayList<FolderInfo> folderInfos;
    /**
     * 文件列表
     */
    private ArrayList<ImageInfo> imageInfos;

    private FolderListAdapter folderListAdapter;
    private ImagePickerAdapter imagePickerAdapter;
    private ImagePickerConfig imagePickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<ImageInfo> selectImageInfo;

    @Override
    protected int getResourceId() {
        return R.layout.activity_image_picker;
    }

    @Override
    protected void initView() {
        mRecyclerViewImageList = (RecyclerView) findViewById(R.id.image_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new GridLayoutManager(this, 3));
        mLinearLaoyutBack = (LinearLayout) findViewById(R.id.image_picker_ll_back);
        mLinearLaoyutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        mTextViewFolder = (TextView) findViewById(R.id.image_picker_tv_folder);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.image_picker_rv_title);
        mTextViewFinish = (TextView) findViewById(R.id.image_picker_tv_finish);
        mTextViewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(view);
            }
        });
        mTextViewFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectImageInfo == null) {
                    if (imagePickerConfig.isVideoPicker() && imagePickerConfig.isImagePicker()) {
                        showToast("请选择文件");
                    } else {
                        showToast(imagePickerConfig.isImagePicker() ? "请选择图片" : "请选择视频");
                    }
                    return;
                }
                List<String> selectImage = imagePickerConfig.getPathList();
                if (selectImage != null) {
                    selectImage.clear();
                    for (ImageInfo info : selectImageInfo) {
                        selectImage.add(info.getPath());
                    }
                }
                if (iHandlerCallBack != null)
                    iHandlerCallBack.onSuccess(selectImageInfo);
                Intent intent = new Intent();
                intent.putExtra(Constant.PICKER_PATH, (ArrayList<String>) selectImage);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void initData() {
        imagePickerConfig = ImagePickerOpen.getInstance().getImagePickerConfig();
        iHandlerCallBack = imagePickerConfig.getiHandlerCallBack();
        if (iHandlerCallBack != null)
            iHandlerCallBack.onStart();
        mContext = this;
        imageInfos = new ArrayList<>();
        folderInfos = new ArrayList<>();
        mTextViewFolder.setText(mContext.getString(imagePickerConfig.isImagePicker() && imagePickerConfig.isVideoPicker() ? R.string.image_picker_all_file :
                imagePickerConfig.isVideoPicker() ? R.string.image_picker_all_video : R.string.image_picker_all_folder));
        if (!imagePickerConfig.isMultiSelect()) {
            mTextViewFinish.setVisibility(View.GONE);
        }
        mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish));
        folderListAdapter = new FolderListAdapter(mContext, folderInfos);
        folderListAdapter.setOnItemClickListener(new FolderListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mTextViewFolder.setText(folderInfos.get(position).getName());
                imagePickerAdapter.setImageInfoList(folderInfos.get(position).getImageInfoList());
                if (mFolderPopupWindow != null) {
                    mFolderPopupWindow.dismiss();
                }
            }
        });
        imagePickerAdapter = new ImagePickerAdapter(mContext, imageInfos);
        imagePickerAdapter.setOnItemClickListener(new ImagePickerAdapter.OnItemClickListener() {
            @Override
            public void onCameraClick(List<ImageInfo> selectImageInfo) {
                ImagePickerOpen.getInstance().openCamera(ImagePickerActivity.this, Constant.CAMERA_RESULT_CODE);
                ImagePickerActivity.this.selectImageInfo = selectImageInfo;
            }

            @Override
            public void onImageClick(List<ImageInfo> selectImageInfo, int selectable) {
                List<String> selectImage = imagePickerConfig.getPathList();
                if (selectImage != null) {
                    selectImage.clear();
                    for (ImageInfo info : selectImageInfo) {
                        selectImage.add(info.getPath());
                    }
                }
                if (iHandlerCallBack != null)
                    iHandlerCallBack.onSuccess(selectImageInfo);
                ImagePickerActivity.this.selectImageInfo = selectImageInfo;

                if (!imagePickerConfig.isMultiSelect()) {
                    mTextViewFinish.setVisibility(View.GONE);
                    if (iHandlerCallBack != null)
                        iHandlerCallBack.onSuccess(selectImageInfo);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.PICKER_PATH, (ArrayList<String>) selectImage);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    if (selectImageInfo.isEmpty()) {
                        mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish));
                        return;
                    }
                    mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish_, selectImageInfo.size(), selectable));
                }
            }
        });
        mRecyclerViewImageList.setAdapter(imagePickerAdapter);
        if (imagePickerConfig.isImagePicker() && !imagePickerConfig.isVideoPicker()) {
            loaderCallbacks = new ImageLoaderCallBacks(this, this);
            getSupportLoaderManager().restartLoader(Constant.ALL, null, loaderCallbacks);
        } else if (!imagePickerConfig.isImagePicker() && imagePickerConfig.isVideoPicker()) {
            loaderCallbacks = new VideoLoaderCallBacks(this, this);
            getSupportLoaderManager().restartLoader(Constant.ALL, null, loaderCallbacks);
        } else {
            loaderCallbacks = new ImageLoaderCallBacks(this, this);
            getSupportLoaderManager().restartLoader(Constant.ALL, null, loaderCallbacks);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    public void showPopupWindow(View v) {
        if (mFolderPopupWindow == null) {
            if (folderInfos == null) {
                folderInfos = new ArrayList<>();
            }
            mFolderPopupWindow = new FolderPopupWindow(this, folderListAdapter);
        }
        mFolderPopupWindow.showAsDropDown(mRelativeLayout, 0, 0);
    }

    public void goBack() {
        if (iHandlerCallBack != null) {
            iHandlerCallBack.onCancel();
        }
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public void finish() {
        if (iHandlerCallBack != null) {
            iHandlerCallBack.onFinish();
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.CAMERA_RESULT_CODE) {
                ArrayList<String> paths = data.getStringArrayListExtra(Constant.CAMERA_PATH);
                String mime = data.getStringExtra(Constant.MIME);
                long height = data.getIntExtra(Constant.HEIGHT, ScreenUtils.getScreenHeight(this));
                long width = data.getIntExtra(Constant.WIDTH, ScreenUtils.getScreenWidth(this));
                List<String> selectImage = imagePickerConfig.getPathList();
                if (selectImage != null) {
                    selectImage.clear();
                    for (ImageInfo info : selectImageInfo) {
                        selectImage.add(info.getPath());
                    }
                    if (!imagePickerConfig.isMultiSelect()) {
                        selectImageInfo.clear();
                        selectImage.clear();
                    }
                    selectImage.addAll(paths);
                    for (String path : paths) {
                        File imageFile = new File(path);
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setPath(path);
                        imageInfo.setAddTime(String.valueOf(System.currentTimeMillis() / 1000));
                        imageInfo.setMime(mime);
                        imageInfo.setWidth(width);
                        imageInfo.setHeight(height);
                        imageInfo.setName(imageFile.getName());
                        imageInfo.setFolderName(imageFile.getParentFile().getName());
                        imageInfo.setFolderPath(imageFile.getParent());
                        selectImageInfo.add(imageInfo);
                    }
                }
                if (iHandlerCallBack != null)
                    iHandlerCallBack.onSuccess(selectImageInfo);
                getSupportLoaderManager().restartLoader(Constant.ALL, null, loaderCallbacks);
                Intent intent = new Intent();
                intent.putExtra(Constant.PICKER_PATH, paths);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        if (resultCode == Constant.CAMERA_ERROR_CODE) {
            Toast.makeText(this, "请检查相机权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageLoadFinished(ArrayList<ImageInfo> fileList, ArrayList<FolderInfo> folderList) {
        imageInfos.clear();
        imageInfos.addAll(fileList);
        if (imagePickerConfig.isImagePicker() && !imagePickerConfig.isVideoPicker()) {
            folderInfos.clear();
            folderInfos.addAll(MediaHandler.getImageFolder(this, fileList));
            folderListAdapter.notifyDataSetChanged();
            imagePickerAdapter.notifyDataSetChanged();
        } else if (imagePickerConfig.isImagePicker() && imagePickerConfig.isVideoPicker()) {
            loaderCallbacks = new VideoLoaderCallBacks(this, this);
            getSupportLoaderManager().restartLoader(Constant.ALL, null, loaderCallbacks);
        }
    }

    @Override
    public void onVideoLoadFinished(ArrayList<ImageInfo> fileList, ArrayList<FolderInfo> folderList) {
        if (!imagePickerConfig.isImagePicker() && imagePickerConfig.isVideoPicker()) {
            imageInfos.clear();
            imageInfos.addAll(fileList);
            folderInfos.clear();
            folderInfos.addAll(MediaHandler.getVideoFolder(this, fileList));
            folderListAdapter.notifyDataSetChanged();
            imagePickerAdapter.notifyDataSetChanged();
        } else if (imagePickerConfig.isImagePicker() && imagePickerConfig.isVideoPicker()) {
            List<FolderInfo> folderInfoArrayList = MediaHandler.getFolderInfo(this, imageInfos, fileList);
            for (int i = 0; i < folderInfoArrayList.size(); i++) {
                FolderInfo folderInfo = folderInfoArrayList.get(i);
                if (folderInfo.getFolderId() == MediaHandler.ALL_MEDIA_FOLDER) {
                    imageInfos.clear();
                    imageInfos.addAll(folderInfo.getImageInfoList());
                }
            }
            folderInfos.clear();
            folderInfos.addAll(folderInfoArrayList);
            folderListAdapter.notifyDataSetChanged();
            imagePickerAdapter.notifyDataSetChanged();
        }
    }
}
