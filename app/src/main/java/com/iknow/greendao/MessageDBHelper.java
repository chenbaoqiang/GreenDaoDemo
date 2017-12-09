package com.iknow.greendao;

import android.app.Application;
import android.util.Log;

import com.iknow.greendao.bean.Message;
import com.iknow.greendao.db.BaseDBHelper;
import com.iknow.greendao.gen.MessageDao;

import java.util.List;

/**
 * Created by chenbaoqiang on 2017/11/30.
 */

public class MessageDBHelper extends BaseDBHelper<Message> {

    private final MessageDao messageDao;

    public MessageDBHelper(Application context) {
        super(context,Message.class);
        messageDao = (MessageDao) dao;
    }

    public void insertMessage(Message message){
        insertObject(message);
    }


    private  void addMessage(Message message) {


//        schema = new Schema(1, "com.iknow.greendao.gen");
//        try {
////            new DaoGenerator().generateAll(schema, "./app/src/main/java");
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage(), e);
//        }
//        Entity note = schema.addEntity(Message.class.getSimpleName());
//        note.setTableName(message.getUri()+"_msg");
//        note.addIdProperty().primaryKey().autoincrement();
//        note.addStringProperty("text").notNull();
//        note.addStringProperty("comment");
//        note.addDateProperty("date");

    }

    public void query(){
        List<Message> messages = queryAll(Message.class);
        for (int i = 0; i <messages.size() ; i++) {
            Message message = messages.get(i);
            Log.i(TAG, "query: messgae = " +message);
        }
    }


}
