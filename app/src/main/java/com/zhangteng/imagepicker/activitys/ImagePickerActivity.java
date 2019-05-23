package com.zhangteng.imagepicker.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.zhangteng.imagepicker.config.ImagePickerEnum;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.widget.FolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
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
    /**
     * 文件夹列表
     */
    private ArrayList<FolderInfo> folderInfos;
    /**
     * 文件列表
     */
    private ArrayList<ImageInfo> imageInfos;
    /**
     * 用于过滤同一个图片
     */
    private HashSet<String> imageInfosDifferent;
    private FolderListAdapter folderListAdapter;
    private ImagePickerAdapter imagePickerAdapter;
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
                Intent intent = new Intent();
                intent.putExtra(Constant.PICKER_PATH, (ArrayList<String>)selectImage);
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
        selectImage = imagePickerConfig.getPathList();
        iHandlerCallBack = imagePickerConfig.getiHandlerCallBack();
        iHandlerCallBack.onStart();
        mContext = this;
        imageInfos = new ArrayList<>();
        folderInfos = new ArrayList<>();
        mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish, 0, imagePickerConfig.isVideoPicker() ? imagePickerConfig.getMaxVideoSelectable() : imagePickerConfig.getMaxImageSelectable()));
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
                ImagePickerOpen.getInstance().openCamera(ImagePickerActivity.this);
                ImagePickerActivity.this.selectImage = selectImage;
            }

            @Override
            public void onImageClick(List<String> selectImage) {
                mTextViewFinish.setText(mContext.getString(R.string.image_picker_finish, selectImage.size(), imagePickerConfig.isVideoPicker() ? imagePickerConfig.getMaxVideoSelectable() : imagePickerConfig.getMaxImageSelectable()));
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
            private final String[] VIDEO_PROJECTION = {
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.MINI_THUMB_MAGIC
            };

            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                if (imagePickerConfig.isVideoPicker()) {
                    if (i == ALL) {
                        return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION, null, null, VIDEO_PROJECTION[2] + " DESC");
                    } else if (i == FODLER) {
                        return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION, VIDEO_PROJECTION[0] + " like '%" + bundle.getString("path") + "%'", null, VIDEO_PROJECTION[2] + " DESC");
                    }
                } else {
                    if (i == ALL) {
                        return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
                    } else if (i == FODLER) {
                        return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%" + bundle.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                    }
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
                            String name = null;
                            String addtime = null;
                            String path = null;
                            int size = 0;
                            String thumb = null;
                            if (imagePickerConfig.isVideoPicker()) {
                                name = cursor.getString(cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
                                addtime = cursor.getString(cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
                                path = cursor.getString(cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                                size = cursor.getInt(cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));
                                thumb = cursor.getString(cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[5]));
                            } else {
                                name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                                addtime = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                                path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                                size = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                            }
                            if (size > 1024 * 5) {
                                if (imageInfosDifferent == null) {
                                    imageInfosDifferent = new HashSet<>();
                                }
                                if (imageInfosDifferent.contains(path)) {
                                    return;
                                } else {
                                    imageInfosDifferent.add(path);
                                }
                                ImageInfo imageInfo = new ImageInfo(name, addtime, path);
                                if (imagePickerConfig.isVideoPicker()) {
                                    imageInfo.setThumbnail(thumb);
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.CAMERA_RESULT_CODE) {
                ArrayList<String> paths;
                paths = data.getStringArrayListExtra(Constant.CAMERA_PATH);
                if (!imagePickerConfig.isMultiSelect()) {
                    selectImage.clear();
                }
                selectImage.addAll(paths);
                iHandlerCallBack.onSuccess(selectImage);
                getSupportLoaderManager().restartLoader(ALL, null, loaderCallbacks);
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
}
