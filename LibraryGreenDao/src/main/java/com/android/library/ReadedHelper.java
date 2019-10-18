package com.android.library;

import com.android.library.bean.DBReadedBean;

import java.util.List;

/**
 * @author chenjunwei
 * @desc 新闻已读数据库
 * @date 2019-09-25
 */
public class ReadedHelper {
    /**
     * 只处理新闻的已读： 模块有：新闻、政务、专题详情
     *
     * @param module_id
     * @param id
     */
    public static void saveReaded(String module_id, String id) {
        DBReadedBean b = null;
        List list = findReaded(module_id, id);
        if (list != null && list.size() > 0) {
            b = (DBReadedBean) list.get(0);
        } else {
            b = new DBReadedBean();
        }
        b.setDataId(id);
        b.setModule_id(module_id);
        GreenDaoHelper.save(b);
    }

    /**
     * 删除已读
     *
     * @param module_id
     * @param id
     */
    public static void deleteReaded(String module_id, String id) {
        List list = findReaded(module_id, id);
        if (list != null && list.size() > 0) {
            GreenDaoHelper.delete(DBReadedBean.class, list);
        }
    }

    /**
     * 判断已读
     *
     * @param id
     * @return
     */
    public static boolean isReaded(String module_id, String id) {
        List list = findReaded(module_id, id);
        if (list == null || list.size() < 1) {
            return false;
        }
        return true;
    }

    /**
     * 查找已读的新闻
     *
     * @param module_id
     * @param id
     * @return
     */
    public static List findReaded(String module_id, String id) {
        List list = GreenDaoHelper.findAll(DBReadedBean.class, "where dataId = ? and moduleId = ?", new String[]{id, module_id});
        return list;
    }

    /**
     * 根据id判断已读
     *
     * @param id
     * @return
     */
    public static boolean isReaded(String id) {
        List list = findReaded(id);
        if (list == null || list.size() < 1) {
            return false;
        }
        return true;
    }

    /**
     * 根据id查找已读
     *
     * @param id
     * @return
     */
    public static List findReaded(String id) {
        List list = GreenDaoHelper.findAll(DBReadedBean.class, "where dataId = ? ", id);
        return list;
    }
}
