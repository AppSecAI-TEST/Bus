package com.speedata.bus;

import android.app.Application;

import com.speedata.bus.db.DaoMaster;
import com.speedata.bus.db.DaoSession;
import com.speedata.bus.db.DbHelper;

import org.greenrobot.greendao.database.Database;

/**
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 *
 * @author :Reginer in  2017/8/7 6:52.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class AppBus extends Application{



    private static AppBus sInstance;
    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        DbHelper helper = new DbHelper(this, "bus", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static AppBus getsInstance() {
        return sInstance;
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
