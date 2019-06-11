package com.zhangteng.imagepicker.loader;

import android.content.Context;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.bean.FolderInfo;
import com.zhangteng.imagepicker.bean.ImageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swing on 2019/6/10 0010.
 */
public class MediaHandler {

    public static final int ALL_MEDIA_FOLDER = -1;//全部媒体
    public static final int ALL_VIDEO_FOLDER = -2;//全部视频

    /**
     * 对查询到的图片进行聚类（相册分类）
     *
     * @param context
     * @param imageFileList
     * @return
     */
    public static List<FolderInfo> getImageFolder(Context context, ArrayList<ImageInfo> imageFileList) {
        return getFolderInfo(context, imageFileList, null);
    }


    /**
     * 对查询到的视频进行聚类（相册分类）
     *
     * @param context
     * @param imageFileList
     * @return
     */
    public static List<FolderInfo> getVideoFolder(Context context, ArrayList<ImageInfo> imageFileList) {
        return getFolderInfo(context, null, imageFileList);
    }

    /**
     * 对查询到的图片和视频分类
     *
     * @param context
     * @param imageFileList
     * @param videoFileList
     * @return
     */
    public static List<FolderInfo> getFolderInfo(Context context, ArrayList<ImageInfo> imageFileList, ArrayList<ImageInfo> videoFileList) {

        Map<Integer, FolderInfo> folderInfoMap = new HashMap<>();

        //全部图片、视频文件
        ArrayList<ImageInfo> imageInfoList = new ArrayList<>();
        if (imageFileList != null) {
            imageInfoList.addAll(imageFileList);
        }
        if (videoFileList != null) {
            imageInfoList.addAll(videoFileList);
        }

        //对媒体数据进行排序
        Collections.sort(imageInfoList, new Comparator<ImageInfo>() {
            @Override
            public int compare(ImageInfo o1, ImageInfo o2) {
                if (o1.getDateToken() > o2.getDateToken()) {
                    return -1;
                } else if (o1.getDateToken() < o2.getDateToken()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        //全部图片或视频
        if (!imageInfoList.isEmpty()) {
            FolderInfo allFolderInfo = new FolderInfo(ALL_MEDIA_FOLDER,
                    videoFileList == null ?
                            context.getString(R.string.image_picker_all_folder)
                            : context.getString(R.string.image_picker_all_file),
                    imageInfoList.get(0), imageInfoList);
            folderInfoMap.put(ALL_MEDIA_FOLDER, allFolderInfo);
        }

        //全部视频
        if (videoFileList != null && !videoFileList.isEmpty()) {
            FolderInfo allVideoFolder = new FolderInfo(ALL_VIDEO_FOLDER,
                    context.getString(R.string.image_picker_all_video),
                    videoFileList.get(0), videoFileList);
            folderInfoMap.put(ALL_VIDEO_FOLDER, allVideoFolder);
        }

        //对图片进行文件夹分类
        if (imageFileList != null && !imageFileList.isEmpty()) {
            int size = imageFileList.size();
            //添加其他的图片文件夹
            for (int i = 0; i < size; i++) {
                ImageInfo imageInfo = imageFileList.get(i);
                int imageFolderId = imageInfo.getFolderId();
                String folderName = imageInfo.getFolderName();
                FolderInfo folderInfo = folderInfoMap.get(imageFolderId);
                if (folderInfo == null) {
                    folderInfo = new FolderInfo(folderName, imageInfo.getFolderPath(), imageInfo, new ArrayList<ImageInfo>());
                    folderInfo.setFolderId(imageFolderId);
                }
                List<ImageInfo> imageList = folderInfo.getImageInfoList();
                imageList.add(imageInfo);
                folderInfo.setImageInfoList(imageList);
                folderInfoMap.put(imageFolderId, folderInfo);
            }
        }

        //整理聚类数据
        List<FolderInfo> folderInfoList = new ArrayList<>();
        for (Integer folderId : folderInfoMap.keySet()) {
            folderInfoList.add(folderInfoMap.get(folderId));
        }

        //按照图片文件夹的数量排序
        Collections.sort(folderInfoList, new Comparator<FolderInfo>() {
            @Override
            public int compare(FolderInfo o1, FolderInfo o2) {
                if (o1.getImageInfoList().size() > o2.getImageInfoList().size()) {
                    return -1;
                } else if (o1.getImageInfoList().size() < o2.getImageInfoList().size()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return folderInfoList;
    }

}
