package com.zhangteng.imagepicker.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yalantis.ucrop.UCrop;
import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.adapter.FolderListAdapter;
import com.zhangteng.imagepicker.adapter.ImagePickerAdapter;
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
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.imagepicker.utils.NullUtill;
import com.zhangteng.imagepicker.utils.ScreenUtils;
import com.zhangteng.imagepicker.utils.ToastUtil;
import com.zhangteng.imagepicker.utils.UcropUtil;
import com.zhangteng.imagepicker.widget.FolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImagePickerActivity extends AppCompatActivity implements LoaderCallBacks {
    private RecyclerView mRecyclerViewImageList;
    private LinearLayout mLinearLaoyutBack;
    private TextView mTextViewFolder;
    private ImageView mImageViewFolder;
    private LinearLayout mLLFolder;
    private TextView mTextViewFinish;
    private FolderPopupWindow mFolderPopupWindow;
    private View mStatusView;
    private RelativeLayout mRelativeLayout;
    private ImageView mBackIv;
    private LoaderManager.LoaderCallbacks<Cursor> loaderImageCallbacks;
    private LoaderManager.LoaderCallbacks<Cursor> loaderVideoCallbacks;
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
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x00000000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_image_picker);
        initView();
        initData();
    }

    protected void initView() {
        mRecyclerViewImageList = findViewById(R.id.image_picker_rv_list);
        mRecyclerViewImageList.setLayoutManager(new GridLayoutManager(this, 3));
        mLinearLaoyutBack = findViewById(R.id.image_picker_ll_back);
        mLinearLaoyutBack.setOnClickListener(view -> goBack());
        mLLFolder = findViewById(R.id.image_picker_ll_folder);
        mTextViewFolder = findViewById(R.id.image_picker_tv_folder);
        mImageViewFolder = findViewById(R.id.image_picker_iv_folder);
        mStatusView = findViewById(R.id.image_picker_status);
        mRelativeLayout = findViewById(R.id.image_picker_title);
        mTextViewFinish = findViewById(R.id.image_picker_tv_finish);
        mBackIv = findViewById(R.id.image_picker_back);
        mStatusView.setBackgroundResource(ImagePickerOpen.getInstance().getImagePickerConfig().getPickerThemeColorRes());
        mRelativeLayout.setBackgroundResource(ImagePickerOpen.getInstance().getImagePickerConfig().getPickerThemeColorRes());
        mTextViewFinish.setTextColor(getResources().getColor(ImagePickerOpen.getInstance().getImagePickerConfig().getPickerTitleColorRes()));
        mTextViewFolder.setTextColor(getResources().getColor(ImagePickerOpen.getInstance().getImagePickerConfig().getPickerTitleColorRes()));
        mImageViewFolder.setImageResource(ImagePickerOpen.getInstance().getImagePickerConfig().getPickerFolderRes());
        mBackIv.setImageResource(ImagePickerOpen.getInstance().getImagePickerConfig().getPickerBackRes());
        mLLFolder.setOnClickListener(this::showPopupWindow);
        mTextViewFinish.setOnClickListener(view -> {
            if (NullUtill.isEmpty(selectImageInfo)) {
                if (imagePickerConfig.isVideoPicker() && imagePickerConfig.isImagePicker()) {
                    ToastUtil.toastShort(ImagePickerActivity.this, "请选择文件");
                } else {
                    ToastUtil.toastShort(ImagePickerActivity.this, imagePickerConfig.isVideoPicker() ? "请选择视频" : "请选择图片");
                }
                return;
            }
            if (iHandlerCallBack != null)
                iHandlerCallBack.onSuccess(selectImageInfo);
            ArrayList<String> selectImage = imagePickerConfig.getPathList();
            if (selectImage != null) {
                selectImage.clear();
                for (ImageInfo info : selectImageInfo) {
                    selectImage.add(info.getPath());
                }
                if (imagePickerConfig.isCrop()) {
                    for (int i = selectImageInfo.size() - 1; i >= 0; i--) {
                        File sourceFile = new File(selectImageInfo.get(i).getPath());
                        String name = sourceFile.getName();
                        File parentFile = FileUtils.getCropDir(imagePickerConfig.getFilePath());
                        File destinationFile = new File(parentFile.getAbsoluteFile() + File.separator + UUID.randomUUID() + name);
                        Uri sourceUri = Uri.fromFile(sourceFile);
                        Uri destinationUri = Uri.fromFile(destinationFile);
                        UcropUtil.themeTypeCrop(ImagePickerActivity.this, sourceUri, destinationUri, imagePickerConfig.getCropAspectRatio(), 1);
                    }
                }
            }
            if (!imagePickerConfig.isCrop()) {
                Intent intent = new Intent();
                intent.putExtra(Constant.PICKER_PATH, selectImage);
                setResult(RESULT_OK, intent);
                if (iHandlerCallBack != null) {
                    iHandlerCallBack.onFinish(selectImageInfo);
                }
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

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
        folderListAdapter.setOnItemClickListener((view, position) -> {
            mTextViewFolder.setText(folderInfos.get(position).getName());
            imagePickerAdapter.setImageInfoList(folderInfos.get(position).getImageInfoList());
            if (mFolderPopupWindow != null) {
                mFolderPopupWindow.dismiss();
            }
        });
        imagePickerAdapter = new ImagePickerAdapter(mContext, imageInfos);
        imagePickerAdapter.setOnItemClickListener(new ImagePickerAdapter.OnItemClickListener() {
            @Override
            public void onCameraClick(List<ImageInfo> selectImageInfo) {
                ImagePickerOpen.getInstance().openCamera(ImagePickerActivity.this, Constant.CAMERA_REQUEST_CODE);
                ImagePickerActivity.this.selectImageInfo = selectImageInfo;
            }

            @Override
            public void onImageClick(List<ImageInfo> selectImageInfo, int selectable) {
                ArrayList<String> selectImage = imagePickerConfig.getPathList();
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
                    if (imagePickerConfig.isCrop() && !NullUtill.isEmpty(selectImage) && !NullUtill.isEmpty(selectImageInfo)) {
                        File sourceFile = new File(selectImage.get(0));
                        String name = sourceFile.getName();
                        File parentFile = FileUtils.getCropDir(imagePickerConfig.getFilePath());
                        File destinationFile = new File(parentFile.getAbsoluteFile() + File.separator + UUID.randomUUID() + name);
                        Uri sourceUri = Uri.fromFile(sourceFile);
                        Uri destinationUri = Uri.fromFile(destinationFile);
                        UcropUtil.themeTypeCrop(ImagePickerActivity.this, sourceUri, destinationUri, imagePickerConfig.getCropAspectRatio(), 1);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.PICKER_PATH, selectImage);
                        setResult(RESULT_OK, intent);
                        if (iHandlerCallBack != null) {
                            iHandlerCallBack.onFinish(selectImageInfo);
                        }
                        finish();
                    }
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
        if (imagePickerConfig.isImagePicker() || !imagePickerConfig.isVideoPicker()) {
            loaderImageCallbacks = new ImageLoaderCallBacks(this, this);
            LoaderManager.getInstance(this).restartLoader(Constant.ALL, null, loaderImageCallbacks);
        } else {
            loaderVideoCallbacks = new VideoLoaderCallBacks(this, this);
            LoaderManager.getInstance(this).restartLoader(Constant.ALL, null, loaderVideoCallbacks);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> paths = data.getStringArrayListExtra(Constant.CAMERA_PATH);
                String mime = data.getStringExtra(Constant.MIME);
                long height = data.getIntExtra(Constant.HEIGHT, ScreenUtils.getScreenHeight(this));
                long width = data.getIntExtra(Constant.WIDTH, ScreenUtils.getScreenWidth(this));
                List<String> selectImage = imagePickerConfig.getPathList();
                if (selectImage != null) {
                    selectImage.clear();
                    if (NullUtill.isEmpty(selectImageInfo)) {
                        selectImageInfo = new ArrayList<>();
                    }
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
                    if (imagePickerConfig.isCrop()) {
                        for (int i = selectImageInfo.size() - 1; i >= 0; i--) {
                            File sourceFile = new File(selectImageInfo.get(i).getPath());
                            String name = sourceFile.getName();
                            File parentFile = FileUtils.getCropDir(imagePickerConfig.getFilePath());
                            File destinationFile = new File(parentFile.getAbsoluteFile() + File.separator + UUID.randomUUID() + name);
                            Uri sourceUri = Uri.fromFile(sourceFile);
                            Uri destinationUri = Uri.fromFile(destinationFile);
                            UcropUtil.themeTypeCrop(ImagePickerActivity.this, sourceUri, destinationUri, imagePickerConfig.getCropAspectRatio(), 1);
                        }
                    }
                }
                if (iHandlerCallBack != null)
                    iHandlerCallBack.onSuccess(selectImageInfo);
                if (!imagePickerConfig.isCrop()) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.PICKER_PATH, paths);
                    setResult(RESULT_OK, intent);
                    if (iHandlerCallBack != null) {
                        iHandlerCallBack.onFinish(selectImageInfo);
                    }
                    finish();
                }
            } else if (resultCode == Constant.CAMERA_ERROR_CODE) {
                Toast.makeText(this, "请检查相机权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                updateCropSelectedImage(UCrop.getOutput(data));
            } else {
                updateCropSelectedImage(null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoaderManager.getInstance(this).destroyLoader(Constant.ALL);
    }

    @Override
    public void onImageLoadFinished(ArrayList<ImageInfo> fileList, ArrayList<FolderInfo> folderList) {
        imageInfos.clear();
        imageInfos.addAll(fileList);
        imagePickerAdapter.notifyDataSetChanged();
        folderInfos.clear();
        folderInfos.addAll(MediaHandler.getImageFolder(this, fileList));
        folderListAdapter.notifyDataSetChanged();
        if (imagePickerConfig.isVideoPicker()) {
            loaderVideoCallbacks = new VideoLoaderCallBacks(this, this);
            LoaderManager.getInstance(this).restartLoader(Constant.ALL, null, loaderVideoCallbacks);
        }
    }

    @Override
    public void onVideoLoadFinished(ArrayList<ImageInfo> fileList, ArrayList<FolderInfo> folderList) {
        List<FolderInfo> folderInfoArrayList = MediaHandler.getFolderInfo(this, imagePickerConfig.isImagePicker() ? imageInfos : null, fileList);
        for (int i = 0; i < folderInfoArrayList.size(); i++) {
            FolderInfo folderInfo = folderInfoArrayList.get(i);
            if (folderInfo.getFolderId() == MediaHandler.ALL_MEDIA_FOLDER) {
                imageInfos.clear();
                imageInfos.addAll(folderInfo.getImageInfoList());
                imagePickerAdapter.notifyDataSetChanged();
                break;
            }
        }
        folderInfos.clear();
        folderInfos.addAll(folderInfoArrayList);
        folderListAdapter.notifyDataSetChanged();
    }

    private void updateCropSelectedImage(Uri resultUri) {
        count++;
        if (NullUtill.isEmpty(selectImageInfo)) {
            selectImageInfo = new ArrayList<>();
        }
        if (resultUri != null && resultUri.getPath() != null) {
            String resultName = new File(resultUri.getPath()).getName();
            if (!NullUtill.isEmpty(resultName)) {
                for (ImageInfo imageInfo : selectImageInfo) {
                    if (!NullUtill.isEmpty(imageInfo.getName())
                            && resultName.contains(imageInfo.getName())) {
                        imageInfo.setPath(resultUri.getPath());
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(resultUri.getPath(), options);
                        imageInfo.setWidth(options.outWidth);
                        imageInfo.setHeight(options.outHeight);
                    }
                }
            }
        }
        if (count == selectImageInfo.size()) {
            ArrayList<String> paths = new ArrayList<>();
            for (int i = selectImageInfo.size() - 1; i >= 0; i--) {
                paths.add(selectImageInfo.get(i).getPath());
            }
            Intent intent = new Intent();
            intent.putExtra(Constant.PICKER_PATH, paths);
            setResult(RESULT_OK, intent);
            if (iHandlerCallBack != null) {
                iHandlerCallBack.onFinish(selectImageInfo);
            }
            finish();
        }
    }
}

