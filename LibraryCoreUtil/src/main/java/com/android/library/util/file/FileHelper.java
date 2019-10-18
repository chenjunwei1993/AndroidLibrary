package com.android.library.util.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.library.util.CoreUtils;
import com.android.library.util.LogUtil;
import com.android.library.util.bitmap.ImageUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author 张雷
 */
public class FileHelper {

    /**
     * 由指定的路径和文件名创建文件
     *
     * @param path
     * @param name
     * @return
     * @throws IOException
     */
    public static File createFile(String path, String name) throws IOException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @param name
     * @return
     */
    public static boolean fileExist(String path, String name) {
        File file = new File(path + name);
        if (file.exists() && !file.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * @param name
     * @param path
     * @return boolean
     * @Title: copyDrawableFiles
     * @Description: 拷贝drawable下的图片到指定位置
     */
    public static boolean copyDrawableFiles(Context c, String name, String path) {
        try {
            int id = android.R.drawable.class.getField(name).getInt(android.R.drawable.class);
            Bitmap bitmap = ImageUtils.getBitMapFromResource(c, id);
            savBitmap(bitmap, name + ".png", path);
        } catch (IllegalArgumentException e) {
            LogUtil.e(e.getMessage());
            return false;
        } catch (IllegalAccessException e) {
            LogUtil.e(e.getMessage());
            return false;
        } catch (NoSuchFieldException e) {
            LogUtil.e(e.getMessage());
            return false;
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * @param bitmap
     * @param name
     * @param path
     * @throws IOException
     * @Title: savBitmap
     * @Description: 保存bitmap成图片
     */
    public static File savBitmap(Bitmap bitmap, String name, String path)
            throws IOException {
        if (bitmap == null) {
            return null;
        }
        File file = createFile(path, name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            LogUtil.e(e.getMessage());
        } finally {
            CloseUtils.closeIO(fos);
        }
        return file;
    }

    /**
     * @param bitmap
     * @param name   完整的文件名（带有路径）
     * @throws IOException
     * @Title: savBitmap
     * @Description: 保存bitmap成图片
     */
    public static void savBitmap(Bitmap bitmap, String name) throws IOException {
        if (bitmap == null) {
            return;
        }
        File f = new File(name);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            LogUtil.e(e.getMessage());
        } finally {
            CloseUtils.closeIO(fos);
        }
    }

    /**
     * 判断Assets是否存在这个文件
     *
     * @param pt
     * @return
     */
    public static boolean isAssetsExists(String pt) {
        AssetManager am = CoreUtils.getContext().getAssets();
        try {
            String[] names = am.list("");
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(pt.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return void
     * @Title: readInitFile
     * @Description:读取初始化文件（内置数据）
     */
    public static String readAssetFile(Context c, String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputReader = new InputStreamReader(c.getResources().getAssets()
                    .open(name));
            bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.startsWith("//")) {
                    continue;
                }
                sb.append(line);
            }
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        } finally {
            CloseUtils.closeIO(bufReader);
            CloseUtils.closeIO(inputReader);
        }
        return sb.toString();
    }

    /**
     * @param @param rawid raw目录下对应的文件名称
     * @return String
     * @Title: getTestData
     * @Description: 该方法用于读取测试数据，例如一串sql,json,xml等
     */
    public static String getTestData(Context c, int rawid) {
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = c.getResources().openRawResource(rawid);
            reader = new InputStreamReader(is);
            br = new BufferedReader(reader);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(br);
            CloseUtils.closeIO(reader);
            CloseUtils.closeIO(is);
        }
        return sb.toString();
    }

    /**
     * 删除文件或者文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * @param @param srcPath
     * @param @param srcName
     * @param @param desPath
     * @param @param desName
     * @return boolean
     * @Title: copyFile
     * @Description: 拷贝文件
     */
    public static boolean copyFile(String srcPath, String srcName,
                                   String desPath, String desName) {
        if (!fileExist(srcPath, srcName)) {
            return false;
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File inFile = new File(srcPath, srcName);
            File outFile = new File(desPath, desName);

            if (!fileExist(desPath, desName)) {
                createFile(desPath, desName);
            }

            fis = new FileInputStream(inFile);
            bis = new BufferedInputStream(fis);

            fos = new FileOutputStream(outFile);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024 * 8];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(bis);
            CloseUtils.closeIO(fis);
            CloseUtils.closeIO(bos);
            CloseUtils.closeIO(fos);
        }
        return false;
    }

    /**
     * 拷贝这个文件夹
     *
     * @param oldPath
     * @param newPath
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) { // 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从SD卡读取内容
     *
     * @param path 路径
     * @return
     */
    public static String readFromSDCard(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        InputStream in = null;
        String htmlUrl = null;
        try {
            in = new FileInputStream(file);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            htmlUrl = new String(buffer, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlUrl;
    }

    /**
     * 从SD卡的图片Bitmap
     *
     * @param path 路径
     * @return
     */
    public static Bitmap readBitmapFromSDCard(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return ImageUtils.getBitMapFromFile(path);
    }

    /**
     * 获得文件大小
     *
     * @param file
     * @return
     */
    public static String getFileSize(File file) {
        FileSizeHelper getSize = new FileSizeHelper();
        String size = null;
        try {
            long l = 0;
            if (file.isDirectory()) {
                l = getSize.getFileSize(file);
            } else {
                l = getSize.getFileSizes(file);
            }
            size = getSize.FormetFileSize(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 将该路径文件转化为二进制
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 将二进制转化为文件并存储
     *
     * @param filePath
     * @return
     */
    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
