package com.zhangteng.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.yalantis.ucrop.UCrop;
import com.zhangteng.imagepicker.R;
/**
*@ClassName: UcropUtil
*@Description:
*@Author: Swing
*@Date: 2020/5/15 0015 上午 10:27
*/
public class UcropUtil {
    public static void themeTypeCrop(Context context, Fragment fragment, Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(context.getResources().getColor(R.color.image_picker_white));//标题字的颜色以及按钮颜色
        options.setToolbarColor(context.getResources().getColor(R.color.image_picker_theme)); // 设置标题栏颜色
        options.setStatusBarColor(context.getResources().getColor(R.color.image_picker_theme));//设置状态栏颜色
        options.setLogoColor(context.getResources().getColor(R.color.image_picker_theme));
        options.setActiveWidgetColor(context.getResources().getColor(R.color.image_picker_theme));
        options.setHideBottomControls(true);
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(200, 200)
                .withOptions(options)
                .start(context, fragment);
    }

    public static void themeTypeCrop(Activity activity, Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(activity.getResources().getColor(R.color.image_picker_white));//标题字的颜色以及按钮颜色
        options.setToolbarColor(activity.getResources().getColor(R.color.image_picker_theme)); // 设置标题栏颜色
        options.setStatusBarColor(activity.getResources().getColor(R.color.image_picker_theme));//设置状态栏颜色
        options.setLogoColor(activity.getResources().getColor(R.color.image_picker_theme));
        options.setActiveWidgetColor(activity.getResources().getColor(R.color.image_picker_theme));
        options.setHideBottomControls(true);
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(200, 200)
                .withOptions(options)
                .start(activity);
    }

    public static void themeTypeCropToAvatar(Activity activity, Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(activity.getResources().getColor(R.color.image_picker_white));//标题字的颜色以及按钮颜色
        options.setToolbarColor(activity.getResources().getColor(R.color.image_picker_theme)); // 设置标题栏颜色
        options.setStatusBarColor(activity.getResources().getColor(R.color.image_picker_theme));//设置状态栏颜色
        options.setLogoColor(activity.getResources().getColor(R.color.image_picker_theme));
        options.setActiveWidgetColor(activity.getResources().getColor(R.color.image_picker_theme));
        options.setHideBottomControls(true);
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withOptions(options)
                .start(activity);
    }

    public static void themeTypeCrop(Context context, Fragment fragment, Uri sourceUri, Uri destinationUri, float width, float height) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(context.getResources().getColor(R.color.image_picker_white));//标题字的颜色以及按钮颜色
        options.setToolbarColor(context.getResources().getColor(R.color.image_picker_theme)); // 设置标题栏颜色
        options.setStatusBarColor(context.getResources().getColor(R.color.image_picker_theme));//设置状态栏颜色
        options.setLogoColor(context.getResources().getColor(R.color.image_picker_theme));
        options.setActiveWidgetColor(context.getResources().getColor(R.color.image_picker_theme));
        options.setHideBottomControls(true);
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(width, height)
                .withOptions(options)
                .start(context, fragment);
    }

    public static void themeTypeCrop(Activity activity, Uri sourceUri, Uri destinationUri, float width, float height) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(activity.getResources().getColor(R.color.image_picker_white));//标题字的颜色以及按钮颜色
        options.setToolbarColor(activity.getResources().getColor(R.color.image_picker_theme)); // 设置标题栏颜色
        options.setStatusBarColor(activity.getResources().getColor(R.color.image_picker_theme));//设置状态栏颜色
        options.setLogoColor(activity.getResources().getColor(R.color.image_picker_theme));
        options.setActiveWidgetColor(activity.getResources().getColor(R.color.image_picker_theme));
        options.setHideBottomControls(true);
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(width, height)
                .withOptions(options)
                .start(activity);
    }
}
