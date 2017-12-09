package com.iknow.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by chenbaoqiang on 2017/11/30.
 */

@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private String number;
    @Transient
    private int tempUsageCount; // not persisted
    @Generated(hash = 345148039)
    public User(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}

