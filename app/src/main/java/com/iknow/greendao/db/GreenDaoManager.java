package com.iknow.greendao.db;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import com.iknow.greendao.gen.DaoMaster;
import com.iknow.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;

import de.greenrobot.daogenerator.Schema;

/**
 * Created by chenbaoqiang on 2017/11/30.
 * http://blog.csdn.net/wjk343977868/article/details/53943135
 */

public class GreenDaoManager {

    private static final String TAG = "greendao_"+GreenDaoManager.class.getSimpleName();
    private String DB_NAME = "feinno.db";//数据库名称
    private String DB_PWD = "1234";//数据库加密秘钥
    private volatile static GreenDaoManager mDaoManager;//多线程访问
    private DaoMaster.OpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase db;
    private Context mContext;
    private Schema schema;
    /**
     * 数据库是否加密
     */
    private boolean encrypted = true;

    /**
     * 使用单例模式获得操作数据库的对象
     *
     * @return
     */
    public static GreenDaoManager getInstance() {
        if (mDaoManager == null) {
            synchronized (GreenDaoManager.class) {
                mDaoManager = new GreenDaoManager();
            }
        }
        return mDaoManager;
    }

    /**
     * 初始化数据库
     *
     * @param context
     * @param dbName    数据库名称
     * @param encrypted 是否加密
     * @param dbPwd     数据库秘钥
     */
    public void init(Application context, String dbName, boolean encrypted, String dbPwd) {
        this.mContext = context;
        this.DB_NAME = dbName;
        this.encrypted = encrypted;
        this.DB_PWD = (TextUtils.isEmpty(dbPwd)) ? DB_PWD : dbPwd;
    }

    /**
     * 判断数据库是否存在，如果不存在则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {

            ContextWrapper wrapper = new ContextWrapper(mContext) {
                /**
                 * 获得数据库路径，如果不存在，则创建对象对象
                 *
                 * @param name
                 */
                @Override
                public File getDatabasePath(String name) {
                    // 判断是否存在sd卡
                    boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
                    if (!sdExist) {// 如果不存在,
                        Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
                        return null;
                    } else {// 如果存在
                        // 获取sd卡路径
                        String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                        dbDir += "/Android";// 数据库所在目录
                        String dbPath = dbDir + "/" + name;// 数据库路径
                        // 判断目录是否存在，不存在则创建该目录
                        File dirFile = new File(dbDir);
                        if (!dirFile.exists())
                            dirFile.mkdirs();

                        // 数据库文件是否创建成功
                        boolean isFileCreateSuccess = false;
                        // 判断文件是否存在，不存在则创建该文件
                        File dbFile = new File(dbPath);
                        if (!dbFile.exists()) {
                            try {
                                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else
                            isFileCreateSuccess = true;
                        // 返回数据库文件对象
                        if (isFileCreateSuccess) {
                            Log.e(TAG, "getDatabasePath: db path = " + dbFile.getAbsolutePath());
                            return dbFile;

                        } else {
                            Log.e(TAG, "getDatabasePath: db path = " + getDatabasePath(name));
                            return super.getDatabasePath(name);

                        }
                    }
                }

                /**
                 * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
                 *
                 * @param name
                 * @param mode
                 * @param factory
                 */
                @Override
                public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                    return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                }

                /**
                 * Android 4.0会调用此方法获取数据库。
                 *
                 * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
                 *      int,
                 *      android.database.sqlite.SQLiteDatabase.CursorFactory,
                 *      android.database.DatabaseErrorHandler)
                 * @param name
                 * @param mode
                 * @param factory
                 * @param errorHandler
                 */
                @Override
                public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                    return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                }
            };
            //适用于未加密的数据库
            if (encrypted) {
                //适用于加密的数据库
//               mHelper = new MyEncryptedSQLiteOpenHelper(wrapper, DB_NAME, null);//用于修改数据库路径

                mHelper = new MyEncryptedSQLiteOpenHelper(mContext, DB_NAME, null);
                mDaoMaster = new DaoMaster(mHelper.getEncryptedWritableDb(DB_PWD));//获取可读写的加密数据库
            } else {
//            mHelper = new MyOpenHelper(wrapper, DB_NAME);//用于修改数据库路径
                mHelper = new MyOpenHelper(mContext, DB_NAME);
                mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
            }


        }
        return mDaoMaster;
    }


    /**
     * 完成对数据库的增删查找
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            if (null == mDaoMaster) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 设置debug模式开启或关闭，默认关闭
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }


    public Schema getSchema() {
        return schema;
    }


}
