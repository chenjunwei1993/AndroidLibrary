package com.android.library;


import android.content.Context;
import android.text.TextUtils;

import com.android.library.dao.DaoMaster;
import com.android.library.dao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

import java.util.List;


/**
 * @author chenjunwei
 * @desc greendao 工具类
 * @date 2019-09-25
 */
public class GreenDaoHelper {
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoSession daoSession;
    private static String DBName = DBConstant.GREENDAO_DB_NAME;

    /**
     * 初始化数据库，在Application中调用
     *
     * @param mContext
     * @param dbName    数据库名称
     * @param password  加密密码
     * @param encrypted 是否加密
     */
    public static void initDB(Context mContext, String dbName, String password, boolean encrypted) {
        DBName = TextUtils.isEmpty(dbName) ? DBConstant.GREENDAO_DB_NAME : dbName;
        mDevOpenHelper = new MyDevOpenHelper(mContext, DBName);
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase(mContext, password, encrypted));
        daoSession = daoMaster.newSession();
    }

    /**
     * 获取可读数据库
     *
     * @param mContext
     * @param password
     * @param encrypted
     * @return
     */
    private static Database getReadableDatabase(Context mContext, String password, boolean encrypted) {
        if (null == mDevOpenHelper) {
            mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, DBName);
        }
        if (encrypted) {
            //加密
            return mDevOpenHelper.getEncryptedReadableDb(password);
        } else {
            return mDevOpenHelper.getReadableDb();
        }
    }


    private static Database getWritableDatabase(Context mContext, String password, boolean encrypted) {
        if (null == mDevOpenHelper) {
            mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, DBName);
        }
        if (encrypted) {
            //加密
            return mDevOpenHelper.getEncryptedWritableDb(password);
        } else {
            return mDevOpenHelper.getWritableDb();
        }
    }


    /**
     * 保存数据，如果数据库存在则更新，否则 插入
     *
     * @param entity
     * @param <T>
     */
    public static <T> void save(T entity) {
        AbstractDao abstractDao = daoSession.getDao(entity.getClass());
        abstractDao.save(entity);
    }

    /**
     * 插入数据
     *
     * @param entity
     * @param <T>
     */
    public static <T> void insert(T entity) {
        AbstractDao abstractDao = daoSession.getDao(entity.getClass());
        abstractDao.insertOrReplace(entity);
    }

    /**
     * 更新数据
     *
     * @param entity
     * @param <T>
     */
    public static <T> void update(T entity) {
        AbstractDao abstractDao = daoSession.getDao(entity.getClass());
        abstractDao.update(entity);
    }

    /**
     * 删除表中全部数据
     *
     * @param clazz
     */
    public static void deleteAll(Class<?> clazz) {
        AbstractDao abstractDao = daoSession.getDao(clazz);
        abstractDao.deleteAll();
    }

    /**
     * 删除表中指定数据
     *
     * @param clazz
     * @param entity
     * @param <T>
     */
    public static <T> void delete(Class<?> clazz, T entity) {
        AbstractDao abstractDao = daoSession.getDao(clazz);
        abstractDao.delete(entity);
    }

    /**
     * 删除数据
     *
     * @param clazz 类
     * @param where 条件
     * @param value 参数
     * @param <T>   泛型
     */
    public static <T> void delete(Class<?> clazz, String where, String... value) {
        AbstractDao abstractDao = daoSession.getDao(clazz);
        List<T> list = (List<T>) abstractDao.queryRaw(where, value);
        abstractDao.delete(list);
    }

    /**
     * 查找所有数据
     *
     * @param entity
     */
    public static<T> List<T> findAll(Class<?> entity) {
        List<T> list = (List<T>)daoSession.getDao(entity).loadAll();
        return list;
    }

    /**
     * 表中个数
     *
     * @param entity
     * @param <T>泛型
     */
    public static <T> void count(T entity) {
        daoSession.getDao(entity.getClass()).count();
    }

    /**
     * 查找所有符合条件的
     *
     * @param clazz 类
     * @param where 条件
     * @param value 参数
     * @param <T>   泛型
     * @return
     */
    public static <T> List<T> findAll(Class<?> clazz, String where, String... value) {
        List<T> list = (List<T>) daoSession.getDao(clazz).queryRaw(where, value);
        return list;
    }

    /**
     * 查找第一条数据
     *
     * @param clazz 类
     * @param where 条件
     * @param value 参数
     * @param <T>   泛型
     * @return
     */
    public static <T> T find(Class<?> clazz, String where, String... value) {
        T t = null;
        List<T> list = (List<T>) daoSession.getDao(clazz).queryRaw(where, value);
        if (list != null && list.size() > 0) {
            t = list.get(0);
        }
        return t;
    }
}
