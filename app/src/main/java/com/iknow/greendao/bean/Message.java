package com.iknow.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by chenbaoqiang on 2017/11/30.
 */

@Entity(createInDb = true)
public class Message {
    @Id
    private Long id;
    private String uri;
    private String content;
    @Unique
    private String uuid;
    @Generated(hash = 1037440366)
    public Message(Long id, String uri, String content, String uuid) {
        this.id = id;
        this.uri = uri;
        this.content = content;
        this.uuid = uuid;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUri() {
        return this.uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", content='" + content + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}

