package com.android.library.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.text.TextUtils;


import com.android.library.util.file.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author chenjunwei
 * @desc 图片压缩处理工具类
 * @date 2019-09-24
 */
public class BitmapCompressUtil {


    /**
     * 重要：压缩图片方法  输入路径，输出路径，输出质量
     * 重要：压缩图片方法  输入路径，输出路径，输出质量
     * 重要：压缩图片方法  输入路径，输出路径，输出质量
     *
     * @param filePath
     * @param targetPath
     * @param quality
     * @return
     */
    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        try {
            File outputFile = new File(targetPath);
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            return outputFile.getPath();
        } catch (Exception e) {
            bm.recycle();
            return filePath;
        }
    }

    /**
     * 根据bitmap直接生成目标路径---如果存在则将直接输出文件路径
     *
     * @param bitmap
     * @param targetPath
     * @param quality
     * @return
     */
    public static String getTargetImagePath(Bitmap bitmap, String targetPath, int quality) {
        try {
            File outputFile = new File(targetPath);
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            } else {
                return targetPath;
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
            return outputFile.getPath();
        } catch (Exception e) {
            bitmap.recycle();
            return targetPath;
        }

    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath, int w, int h) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, w, h);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            try {
                m.postRotate(degress);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
            } catch (OutOfMemoryError e) {
            }
            return bitmap;
        }
        return bitmap;
    }


    /**
     * 缩放图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 图片反转
     *
     * @param bmp
     * @param flag 0为水平反转，1为垂直反转
     * @return
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }
        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        return null;
    }

    /**
     * 对图片做压缩处理
     *
     * @author mark
     */
    public static Bitmap decodeSampledBitmapFromByte(byte[] bitmapBytes) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0,
                bitmapBytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeByteArray(bitmapBytes, 0,
                bitmapBytes.length, options);
    }

    /**
     * 保存图片到SD卡
     *
     * @param imgPath             为null，success之后会返回保存地址
     * @param bitmap
     * @param mISaveImageListener
     */
    public static void saveBitmapToSD(String imgPath, Bitmap bitmap,
                                      ISaveImageListener mISaveImageListener) {
        new SavePicTask(imgPath, mISaveImageListener).execute(bitmap);
    }

    /**
     * 保存图片工具类
     *
     * @author wangya
     */
    private static class SavePicTask extends AsyncTask<Bitmap, String, String> {
        private String imgPath;
        private ISaveImageListener mISaveImageListener;

        public SavePicTask(String imgPath,
                           ISaveImageListener mISaveImageListener) {
            super();
            this.mISaveImageListener = mISaveImageListener;
            if (TextUtils.isEmpty(imgPath)) {
                imgPath = new FileUtils().getSDPATH() + new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                        .format(new Date()) + ".jpg";
            }
            this.imgPath = imgPath;
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            // 如果为null直接执行失败事件
            if (bitmap == null) {
                return "failed";
            }
            File file = new File(imgPath);
            try {
                FileOutputStream fout = new FileOutputStream(file.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                fout.flush();
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return imgPath;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mISaveImageListener != null) {
                if (TextUtils.equals("failed", result)) {
                    mISaveImageListener.failed();
                } else {
                    mISaveImageListener.success(result);
                }
            }

        }
    }

    /**
     * 保存图片接口
     */
    public interface ISaveImageListener {
        void success(String path);

        void failed();
    }


}
