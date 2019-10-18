package com.android.library.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class DBListBean {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "save_time")
    private String save_time;
    @Property(nameInDb = "url")
    private String url;
    @Property(nameInDb = "data")
    private String data;


    @Generated(hash = 1687366436)
    public DBListBean(Long id, String save_time, String url, String data) {
        this.id = id;
        this.save_time = save_time;
        this.url = url;
        this.data = data;
    }

    @Generated(hash = 653475956)
    public DBListBean() {
    }


    public String getSave_time() {
        return save_time;
    }

    public void setSave_time(String save_time) {
        this.save_time = save_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
