## 媒体资源选择工具类

详细文档地址： [http://documents.hoge.cn/docs/show/514](http://documents.hoge.cn/docs/show/514 "媒体资源选择工具类")

## 第三方依赖

第三方库版本：${phoenix_ui} = 1.0.0
${ucrop} = 2.2.1
```
// 本地库集成第三方SDK-phoenix，并定制其UI模块
api "com.hoge.android.mxu:phoenix-ui:${phoenix_ui}"
// UCrop剪裁
api "com.github.yalantis:ucrop:${ucrop}"
```

## 使用方法

该库引入第三方Phoenix一站式图片视频音频选择框架，并对其进行了二次封装，对外提供接口
所有对外方法定义在MediaSelectorInterface类中，使用时请查阅。

### 快速开始

> STEP - 1 在Application中初始化ImageLoader

```
MediaSelectorUtil.initWithImageLoader(new ImageLoader() {
            @Override
            public void loadImage(Context context, ImageView imageView, String imagePath, int type) {
                Glide.with(context).load(imagePath).into(imageView);
            }
        });
```

> STEP - 2 构造图片选择器实例

```
private MediaSelectorUtil mediaSelectorUtil;
//默认UI样式
mediaSelectorUtil = new MediaSelectorUtil.Builder().with(this).build(); 

//可自定义UI，如
mediaSelectorUtil = new MediaSelectorUtil.Builder().with(this)
      .appThemeColor(Color.LTGRAY)
      .appTitleColor(Color.parseColor("#DC4726"))
      .leftBackColor(Color.parseColor("#DC4726"))
      .rightMenuColor(Color.parseColor("#DC4726"))
      .previewBtnTextColor(Color.parseColor("#DC4726"))
      .pickerCommitBtnBgColor(Color.parseColor("#DC4726"))
      .pickerCommitBtnTextColor(Color.WHITE)
      .spanCount(4)
      .build();
```

> STEP - 3 弹出图片选择框

```
mediaSelectorUtil.showPickerDialogStyle1();
```

> STEP - 4 处理权限回调

```
@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mediaSelectorUtil.handlePermissionResult(requestCode, permissions, grantResults, new MediaSelectorInterface.OnRequestPermissionListener() {
            @Override
            public void onAllPermissionFetched() {
                mediaSelectorUtil.showPickerDialogStyle1();
            }
        });
    }
```

> STEP - 5 处理图库或拍照结果

```
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == MediaSelectorUtil.PICK_IMG_REQCODE) {
            // 图片选择返回的数据
            mediaSelectorUtil.handlePickResultToCrop(data, new int[]{1, 1}, new int[]{300, 300});
        } else if (requestCode == MediaSelectorUtil.TAKE_PHOTO_REQCODE) {
            // 拍照返回
            mediaSelectorUtil.handleTakeResultToCrop(new int[]{16, 9}, new int[]{800, 500});
        } else if (requestCode == UCrop.REQUEST_CROP) {
            // 剪裁返回
            Uri resultUri = UCropUtil.getUCropResult(data);
            Glide.with(mContext).load(resultUri).into(mPreViewImg);
            mediaSelectorUtil.cleanTakePhotoTmpFile();
        } else if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(mContext, "crop error", Toast.LENGTH_SHORT).show();
            mediaSelectorUtil.cleanTakePhotoTmpFile();
        }
    }
```

## 使用场景

|工程名称|模块|工程地址|负责人|
|:----    |:---|:----- |-----   |
| MXU| 全局 |  |陈俊伟



## 版本修改记录

| 当前版本  | 升级日期  |升级日志|操作人
|:-----:|:-----:|:-----:|:-----: |
|1.0.0   |2017.12.20  | 创建资源选择器封装类库|陈俊伟
\