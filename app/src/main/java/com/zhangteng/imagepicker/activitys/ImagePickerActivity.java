package com.zhangteng.imagepicker.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.adapter.FolderListAdapter;
import com.zhangteng.imagepicker.adapter.ImagePickerAdapter;
import com.zhangteng.imagepicker.base.BaseActivity;
import com.zhangteng.imagepicker.bean.FolderInfo;
import com.zhangteng.imagepicker.bean.ImageInfo;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.imagepicker.widget.FolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends BaseActivity {
    private RecyclerView mRecyclerViewImageList;
    private LinearLayout mLinearLaoyutBack;
    private TextView mTextViewFolder;
    private TextView mTextViewFinish;
    private FolderPopupWindow mFolderPopupWindow;
    private RelativeLayout mRelativeLayout;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private static final int ALL = 0;
    private static final int FODLER = 1;
    private Context mContext;
    private ArrayList<FolderInfo> folderInfos;
    private ArrayList<ImageInfo> imageInfos;
    private FolderListAdapter folderListAdapter;
    private ImagePickerAdapter imagePickerAdapter;
    private int REQUEST_CODE = 100;
    private File cameraTempFile;
    private ImagePickerConfig imagePickerConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> selectImage;

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
                buttonClick(view);
            }
        });
        mTextViewFolder = (TextView) findViewById(R.id.image_picker_tv_folder);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.image_picker_rv_title);
        mTextViewFinish = (TextView) findViewById(R.id.image_picker_tv_finish);
        mTextViewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(view);
            }
        });
        mTextViewFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHandlerCallBack.onSuccess(selectImage);
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
        selectImage = imagePickerConfig.getPathList();
        iHandlerCallBack = imagePickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        if (imagePickerConfig.isOpenCamera()) {
            startCamera();
        }
        mContext = this;
        imageInfos = new ArrayList<>();
        folderInfos = new ArrayList<>();
        mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish, 0, imagePickerConfig.getMaxSize()));
        folderListAdapter = new FolderListAdapter(mContext, folderInfos);
        folderListAdapter.setOnItemClickListener(new FolderListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    mTextViewFolder.setText(mContext.getString(R.string.image_picker_all_folder));
                    imagePickerAdapter.setImageInfoList(imageInfos);
                } else {
                    mTextViewFolder.setText(folderInfos.get(position - 1).getName());
                    imagePickerAdapter.setImageInfoList(folderInfos.get(position - 1).getImageInfoList());
                }
                if (mFolderPopupWindow != null) {
                    mFolderPopupWindow.dismiss();
                }
            }
        });
        imagePickerAdapter = new ImagePickerAdapter(mContext, imageInfos);
        imagePickerAdapter.setOnItemClickListener(new ImagePickerAdapter.OnItemClickListener() {
            @Override
            public void onCameraClick(List<String> selectImage) {
                startCamera();
                ImagePickerActivity.this.selectImage = selectImage;
            }

            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish, selectImage.size(), imagePickerConfig.getMaxSize()));
                iHandlerCallBack.onSuccess(selectImage);
                ImagePickerActivity.this.selectImage = selectImage;
            }
        });
        mRecyclerViewImageList.setAdapter(imagePickerAdapter);
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.SIZE
            };

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                if (i == ALL) {
                    return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
                } else if (i == FODLER) {
                    return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%" + bundle.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                }
                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor != null) {
                    int count = cursor.getCount();
                    if (count > 0) {
                        List<ImageInfo> imageInfos1 = new ArrayList<>();
                        cursor.moveToFirst();
                        do {
                            String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            String addtime = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            int size = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                            if (size > 1024 * 5) {
                                ImageInfo imageInfo = new ImageInfo(name, addtime, path);
                                imageInfos1.add(imageInfo);
                                File file = new File(path);
                                File parent = file.getParentFile();
                                FolderInfo folderInfo = new FolderInfo();
                                folderInfo.setName(parent.getName());
                                folderInfo.setPath(parent.getAbsolutePath());
                                if (!folderInfos.contains(folderInfo)) {
                                    List<ImageInfo> list = new ArrayList<>();
                                    list.add(imageInfo);
                                    folderInfo.setImageInfoList(list);
                                    folderInfo.setImageInfo(list.get(0));
                                    folderInfos.add(folderInfo);
                                } else {
                                    folderInfos.get(folderInfos.indexOf(folderInfo)).getImageInfoList().add(imageInfo);
                                }
                            }
                        } while (cursor.moveToNext());
                        imageInfos.clear();
                        imageInfos.addAll(imageInfos1);

                        folderListAdapter.notifyDataSetChanged();
                        imagePickerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        getSupportLoaderManager().restartLoader(ALL, null, loaderCallbacks);
    }

    @Override
    public void localButtonClick(View v) {
        super.localButtonClick(v);
        if (mFolderPopupWindow == null) {
            if (folderInfos == null) {
                folderInfos = new ArrayList<>();
            }
            mFolderPopupWindow = new FolderPopupWindow(this, folderListAdapter);
        }
        mFolderPopupWindow.showAsDropDown(mRelativeLayout, 0, 0);
    }

    @Override
    public void finish() {
        if (iHandlerCallBack != null) {
            iHandlerCallBack.onFinish();
        }
        super.finish();
    }

    @Override
    public void goBack() {
        iHandlerCallBack.onCancel();
        super.goBack();
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraTempFile = FileUtils.createTmpFile(this, imagePickerConfig.getFilePath());
        String provider = imagePickerConfig.getProvider();
        Uri imageUri = FileProvider.getUriForFile(mContext, provider, cameraTempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (cameraTempFile != null) {
                    if (!imagePickerConfig.isMultiSelect()) {
                        selectImage.clear();
                    }
                    selectImage.add(cameraTempFile.getAbsolutePath());
                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilePath(mContext) + imagePickerConfig.getFilePath()));
                    intent.setData(uri);
                    sendBroadcast(intent);
                    iHandlerCallBack.onSuccess(selectImage);
                    getSupportLoaderManager().restartLoader(ALL, null, loaderCallbacks);
                    finish();
                }
            } else {
                if (cameraTempFile != null && cameraTempFile.exists()) {
                    cameraTempFile.delete();
                }
                if (imagePickerConfig.isOpenCamera()) {
                    finish();
                }
            }
        }
    }
}
