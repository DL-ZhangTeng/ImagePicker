package com.zhangteng.imagepicker.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.zhangteng.imagepicker.bean.ImageInfo;
import com.zhangteng.imagepicker.config.Constant;
import com.zhangteng.imagepicker.config.ImagePickerOpen;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Swing on 2019/6/10 0010.
 */
public class ImageLoaderCallBacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private LoaderCallBacks loaderCallBacks;
    private Context mContext;
    /**
     * 用于过滤同一个图片
     */
    private HashSet<String> imageInfosDifferent;


    public ImageLoaderCallBacks(Context context, LoaderCallBacks loaderCallBacks) {
        this.mContext = context;
        this.loaderCallBacks = loaderCallBacks;
    }

    private final String[] IMAGEPROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == Constant.ALL) {
            return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGEPROJECTION, null, null, IMAGEPROJECTION[2] + " DESC");
        } else if (i == Constant.FODLER) {
            return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGEPROJECTION, IMAGEPROJECTION[0] + " like '%" + bundle.getString("path") + "%'", null, IMAGEPROJECTION[2] + " DESC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                ArrayList<ImageInfo> imageInfos1 = new ArrayList<>();
                cursor.moveToFirst();
                do {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[0]));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[1]));
                    String addtime = cursor.getString(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[2]));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[3]));
                    int size = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[4]));
                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[5]));
                    String thumb = cursor.getString(cursor.getColumnIndexOrThrow(IMAGEPROJECTION[6]));
                    int folderId = cursor.getInt(cursor.getColumnIndex(IMAGEPROJECTION[7]));
                    String folderName = cursor.getString(cursor.getColumnIndex(IMAGEPROJECTION[8]));
                    long dateToken = cursor.getLong(cursor.getColumnIndex(IMAGEPROJECTION[9]));
                    long width = cursor.getLong(cursor.getColumnIndex(IMAGEPROJECTION[10]));
                    long height = cursor.getLong(cursor.getColumnIndex(IMAGEPROJECTION[11]));
                    if (size >= 5 * 1024
                            && size <= ImagePickerOpen.getInstance().getImagePickerConfig().getMaxImageSize() * 1024 * 1024) {
                        if (imageInfosDifferent == null) {
                            imageInfosDifferent = new HashSet<>();
                        }
                        if (imageInfosDifferent.contains(path)) {
                            return;
                        } else {
                            imageInfosDifferent.add(path);
                        }
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setPath(path);
                        imageInfo.setName(name);
                        imageInfo.setAddTime(addtime);
                        imageInfo.setId(id);
                        imageInfo.setSize(size);
                        imageInfo.setMime(mimeType);
                        imageInfo.setThumbnail(thumb);
                        imageInfo.setFolderId(folderId);
                        imageInfo.setFolderName(folderName);
                        imageInfo.setDateToken(dateToken);
                        imageInfo.setWidth(width);
                        imageInfo.setHeight(height);
                        imageInfos1.add(imageInfo);
                    }
                } while (cursor.moveToNext());
                loaderCallBacks.onImageLoadFinished(imageInfos1, null);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
