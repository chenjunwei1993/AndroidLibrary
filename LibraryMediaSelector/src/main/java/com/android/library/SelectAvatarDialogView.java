package com.android.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.mediaselector.R;

/**
 *  * 样式1 - 底部列表
 *  * 样式2 - 中间图标
 * @Descripttion: 资源媒体选择 封装工具类
 * @Author: 陈俊伟
 * @Date: 2020/11/17
 */
public class SelectAvatarDialogView extends LinearLayout {
    private int mStyle; // 1-底部列表选择 2-中间带图标选择
    private View rootView; // 总的布局
    private LinearLayout select_gallery_layout;
    private LinearLayout select_camera_layout;
    private TextView gallery_content;
    private TextView camera_content;
    private TextView cancle;
    private TextView dialog_title;
    private OnItemSelectListener avatarSelectListener;
    private String title;
    private String galaryContent, cameraContent;


    public SelectAvatarDialogView(Context context, int style) {
        super(context);
        mStyle = style;
        initView(context);
    }

    private void initView(Context context) {
        if (mStyle == 1) {
            rootView = LayoutInflater.from(context).inflate(
                    R.layout.select_avatar_style1_layout, null);
            title = context.getString(R.string.dialog_style1_title);
            galaryContent = context.getString(R.string.dialog_style1_gallary);
            cameraContent = context.getString(R.string.dialog_style1_camera);
        } else if (mStyle == 2) {
            rootView = LayoutInflater.from(context).inflate(
                    R.layout.select_avatar_style2_layout, null);
            title = context.getString(R.string.dialog_style2_title);
            galaryContent = context.getString(R.string.dialog_style2_gallary);
            cameraContent = context.getString(R.string.dialog_style2_camera);
        } else {
            return;
        }

        select_gallery_layout = (LinearLayout) rootView.findViewById(R.id.select_gallery_layout);
        select_camera_layout = (LinearLayout) rootView.findViewById(R.id.select_camera_layout);
        gallery_content = (TextView) rootView.findViewById(R.id.gallery_content);
        camera_content = (TextView) rootView.findViewById(R.id.camera_content);
        cancle = (TextView) rootView.findViewById(R.id.dialog_cancel_btn);
        dialog_title = (TextView) rootView.findViewById(R.id.dialog_title);

        setGalaryContent(galaryContent);
        setCameraContent(cameraContent);
        setTitle(title);
        setListens();
        addView(rootView);
    }

    public void setTitle(String title) {
        this.title = title;
        dialog_title.setText(title);
    }

    public void setCameraContent(String cameraContent) {
        this.cameraContent = cameraContent;
        camera_content.setText(cameraContent);
    }

    public void setGalaryContent(String galaryContent) {
        this.galaryContent = galaryContent;
        gallery_content.setText(galaryContent);
    }

    private void setListens() {
        select_gallery_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (avatarSelectListener != null) {
                    avatarSelectListener.onAvatarSelect(0);
                    avatarSelectListener.goFinish();
                }
            }
        });

        select_camera_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (avatarSelectListener != null) {
                    avatarSelectListener.onAvatarSelect(1);
                    avatarSelectListener.goFinish();
                }
            }
        });

        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (avatarSelectListener != null) {
                    avatarSelectListener.goFinish();
                }
            }
        });
    }

    public void setOnAvatarSelectListener(OnItemSelectListener avatarSelectListener) {
        this.avatarSelectListener = avatarSelectListener;
    }

    interface OnItemSelectListener {
        void onAvatarSelect(int whichButton);

        void goFinish();
    }

}
