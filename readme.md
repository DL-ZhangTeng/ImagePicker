# 图片选择器ImagePicker

ImagePicker是一个Android版本的图片视频选择组件。
[GitHub仓库地址](https://github.com/DL-ZhangTeng/ImagePicker)

## 引入

### gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.DL-ZhangTeng:ImagePicker:1.5.0'
//使用的三方
implementation 'com.github.bumptech.glide:glide:4.12.0'
implementation 'com.yalantis:ucrop:2.2.0'
implementation 'com.github.DL-ZhangTeng:RequestPermission:1.3.0'
implementation 'com.github.DL-ZhangTeng:Utils:2.2.0'
```

## 效果图

![图片选择器1](https://img-blog.csdnimg.cn/20200820102910783.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70#pic_center)
![图片选择器2](https://img-blog.csdnimg.cn/20200820102951969.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70#pic_center)
![图片选择器3](https://img-blog.csdnimg.cn/20200820103007467.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70#pic_center)

## 属性

| 属性名                 | 描述                                                                               |
|---------------------|----------------------------------------------------------------------------------|
| multiSelect         | 是否单选，默认true                                                                      |
| maxImageSelectable  | 多选时最多选多少张，默认9                                                                    |
| maxVideoSelectable  | 多选时最多选多少个，默认1                                                                    |
| isShowCamera        | 是否在第一格显示拍照录制，默认true                                                              |
| filePath            | 图片剪裁保持地址，默认"/" + BuildConfig.APPLICATION_ID + "/imagePicker/ImagePickerPictures" |
| provider            | 文件提供者，默认BuildConfig.APPLICATION_ID + ".FileProvider"                             |
| pathList            | 图片保持List                                                                         |
| isVideoPicker       | 是否可选择视频，默认true                                                                   |
| isImagePicker       | 是否可选择图片，默认true                                                                   |
| isMirror            | 摄像拍照是否镜像                                                                         |
| maxWidth            | 最大图片宽度，默认1920                                                                    |
| maxHeight           | 最大图片高度，默认1920                                                                    |
| maxImageSize        | 图片大小，默认15M                                                                       |
| maxVideoLength      | 视频长度，默认15000ms                                                                   |
| maxVideoSize        | 视频大小，默认45M                                                                       |
| imagePickerType     | 弹出类型，默认图片选择器                                                                     |
| isCrop              | 是否开启剪裁，默认false                                                                   |
| cropAspectRatio     | 剪裁比率（w/h），默认0                                                                    |
| pickerThemeColorRes | 选择器主题色                                                                           |
| cropThemeColorRes   | 剪裁器主题色                                                                           |
| pickerTitleColorRes | 选择器标题色                                                                           |
| cropTitleColorRes   | 剪裁器标题色                                                                           |
| pickerBackRes       | 选择器返回按钮                                                                          |
| pickerFolderRes     | 选择器文件夹选择下拉图标                                                                     |

## 使用

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePickerConfig imagePickerConfig = new ImagePickerConfig.Builder()
                .provider(getPackageName() + ".FileProvider")
                .imageLoader(new GlideImageLoader()) //图片加载器
                .iHandlerCallBack(new HandlerCallBack())    //图片选择器生命周期监听（直接打开摄像头时无效）
                .multiSelect(true)                 //是否多选
                .isShowCamera(true)
                .isVideoPicker(true)              //是否选择视频 默认false
                .isImagePicker(true)
                .imagePickerType(ImagePickerEnum.PHOTO_PICKER) //选择器打开类型
                .isMirror(false)                              //是否旋转镜头
                .maxImageSelectable(9)                        //图片可选择数
                .maxHeight(1920)                              //图片最大高度
                .maxWidth(1920)                               //图片最大宽度
                .maxImageSize(15)                             //图片最大大小Mb
                .maxVideoLength(5 * 1000)
                .maxVideoSize(180)
                .isCrop(true)
                .pathList(new ArrayList<>())
                .pickerThemeColorRes(R.color.image_picker_white)
                .pickerTitleColorRes(R.color.image_picker_text_black)
                .cropThemeColorRes(R.color.image_picker_white)
                .cropTitleColorRes(R.color.image_picker_text_black)
                .pickerBackRes(R.mipmap.image_picker_back_black)
                .pickerFolderRes(R.mipmap.image_picker_folder_black)
                .build();

        findViewById(R.id.iv).setOnClickListener(v -> {
//            imagePickerConfig.getPathList().clear();
            ImagePickerOpen.getInstance()
                    .setImagePickerConfig(imagePickerConfig)
                    .pathList(new ArrayList<>())
                    .open(this, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            List<String> paths = ImagePickerOpen.getResultData(this, resultCode, data);
            ImagePickerOpen.getInstance().getImagePickerConfig().getImageLoader().loadImage(this, findViewById(R.id.iv), paths.get(0));
        }
    }
}
```

```java
public class HandlerCallBack implements IHandlerCallBack<ImageInfo> {
    private String TAG = "---ImagePicker---";
    List<ImageInfo> photoList = new ArrayList<>();

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: 开启");
    }

    @Override
    public void onSuccess(List<ImageInfo> photoList) {
        this.photoList = photoList;
        Log.i(TAG, "onSuccess: 返回数据");
    }

    @Override
    public void onCancel() {
        Log.i(TAG, "onCancel: 取消");
    }

    @Override
    public void onFinish(List<ImageInfo> selectImage) {

    }

    @Override
    public void onError() {
        Log.i(TAG, "onError: 出错");
    }
}
```

## 混淆

-keep public class com.zhangteng.**.*{ *; }

## 历史版本

| 版本             | 更新                                                                         |     | 更新时间                       |
|----------------|----------------------------------------------------------------------------|:----|----------------------------|
| v1.5.0         | 权限请求被拒绝再次请求权限跳转设置页面                                                        |     | 2023/5/3 at 20:27          |
| v1.4.2         | Android11的存储写入权限拒绝结果过滤                                                     |     | 2023/2/14 at 0:27          |
| v1.4.1         | 使用210工具库                                                                   |     | 2023/1/13 at 23:48         |
| v1.4.0         | 使用独立的Utils库                                                                |     | 2022/9/2 at 20:39          |
| v1.3.1         | 继承Utils库中的接口                                                               |     | 2022/7/5 at 9:21           |
| v1.3.0         | 使用base的utils库                                                              |     | 2022/1/21 at 15:42         |
| v1.2.0         | 常量名&值更换                                                                    |     | 2022/1/5 at 23:42          |
| v1.1.9         | 修改依赖库&选择图片回调                                                               |     | 2022/01/02 at 21:55        |
| v1.1.8         | 开放openNoPermission方法，更新权限请求库版本                                             |     | 2021/11/18 at 15:44        |
| v1.1.7         | 图片视频存储地址修改为公共媒体地址（targetSdkVersion 29+时在Android10+有文件访问限制）                 |     | 2021/7/1 at 11:24          |
| v1.1.6         | 1.放弃非AndroidX维护；2.增加默认选中图片集合；3.使用jitpack仓库                                 |     | 2021/6/7 at 13:35          |
| v1.0.10/v1.1.5 | 1.照片存储空间修改到Android/data/${applicationId}/files/imagePicker文件夹下;2.更新权限请求库版本 |     | 2021/2/5 at 15:49          |
| v1.0.9/v1.1.4  | 处理部分机型未释放相机bug&忽略目标版本为29时的分区存储                                             |     | 2020/8/24 at 13:58         |
| v1.0.8/v1.1.3  | 增加文件夹选择下拉图标                                                                |     | 2020/8/21 at 10:50         |
| v1.0.7/v1.1.2  | 根据使用功能动态请求权限                                                               |     | 2020/8/18 0018 at 上午 10:50 |
| v1.0.6/v1.1.1  | 修复请求权限失败时崩溃bug                                                             |     | 2020/8/17 0017 at 下午 17:31 |
| v1.1.0         | 迁移到androidx                                                                |     | 2020/7/22 0022 at 上午 11:54 |
| v1.0.5         | 多媒体查找逻辑调整                                                                  |     | 2020/6/17 0017 at 下午 17:37 |
| v1.0.4         | 视频做最大限制（显示的最大视频长度和录制的最大长度一致）                                               |     | 2020/6/2 0002 at 上午 11:00  |
| v1.0.3         | imageInfo实现Parcelable接口                                                    |     | 2020/5/27 0027 at 下午 14:39 |
| v1.0.2         | 更全的样式自定义                                                                   |     | 2020/5/20 0020 at 下午 17:13 |
| v1.0.0         | 初版                                                                         |     | 2020/5/15 0015 at 下午 16:14 |

## 赞赏

如果您喜欢ImagePicker，或感觉ImagePicker帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢

## 联系我

邮箱：763263311@qq.com/ztxiaoran@foxmail.com

## License

Copyright (c) [2020] [Swing]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
