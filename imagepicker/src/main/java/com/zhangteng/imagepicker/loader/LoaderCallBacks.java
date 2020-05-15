package com.zhangteng.imagepicker.loader;

import com.zhangteng.imagepicker.bean.FolderInfo;
import com.zhangteng.imagepicker.bean.ImageInfo;

import java.util.ArrayList;

/**
 * Created by Swing on 2019/6/10 0010.
 */
public interface LoaderCallBacks {
    void onImageLoadFinished(ArrayList<ImageInfo> fileList, ArrayList<FolderInfo> folderList);

    void onVideoLoadFinished(ArrayList<ImageInfo> fileList, ArrayList<FolderInfo> folderList);
}
