package com.iknow.greendao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iknow.greendao.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.EncryptedDatabase;

/**
 * Created by chenbaoqiang on 2017/12/9.
 *
 * 适用于加密的数据库
 */


public class MyEncryptedSQLiteOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = "greendao_"+MyEncryptedSQLiteOpenHelper.class.getSimpleName();
    private final Context context;
    private final String name;
    private final int version = DaoMaster.SCHEMA_VERSION;

    private boolean loadSQLCipherNativeLibs = true;

    public MyEncryptedSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        this.context=context;
        this.name=name;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

//        EncryptedMigrationHelper.migrate((EncryptedDatabase) db,AreaDao.class, PeopleDao.class, ProductDao.class);
        Log.e(TAG,"upgrade run success");
    }

    @Override
    public Database getEncryptedWritableDb(String password) {
        MyEncryptedHelper encryptedHelper = new MyEncryptedHelper(context,name,version,loadSQLCipherNativeLibs);
        return encryptedHelper.wrap(encryptedHelper.getReadableDatabase(password));
    }



    public class MyEncryptedHelper extends net.sqlcipher.database.SQLiteOpenHelper {
        public MyEncryptedHelper(Context context, String name, int version, boolean loadLibs) {
            super(context, name, null, version);
            if (loadLibs) {
                net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
            }
        }

        @Override
        public void onCreate(net.sqlcipher.database.SQLiteDatabase db) {
            MyEncryptedSQLiteOpenHelper.this.onCreate(wrap(db));
        }

        @Override
        public void onUpgrade(net.sqlcipher.database.SQLiteDatabase db, int oldVersion, int newVersion) {
            MyEncryptedSQLiteOpenHelper.this.onUpgrade(wrap(db), oldVersion, newVersion);
        }

        @Override
        public void onOpen(net.sqlcipher.database.SQLiteDatabase db) {
            MyEncryptedSQLiteOpenHelper.this.onOpen(wrap(db));
        }

        protected Database wrap(net.sqlcipher.database.SQLiteDatabase sqLiteDatabase) {
            return new EncryptedDatabase(sqLiteDatabase);
        }
    }

}
