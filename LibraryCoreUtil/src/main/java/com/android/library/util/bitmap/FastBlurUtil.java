package com.android.library.util.bitmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.library.util.ThreadPoolUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lixurong on 2017/9/27.
 * 高度模糊 工具类
 */

public class FastBlurUtil {
    private static Bitmap blurBm = null;

    /**
     * 高斯模糊公共方法
     *
     * @param bkg
     * @param view
     */
    public static void blur(Context mContext, String sign, Bitmap bkg,
                            View view, boolean isFine) {
        blur(mContext, sign, bkg, view, isFine, view.getMeasuredWidth(),
                view.getMeasuredHeight());
    }

    public static void blur(final Context mContext, final Bitmap bitmap,
                            final View view, final boolean isFine, final int width,
                            final int height) {
        blur(mContext, null, bitmap, view, isFine, width, height);
    }

    /**
     * 高斯模糊公共方法
     *
     * @param
     * @param view
     * @param isFine
     * @param width
     * @param height
     */
    public static void blur(final Context mContext, final String savePath,
                            final Bitmap bitmap, final View view, final boolean isFine,
                            final int width, final int height) {
        ThreadPoolUtil.executeCachedThread(new Runnable() {
            @Override
            public void run() {
                try {
                    blurBm = null;
                    if (!bitmap.isMutable()) {
                        blurBm = convertToMutable(bitmap);
                    } else {
                        blurBm = bitmap;
                    }
                    float radius = 60;
                    if (!isFine) {
                        radius = 30;
                    }
                    float scaleFactor = 1;
                    if (blurBm.getWidth() < width) {
                        scaleFactor = width / blurBm.getWidth();
                    }
                    blurBm = FastBlur.doBlur(scaleBitMap(blurBm, scaleFactor),
                            (int) radius, true);
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (view instanceof ImageView) {
                                ((ImageView) view)
                                        .setImageDrawable(new BitmapDrawable(
                                                blurBm));
                            } else {
                                view.setBackgroundDrawable(new BitmapDrawable(
                                        blurBm));
                            }
                        }
                    });
                    if (!TextUtils.isEmpty(savePath)) {
                        BitmapCompressUtil.saveBitmapToSD(savePath, blurBm, null);
                    }
                } catch (OutOfMemoryError e) {
                    e.getStackTrace();
                }
            }
        });
    }

    /**
     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't
     * allocates more memory that there is already allocated.
     *
     * @param imgIn - Source image. It will be released, and should not be used
     *              more
     * @return a copy of imgIn, but muttable.
     */
    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            // this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "temp.tmp");
            // Open an RandomAccessFile
            // Make sure you have added uses-permission
            // android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            // into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();
            // Copy the byte to the file
            // Assume source bitmap loaded using options.inPreferredConfig =
            // Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0,
                    imgIn.getRowBytes() * height);
            imgIn.copyPixelsToBuffer(map);
            // recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            // Create a new bitmap to load the bitmap again. Probably the memory
            // will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            // load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            // close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();
            // delete the temp file
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgIn;
    }

    public static Bitmap scaleBitMap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

}
