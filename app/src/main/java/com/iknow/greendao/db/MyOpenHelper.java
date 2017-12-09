package com.iknow.greendao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.iknow.greendao.gen.DaoMaster;
import com.iknow.greendao.gen.MessageDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chenbaoqiang on 2017/11/30.
 * 适用于未加密的数据库
 */

public class MyOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = "greendao_"+MyOpenHelper.class.getSimpleName();

    public MyOpenHelper(Context context, String name) {
        super(context, name);
        Log.i(TAG,"MyOpenHelper");
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading schema from version " +
                oldVersion + " to " + newVersion + " by migrating all tables data");
        // 第二个参数为要升级的Dao文件.
        MigrationHelper.getInstance().migrate(db, MessageDao.class);
    }
}
