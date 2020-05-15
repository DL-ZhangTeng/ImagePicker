package com.zhangteng.imagepicker.bean;

/**
 * Created by swing on 2018/4/17.
 */
public class ImageInfo {
    private String name;
    private String addTime;
    private String path;
    private String thumbnail;

    private String mime;
    private int folderId;
    private String folderName;
    private String folderPath;
    private long duration;
    private long dateToken;

    private int id;
    private int size;

    private long width;
    private long height;

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

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
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

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public long getDateToken() {
        return dateToken;
    }

    public void setDateToken(long dateToken) {
        this.dateToken = dateToken;
    }
}
