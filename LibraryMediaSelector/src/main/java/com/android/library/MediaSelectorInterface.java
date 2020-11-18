package com.android.library;

import android.view.View;

import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

/**
 * 对外接口定义
 * 创建人：wuguojin
 * 时间：17/12/8 10:40
 */

public interface MediaSelectorInterface {


    /**
     * 展示图片选择框 样式1
     */
    void showPickerDialogStyle1(final OnResultCallbackListener onResultCallbackListener);

    /**
     * 展示图片选择框 样式1
     *
     * @param title 自定义title
     */
    void showPickerDialogStyle1(String title,final OnResultCallbackListener onResultCallbackListener);

    /**
     * 展示图片选择框 样式1
     *
     * @param title
     * @param withCamera 选择图库中包含camera按钮
     */
    void showPickerDialogStyle1(String title, boolean withCamera,final OnResultCallbackListener onResultCallbackListener);

    /**
     * 展示图片选择框 样式2
     */
    void showPickerDialogStyle2(final OnResultCallbackListener onResultCallbackListener);

    /**
     * 展示图片选择框 样式2
     *
     * @param title 自定义title
     */
    void showPickerDialogStyle2(String title,final OnResultCallbackListener onResultCallbackListener);

    /**
     * 展示图片选择框 样式2
     *
     * @param title
     * @param withCamera 选择图库中包含camera按钮
     */
    void showPickerDialogStyle2(String title, boolean withCamera,final OnResultCallbackListener onResultCallbackListener);


    /**
     * 展示自定义布局的图片选择框
     *
     * @param customView      自定义布局
     * @param pickPhotoViewId 图库跳转按钮ID
     * @param takePhotoViewId 拍照跳转按钮ID
     * @param cancelViewId    取消按钮ID
     * @param dialogWidth     对话框宽度
     * @param dialogGravity   对话框相对位置 Gravity.TOP | Gravity.BOTTOM | Gravity.CENTER
     * @param dialogAnimStyle 对话框进出动画style 如果没有动画传-1
     * @param withCamera      选择图库中包含camera按钮
     */
    void showPickDialogCustomView(View customView, int pickPhotoViewId, int takePhotoViewId, int cancelViewId, int dialogWidth,
                                  int dialogGravity, int dialogAnimStyle, boolean withCamera,final OnResultCallbackListener onResultCallbackListener);


    /**
     * 选取一张图片
     */
    void pickOnePhoto(OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择图片 - 包含拍照按钮
     *
     * @param withCamera 包含相机
     */
    void pickOnePhoto(boolean withCamera,OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择多张图片
     *
     * @param maxNum       最大张数
     * @param selectedList 已选择的图片列表
     */
    void pickMultiplePhoto(int maxNum, List<LocalMedia> selectedList,OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择多张图片 - 包含拍照按钮
     *
     * @param maxNum       最大张数
     * @param selectedList 已选择的图片列表
     * @param withCamera   包含相机
     */
    void pickMultiplePhoto(int maxNum, List<LocalMedia> selectedList, boolean withCamera,OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择一个视频
     */
    void pickOneVideo(OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择多个视频
     *
     * @param maxNum       最大数
     * @param selectedList 已选择的资源列表
     */
    void pickMultipleVideo(int maxNum, List<LocalMedia> selectedList,OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择一个音频文件
     */
    void pickOneAudio(OnResultCallbackListener<LocalMedia> onResultCallbackListener);

    /**
     * 选择多个音频
     *
     * @param maxNum       最大量
     * @param selectedList 已选择的资源列表
     */
    void pickMultipleAudio(int maxNum, List<LocalMedia> selectedList,OnResultCallbackListener<LocalMedia> onResultCallbackListener);
}
