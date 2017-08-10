package com.speedata.bus;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;

import com.speedata.bus.db.DaoMaster;
import com.speedata.bus.db.DaoSession;
import com.speedata.bus.db.DbHelper;
import com.umeng.analytics.MobclickAgent;

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
public class AppBus extends Application {


    private static AppBus sInstance;
    private DaoSession daoSession;
//    private ComponentName jobService;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        DbHelper helper = new DbHelper(this, "bus", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
//        jobService = new ComponentName(this, UploadingSer.class);
        Intent intent1 = new Intent();
        intent1.setClass(this, UploadingSer.class);
        startService(intent1);
//        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        JobInfo.Builder builder = new JobInfo.Builder(1,
//                new ComponentName(getPackageName(), UploadingSer.class.getName()));
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
////        builder.setPeriodic(3000);
//
//        Log.i("Reginer", "onCreate: "+scheduler.schedule(builder.build()));
//        if (scheduler.schedule(builder.build()) <= 0) {
//            //If something goes wrong
//        }
////        pollServer();
    }

//    private void pollServer() {
//        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        JobInfo jobInfo = new JobInfo.Builder(1, jobService)
////                .setMinimumLatency(5000)// 设置任务运行最少延迟时间
////                .setOverrideDeadline(60000)// 设置deadline，若到期还没有达到规定的条件则会开始执行
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)// 设置网络条件任何网络都会上传
////                    .setRequiresCharging(true)// 设置是否充电的条件
////                    .setRequiresDeviceIdle(false)// 设置手机是否空闲的条件
//                .build();
//        scheduler.schedule(jobInfo);

//    }

    public static AppBus getsInstance() {
        return sInstance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
