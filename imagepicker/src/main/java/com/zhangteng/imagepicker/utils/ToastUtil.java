package com.zhangteng.imagepicker.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    public static void toastShort(Context context, String msg) {
        if (msg.length() > 12) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toastCenterShort(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastCenterLong(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
