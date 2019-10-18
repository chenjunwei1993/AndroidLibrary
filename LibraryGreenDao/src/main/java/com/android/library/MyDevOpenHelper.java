package com.android.library;

import android.content.Context;

import com.android.library.dao.DBDetailBeanDao;
import com.android.library.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;


/**
 * @author chenjunwei
 * 1.自定义  MySQLiteOpenHelper继承DaoMaster.OpenHelper 重写更新数据库的方法
 * 2.当app下的build.gradle  的schemaVersion数据库的版本号改变时，创建数据库会调用onUpgrade更细数据库的方法
 * @date 2019-09-26
 */
public class MyDevOpenHelper extends DaoMaster.DevOpenHelper {

    public MyDevOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //操作数据库的更新 有几个表升级都可以传入到下面
            MigrationHelper.getInstance().migrate(db, DBDetailBeanDao.class);
        }
    }
}
