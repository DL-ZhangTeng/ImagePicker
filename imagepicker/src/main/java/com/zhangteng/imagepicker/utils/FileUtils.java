package com.zhangteng.imagepicker.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件操作工具类
 */
public class FileUtils {
    /**
     * 删除指定的文件
     *
     * @param filePath 文件路径
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delFile(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 获取图片文件夹
     *
     * @return 文件夹路径
     */
    public static String getPictureDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 获取视频文件夹
     *
     * @return 文件夹路径
     */
    public static String getVideoDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    }

    /**
     * 在缓存路径里创建初始文件夹。保存拍摄图片和剪裁后的图片
     *
     * @param filePath 文件夹路径
     */
    public static void createFile(String filePath) {

        File pictureDir = new File(getPictureDir() + filePath);
        File videoDir = new File(getVideoDir() + filePath);
        File cropFile = new File(getPictureDir() + filePath + "/crop");


        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }
        if (!videoDir.exists()) {
            videoDir.mkdir();
        }
        if (!cropFile.exists()) {
            cropFile.mkdir();
        }
    }

    /**
     * 在缓存路径里保存拍摄图片和剪裁后的图片
     *
     * @param dir 文件夹路径
     * @return 图片绝对路径
     */
    public static String saveBitmap(String dir, Bitmap b) {
        File f = new File(getPictureDir() + dir);
        if (!f.exists()) {
            f.mkdir();
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = "picture_" + dataTake + ".jpg";
        String jpegPath = getPictureDir() + dir + File.separator + jpegName;
        try {
            FileOutputStream fout = new FileOutputStream(jpegPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取缓存路径里的剪裁文件夹
     *
     * @param path 相对路径
     */
    public static File getCropDir(String path) {
        File cropFile = new File(getPictureDir() + path + "/crop");
        if (!cropFile.exists()) {
            cropFile.mkdirs();
        }
        return cropFile;
    }
}