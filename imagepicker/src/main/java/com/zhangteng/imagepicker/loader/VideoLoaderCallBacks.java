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
public class VideoLoaderCallBacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private LoaderCallBacks loaderCallBacks;
    private Context mContext;
    /**
     * 用于过滤同一个图片
     */
    private HashSet<String> imageInfosDifferent;

    public VideoLoaderCallBacks(Context context, LoaderCallBacks loaderCallBacks) {
        this.loaderCallBacks = loaderCallBacks;
        this.mContext = context;
    }

    private final String[] VIDEOPROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.MINI_THUMB_MAGIC,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == Constant.ALL) {
            return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEOPROJECTION, null, null, VIDEOPROJECTION[2] + " DESC");
        } else if (i == Constant.FODLER) {
            return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEOPROJECTION, VIDEOPROJECTION[0] + " like '%" + bundle.getString("path") + "%'", null, VIDEOPROJECTION[2] + " DESC");
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
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[0]));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[1]));
                    String addtime = cursor.getString(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[2]));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[3]));
                    int size = cursor.getInt(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[4]));
                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[5]));
                    String thumb = cursor.getString(cursor.getColumnIndexOrThrow(VIDEOPROJECTION[6]));
                    long duration = cursor.getLong(cursor.getColumnIndex(VIDEOPROJECTION[7]));
                    int folderId = cursor.getInt(cursor.getColumnIndex(VIDEOPROJECTION[8]));
                    String folderName = cursor.getString(cursor.getColumnIndex(VIDEOPROJECTION[9]));
                    long dateToken = cursor.getLong(cursor.getColumnIndex(VIDEOPROJECTION[10]));
                    long width = cursor.getLong(cursor.getColumnIndex(VIDEOPROJECTION[11]));
                    long height = cursor.getLong(cursor.getColumnIndex(VIDEOPROJECTION[12]));
                    if (size >= 5 * 1024
                            && size <= ImagePickerOpen.getInstance().getImagePickerConfig().getMaxVideoSize() * 1024 * 1024
                            && duration <= ImagePickerOpen.getInstance().getImagePickerConfig().getMaxVideoLength() + 999) {
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
                        imageInfo.setDuration(duration);
                        imageInfo.setFolderId(folderId);
                        imageInfo.setFolderName(folderName);
                        imageInfo.setDateToken(dateToken);
                        imageInfo.setWidth(width);
                        imageInfo.setHeight(height);
                        imageInfos1.add(imageInfo);
                    }
                } while (cursor.moveToNext());

                loaderCallBacks.onVideoLoadFinished(imageInfos1, null);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
