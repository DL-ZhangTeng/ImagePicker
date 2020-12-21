package com.zhangteng.imagepicker.utils;

import android.content.Context;
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
     * 获取媒体文件夹
     *
     * @param context
     * @return
     */
    public static String getFilesDir(Context context) {
        String cachePath;
        //isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageEmulated()) {
            cachePath = context.getExternalFilesDir(null).getAbsolutePath();
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 在缓存路径里创建初始文件夹。保存拍摄图片和剪裁后的图片
     *
     * @param filePath 文件夹路径
     */
    public static void createFile(Context context, String filePath) {

        File dir = new File(getFilesDir(context) + filePath);
        File cropFile = new File(getFilesDir(context) + filePath + "/crop");


        if (!cropFile.exists()) {
            cropFile.mkdirs();
        }
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(cropFile, ".nomedia");    // 创建忽视文件。   有该文件，系统将检索不到此文件夹下的图片。
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在缓存路径里保存拍摄图片和剪裁后的图片
     *
     * @param dir 文件夹路径
     * @return 图片绝对路径
     */
    public static String saveBitmap(Context context, String dir, Bitmap b) {
        File f = new File(getFilesDir(context) + dir);
        if (!f.exists()) {
            f.mkdir();
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = "picture_" + dataTake + ".jpg";
        String jpegPath = getFilesDir(context) + dir + File.separator + jpegName;
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
    public static File getCropDir(Context context, String path) {
        File cropFile = new File(getFilesDir(context) + path + "/crop");
        if (!cropFile.exists()) {
            cropFile.mkdirs();
        }
        return cropFile;
    }
}