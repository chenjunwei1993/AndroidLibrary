package com.android.library;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.library.mediaselector.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.tools.SdkVersionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Descripttion: 资源媒体选择 封装工具类
 * @Author: 陈俊伟
 * @Date: 2020/11/17
 */
public class MediaSelectorUtil implements MediaSelectorInterface {
    private Activity mActivity;
    private Fragment mFragment;
    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;

    public MediaSelectorUtil(Activity activity) {
        this.mActivity = activity;
        getWeChatStyle(mActivity);
    }

    public MediaSelectorUtil(Fragment fragment) {
        this.mFragment = fragment;
        if (null == mActivity) {
            mActivity = fragment.getActivity();
        }
        getWeChatStyle(mActivity);
    }

    private void getWeChatStyle(Context context) {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true;
        // 状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(context, R.color.app_color_black);
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_wechat_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_wechat_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_close;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.picture_color_53575e);
        // 相册右侧按钮字体默认颜色
        mPictureParameterStyle.pictureRightDefaultTextColor = ContextCompat.getColor(context, R.color.picture_color_53575e);
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureRightSelectedTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg;
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg;
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_wechat_num_selector;
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg;
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb;
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_53575e);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_half_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(context, R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");

        // 完成文案是否采用(%1$d/%2$d)的字符串，只允许两个占位符哟
//        mPictureParameterStyle.isCompleteReplaceNum = true;
        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureUnCompleteText = getString(R.string.app_wechat_send);
        //自定义相册右侧已选中时文案 支持占位符String 但只支持两个 必须isCompleteReplaceNum为true
//        mPictureParameterStyle.pictureCompleteText = getString(R.string.app_wechat_send_num);
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//        // 自定义预览页右下角选择文字文案
//        mPictureParameterStyle.pictureWeChatPreviewSelectedText = "";

//        // 自定义相册标题文字大小
//        mPictureParameterStyle.pictureTitleTextSize = 9;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 9;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 9;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 9;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 9;
//        // 自定义预览页右下角选择文字大小
//        mPictureParameterStyle.pictureWeChatPreviewSelectedTextSize = 9;

        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(context, R.color.app_color_grey),
                ContextCompat.getColor(context, R.color.app_color_grey),
                Color.parseColor("#393a3e"),
                ContextCompat.getColor(context, R.color.app_color_white),
                mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    @Override
    public void showPickerDialogStyle1(final OnResultCallbackListener onResultCallbackListener) {
        showPickerDialogStyle1("", onResultCallbackListener);
    }

    @Override
    public void showPickerDialogStyle1(String title, final OnResultCallbackListener onResultCallbackListener) {
        showPickerDialogStyle1(title, true, onResultCallbackListener);
    }

    @Override
    public void showPickerDialogStyle1(String title, final boolean withCamera, final OnResultCallbackListener onResultCallbackListener) {
        SelectAvatarDialogView view = new SelectAvatarDialogView(mActivity, 1);
        if (!"".equals(title)) {
            view.setTitle(title);
        }
        final Dialog dialog = new Dialog(mActivity, R.style.MediaSelectorDialog_BottomAnim);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.9);
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        p.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(p);

        view.setOnAvatarSelectListener(new SelectAvatarDialogView.OnItemSelectListener() {
            @Override
            public void onAvatarSelect(int whichButton) {
                if (whichButton == 0) {
                    // 图库
                    pickOnePhoto(withCamera, onResultCallbackListener);
                } else if (whichButton == 1) {
                    // 拍照
                    takePhotoWithSystemCamera(mActivity);
                }
            }

            @Override
            public void goFinish() {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void showPickerDialogStyle2(final OnResultCallbackListener onResultCallbackListener) {
        showPickerDialogStyle2("", onResultCallbackListener);
    }

    @Override
    public void showPickerDialogStyle2(String title, final OnResultCallbackListener onResultCallbackListener) {
        showPickerDialogStyle2(title, true, onResultCallbackListener);
    }

    @Override
    public void showPickerDialogStyle2(String title, final boolean withCamera, final OnResultCallbackListener onResultCallbackListener) {
        SelectAvatarDialogView view = new SelectAvatarDialogView(mActivity, 2);
        if (!"".equals(title)) {
            view.setTitle(title);
        }
        final Dialog dialog = new Dialog(mActivity, R.style.MediaSelectorDialog);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.8);
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(p);

        view.setOnAvatarSelectListener(new SelectAvatarDialogView.OnItemSelectListener() {
            @Override
            public void onAvatarSelect(int whichButton) {
                if (whichButton == 0) {
                    // 图库
                    pickOnePhoto(withCamera, onResultCallbackListener);
                } else if (whichButton == 1) {
                    // 拍照
                    takePhotoWithSystemCamera(mActivity);
                }
            }

            @Override
            public void goFinish() {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showPickDialogCustomView(View customView, int pickPhotoViewId, int takePhotoViewId, int cancelViewId, int dialogWidth,
                                         int dialogGravity, int dialogAnimStyle, final boolean withCamera, final OnResultCallbackListener onResultCallbackListener) {
        if (customView == null) {
            // 如果传入的view为空，默认展示样式一
            showPickerDialogStyle1(onResultCallbackListener);
            return;
        }
        if (dialogGravity != Gravity.BOTTOM && dialogGravity != Gravity.CENTER
                && dialogGravity != Gravity.TOP && dialogGravity != Gravity.CENTER_HORIZONTAL
                && dialogGravity != Gravity.CENTER_VERTICAL && dialogGravity != Gravity.NO_GRAVITY) {
            dialogGravity = Gravity.BOTTOM;
        }

        final Dialog dialog = new Dialog(mActivity, R.style.MediaSelectorDialog);
        dialog.setContentView(customView);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = dialogWidth;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        p.gravity = dialogGravity;
        dialogWindow.setAttributes(p);
        if (dialogAnimStyle != -1) {
            dialogWindow.setWindowAnimations(dialogAnimStyle);
        }

        if (pickPhotoViewId != -1) {
            customView.findViewById(pickPhotoViewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 图库
                    dialog.dismiss();
                    pickOnePhoto(withCamera, onResultCallbackListener);
                }
            });
        }
        if (takePhotoViewId != -1) {
            customView.findViewById(takePhotoViewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 拍照
                    dialog.dismiss();
                    takePhotoWithSystemCamera(mActivity);
                }
            });
        }
        if (cancelViewId != -1) {
            customView.findViewById(cancelViewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 取消
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }

    @Override
    public void pickOnePhoto(OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofImage(), 1, 1, new ArrayList<LocalMedia>(), true, onResultCallbackListener);
    }

    @Override
    public void pickOnePhoto(boolean withCamera, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofImage(), 1, 1, new ArrayList<LocalMedia>(), withCamera, onResultCallbackListener);
    }

    @Override
    public void pickMultiplePhoto(int maxSelectNum, List<LocalMedia> selectedList, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofImage(), maxSelectNum, 1, selectedList, true, onResultCallbackListener);
    }

    @Override
    public void pickMultiplePhoto(int maxSelectNum, List<LocalMedia> selectedList, boolean withCamera, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofImage(), maxSelectNum, 1, selectedList, withCamera, onResultCallbackListener);
    }

    @Override
    public void pickOneVideo(OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofVideo(), 1, 1, new ArrayList<LocalMedia>(), true, onResultCallbackListener);
    }

    @Override
    public void pickMultipleVideo(int maxSelectNum, List<LocalMedia> selectedList, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofVideo(), 1, maxSelectNum, selectedList, true, onResultCallbackListener);
    }

    @Override
    public void pickOneAudio(OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofAudio(), 1, 1, new ArrayList<LocalMedia>(), true, onResultCallbackListener);
    }

    @Override
    public void pickMultipleAudio(int maxSelectNum, List<LocalMedia> selectedList, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        pickAll(PictureMimeType.ofAudio(), 1, maxSelectNum, selectedList, true, onResultCallbackListener);
    }

    // 拍照临时文件名
    private String takePhotoTmpFileName = "";

    public void takePhotoWithSystemCamera(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)) {
            requestForPermissions();
        } else {
            takePhotoTmpFileName = getPath(context) + System.currentTimeMillis() + ".jpg";
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(takePhotoTmpFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            if (mFragment != null) {
                mFragment.startActivityForResult(takeIntent, PictureConfig.REQUEST_CAMERA);
            } else {
                mActivity.startActivityForResult(takeIntent, PictureConfig.REQUEST_CAMERA);
            }
        }
    }

    /**
     * 获取存储路径
     *
     * @param context
     * @return
     */
    public static String getPath(Context context) {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/cache/Cache/";
        } else {
            path = context.getCacheDir() + "/";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private static final int PERMISSION_FOR_CAMERA_STORAGE = 113;

    @TargetApi(Build.VERSION_CODES.M)
    public void requestForPermissions() {
        mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, PERMISSION_FOR_CAMERA_STORAGE);
    }

    private void pickAll(int mimeType, int maxSelectNum, int maxVideoSelectNum, List<LocalMedia> selectedList,
                         boolean isShowCamera, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
        if (selectedList == null) {
            selectedList = new ArrayList<>();
        }
        PictureSelector pictureSelector;
        if (null != mFragment) {
            pictureSelector = PictureSelector.create(mFragment);
        } else {
            pictureSelector = PictureSelector.create(mActivity);
        }

        // 进入相册 以下是例子：不需要的api可以不写
        pictureSelector
                .openGallery(mimeType)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                //.theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                //.isUseCustomCamera(cb_custom_camera.isChecked())// 是否使用自定义相机
                //.setLanguage(language)// 设置语言，默认中文
                //.isPageStrategy(cbPage.isChecked())// 是否开启分页策略 & 每页多少条；默认开启
                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //.setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                //.setRecyclerAnimationMode(animationMode)// 列表动画效果
                .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath(createCustomCameraOutPath())// 自定义相机输出目录
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .maxVideoSelectNum(maxVideoSelectNum) // 视频最大选择数量
                //.minVideoSelectNum(1)// 视频最小选择数量
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                //.isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(context))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                //.isEnablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                .isCamera(isShowCamera)// 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                //.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                .isEnableCrop(false)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                .isCompress(false)// 是否压缩
                //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                //.isWebp(false)// 是否显示webp图片,默认显示
                //.isBmp(false)//是否显示bmp图片,默认显示
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(context, R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isOpenClickSound(false)// 是否开启点击声音
                .selectionData(selectedList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.videoMinSecond(10)// 查询多少秒以内的视频
                //.videoMaxSecond(15)// 查询多少秒以内的视频
                //.recordVideoSecond(10)//录制视频秒数 默认60s
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                .forResult(onResultCallbackListener);
    }
}
