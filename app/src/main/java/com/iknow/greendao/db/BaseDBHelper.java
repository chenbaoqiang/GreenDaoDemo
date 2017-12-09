package com.iknow.greendao.db;

import android.app.Application;
import android.util.Log;


import com.iknow.greendao.gen.DaoSession;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

import de.greenrobot.daogenerator.Schema;

/**
 * Created by chenbaoqiang on 2017/11/30.
 */

public class BaseDBHelper<T> {
    public static final String TAG = "greendao_"+BaseDBHelper.class.getSimpleName();
    public static final boolean DUBUG = true;
    public GreenDaoManager manager;
    public DaoSession daoSession;
    public AbstractDao dao;
    public Schema schema;

    public BaseDBHelper(Application context, Class object) {
        manager = GreenDaoManager.getInstance();
        schema = manager.getSchema();
        daoSession = manager.getDaoSession();
        manager.setDebug(DUBUG);
        dao = daoSession.getDao(object);
    }

    /**************************数据库插入操作***********************/
    /**
     * 插入单个对象
     *
     * @param object
     * @return
     */
    protected boolean insertObject(T object) {
        boolean flag = false;
        try {
            flag = daoSession.insert(object) != -1;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return flag;
    }

    /**
     * 插入多个对象，并开启新的线程
     *
     * @param objects
     * @return
     */
    protected boolean insertMultObject(final List<T> objects) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    /**************************数据库更新操作***********************/
    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     *
     * @param object
     * @return
     */
    protected void updateObject(T object) {

        if (null == object) {
            return;
        }
        try {
            daoSession.update(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 批量更新数据
     *
     * @param objects
     * @return
     */
    protected void updateMultObject(final List<T> objects, Class clss) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {

            dao.updateInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    /**************************数据库删除操作***********************/
    /**
     * 删除某个数据库表
     *
     * @param clss
     * @return
     */
    protected boolean deleteAll(Class clss) {
        boolean flag = false;
        try {
            daoSession.deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    /**
     * 删除某个对象
     *
     * @param object
     * @return
     */
    protected void deleteObject(T object) {
        try {
            daoSession.delete(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 异步批量删除数据
     *
     * @param objects
     * @return
     */
    protected boolean deleteMultObject(final List<T> objects, Class clss) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {

            dao.deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    /**************************数据库查询操作***********************/

    /**
     * 获得某个表名
     *
     * @return
     */
    protected String getTablename(Class object) {
        return dao.getTablename();
    }

    /**
     * 查询某个ID的对象是否存在
     * @param
     * @return
     */
//    public boolean isExitObject(long id,Class object){
//        QueryBuilder<T> qb = (QueryBuilder<T>) daoSession.getDao(object).queryBuilder();
//        qb.where(UserDao.Properties.Id.eq(id));
//        long length = qb.buildCount().count();
//        return length>0;
//    }

    /**
     * 根据主键ID来查询
     *
     * @param id
     * @return
     */
    protected T queryById(long id, Class object) {
        return (T) dao.loadByRowId(id);
    }

    /**
     * 查询某条件下的对象
     *
     * @param object
     * @return
     */
    protected List<T> queryObject(Class object, String where, String... params) {
        Object obj = null;
        List<T> objects = null;
        try {
            obj = daoSession.getDao(object);
            if (null == obj) {
                return null;
            }
            objects = dao.queryRaw(where, params);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return objects;
    }

    /**
     * 查询所有对象
     *
     * @param object
     * @return
     */
    protected List<T> queryAll(Class object) {
        List<T> objects = null;
        try {
            objects = (List<T>) dao.loadAll();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return objects;
    }

    /***************************关闭数据库*************************/
    /**
     * 关闭数据库一般在Odestory中使用
     */
    protected void CloseDataBase() {
        manager.closeDataBase();
    }


}