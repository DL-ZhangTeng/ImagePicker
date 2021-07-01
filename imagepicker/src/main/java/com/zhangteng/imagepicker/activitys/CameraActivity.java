package com.zhangteng.imagepicker.activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.cameralibrary.listener.ClickListener;
import com.zhangteng.imagepicker.cameralibrary.listener.ErrorListener;
import com.zhangteng.imagepicker.cameralibrary.listener.JCameraListener;
import com.zhangteng.imagepicker.config.Constant;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.imagepicker.widget.JCameraView;

import java.util.ArrayList;

/**
 * 录像照相
 */
public class CameraActivity extends AppCompatActivity {
    private JCameraView jCameraView;
    /**
     * BUTTON_STATE_ONLY_CAPTURE = 0x101;      //只能拍照
     * BUTTON_STATE_ONLY_RECORDER = 0x102;     //只能录像
     * BUTTON_STATE_BOTH = 0x103;              //两者都可以
     */
    public int buttonState;

    public int duration;

    public boolean isMirror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonState = getIntent().getIntExtra(Constant.BUTTON_STATE, JCameraView.BUTTON_STATE_BOTH);
        duration = getIntent().getIntExtra(Constant.DURATION, 15 * 1000);
        isMirror = getIntent().getBooleanExtra(Constant.IS_MIRROR, true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.camera_layout);
        jCameraView = findViewById(R.id.jcameraview);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(FileUtils.getVideoDir() + ImagePickerOpen.getInstance().getImagePickerConfig().getFilePath());
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setDuration(duration);
        jCameraView.setMirror(isMirror);
        jCameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Intent intent = new Intent();
                setResult(Constant.CAMERA_ERROR_CODE, intent);
                finishActivityWithAnim();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        if (buttonState != 0) {
            jCameraView.setFeatures(buttonState);
        } else {
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        }
        //JCameraView监听
        if (JCameraView.BUTTON_STATE_ONLY_CAPTURE == buttonState) {//只拍照
            jCameraView.setTip("轻触拍照");
        } else if (JCameraView.BUTTON_STATE_ONLY_RECORDER == buttonState) {//只拍摄
            jCameraView.setTip("长按拍照");
        } else {
            jCameraView.setTip("轻触拍照，长按录制视频");
        }
        jCameraView.setJCameraListener(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtils.saveBitmap(ImagePickerOpen.getInstance().getImagePickerConfig().getFilePath(), bitmap);
                ArrayList<String> paths = new ArrayList<>(1);
                paths.add(path);

                MediaScannerConnection.scanFile(CameraActivity.this, paths.toArray(new String[]{}), new String[]{Constant.MIME_IMAGE}, null);

                Intent intent = new Intent();
                intent.putExtra(Constant.CAMERA_PATH, paths);
                intent.putExtra(Constant.MIME, Constant.MIME_IMAGE);
                intent.putExtra(Constant.HEIGHT, bitmap.getHeight());
                intent.putExtra(Constant.WIDTH, bitmap.getWidth());
                setResult(RESULT_OK, intent);
                finishActivityWithAnim();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                ArrayList<String> paths = new ArrayList<>(1);
                paths.add(url);

                MediaScannerConnection.scanFile(CameraActivity.this, paths.toArray(new String[]{}), new String[]{Constant.MIME_VIDEO}, null);
//                Uri uri;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    uri = FileProvider.getUriForFile(CameraActivity.this, "com.zhangteng.imagepicker.fileprovider", new File(url));
//                } else {
//                    uri = Uri.fromFile(new File(url));
//                }
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Intent intent = new Intent();
                intent.putExtra(Constant.CAMERA_PATH, paths);
                intent.putExtra(Constant.MIME, Constant.MIME_VIDEO);
                intent.putExtra(Constant.HEIGHT, firstFrame.getHeight());
                intent.putExtra(Constant.WIDTH, firstFrame.getWidth());
                setResult(RESULT_OK, intent);
                finishActivityWithAnim();
            }

        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finishActivityWithAnim();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(CameraActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    private void finishActivityWithAnim() {
        finish();
        overridePendingTransition(0, R.anim.camera_push_bottom_out);
    }

    @Override
    public void onBackPressed() {
        finishActivityWithAnim();
    }
}
