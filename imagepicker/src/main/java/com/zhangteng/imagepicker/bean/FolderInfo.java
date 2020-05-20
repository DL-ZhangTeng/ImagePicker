package com.zhangteng.imagepicker.bean;

import java.util.List;

/**
 * Created by Swing on 2018/4/17.
 */
public class FolderInfo {

    private String name;                         // 文件夹名称
    private String path;                         // 文件夹路径
    private ImageInfo imageInfo;                 // 文件夹中第一张图片的信息
    private List<ImageInfo> imageInfoList;       // 文件夹中的图片集合
    private int folderId;

    public FolderInfo() {
    }

    public FolderInfo(String name, String path, ImageInfo imageInfo, List<ImageInfo> imageInfoList) {
        this.name = name;
        this.path = path;
        this.imageInfo = imageInfo;
        this.imageInfoList = imageInfoList;
    }

    public FolderInfo(int folderId, String name, ImageInfo imageInfo, List<ImageInfo> imageInfoList) {
        this.name = name;
        this.imageInfo = imageInfo;
        this.imageInfoList = imageInfoList;
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @Override
    public boolean equals(Object object) {
        try {
            FolderInfo other = (FolderInfo) object;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(object);
    }
}
