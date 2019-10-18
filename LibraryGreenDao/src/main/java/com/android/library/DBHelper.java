package com.android.library;

import com.android.library.bean.DBDetailBean;
import com.android.library.bean.DBListBean;

/**
 * @author chenjunwei
 * @desc 数据库上层封装，要求 在工厂工程里不直接使用DBList,DBDetail等
 * @date 2019-09-25
 */
public class DBHelper {
    /**
     * 清缓存时提供反射类包名
     *
     * @param className
     * @return
     */
    public static String getLibraryDBName(String className) {
        return "com.android.library.bean." + className;
    }

    /**
     * 存储一级列表页
     *
     * @param url
     * @param data
     */
    public static void saveList(String url, String data) {
        DBListBean bean = findList(url);
        if (bean == null) {
            bean = new DBListBean();
        }
        bean.setData(data);
        bean.setSave_time(System.currentTimeMillis() + "");
        bean.setUrl(url);
        GreenDaoHelper.save(bean);
    }

    /**
     * 查找一级列表页数据
     *
     * @param url
     * @param <T>
     * @return
     */
    public static <T> T findList(String url) {
        return GreenDaoHelper.find(DBListBean.class, "where url = ?", url);
    }

    /**
     * 存储详情页数据
     *
     * @param url
     * @return
     */
    public static void saveDetail(String url, String data) {
        DBDetailBean bean = findDetail(url);
        if (bean == null) {
            bean = new DBDetailBean();
        }
        bean.setData(data);
        bean.setSave_time(System.currentTimeMillis() + "");
        bean.setUrl(url);
        GreenDaoHelper.save(bean);
    }

    /**
     * 查找详情页数据
     *
     * @param url
     * @param <T>
     * @return
     */
    public static <T> T findDetail(String url) {
        return GreenDaoHelper.find(DBDetailBean.class, "where url = ?", url);
    }

    /**
     * 清空表
     *
     * @param clazz
     */
    public static void deleteAll(Class<?> clazz) {
        GreenDaoHelper.deleteAll(clazz);
    }

}
