package com.zhangteng.imagepicker.bean;

/**
 * Created by swing on 2018/4/17.
 */
public class ImageInfo {
    private String name;
    private String addTime;
    private String path;
    private String thumbnail;

    public ImageInfo() {
    }

    public ImageInfo(String name, String addTime, String path) {
        this.name = name;
        this.addTime = addTime;
        this.path = path;
    }

    public ImageInfo(String name, String addTime, String path, String thumbnail) {
        this.name = name;
        this.addTime = addTime;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
