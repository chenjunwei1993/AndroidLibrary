package com.android.library.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author chenjunwei
 * @desc 已读 入库
 * 主要是处理新闻的，所以此处的moudle_id都是news
 * 已读的处理
 * @date 2019-09-25
 */
@Entity
public class DBReadedBean {
    @Id(autoincrement = true)
    private Long id; // 数据标记字段，与业务无关
    @Property(nameInDb = "module_id")
    private String module_id;
    @Property(nameInDb = "dataId")
    private String dataId;// 业务中的id，如新闻id
    private String mark1;
    private String mark2;


    @Generated(hash = 1301733986)
    public DBReadedBean(Long id, String module_id, String dataId, String mark1,
                        String mark2) {
        this.id = id;
        this.module_id = module_id;
        this.dataId = dataId;
        this.mark1 = mark1;
        this.mark2 = mark2;
    }

    @Generated(hash = 2078204900)
    public DBReadedBean() {
    }


    public String getModule_id() {
        return this.module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getDataId() {
        return this.dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getMark1() {
        return this.mark1;
    }

    public void setMark1(String mark1) {
        this.mark1 = mark1;
    }

    public String getMark2() {
        return this.mark2;
    }

    public void setMark2(String mark2) {
        this.mark2 = mark2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
