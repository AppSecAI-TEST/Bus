package com.speedata.bus.db;

import com.speedata.bus.AppBus;
import com.speedata.bus.net.NetManager;

import java.util.List;


public class DbCommon {


    /**
     * 获取上传数据并且上传
     */
    public static void getPostData() {
        List<QrBody> mList = AppBus.getsInstance().getDaoSession().getQrBodyDao().loadAll();
        if (mList!=null&&mList.size()>0){
            QrBody qrBody = mList.get(mList.size()-1);
            NetManager.postString(qrBody.getBody(),qrBody.getCityId());
        }
    }

    /**
     * 删除已经上传的数据
     */
    public static void deleteItem() {
        List<QrBody> mList = AppBus.getsInstance().getDaoSession().getQrBodyDao().loadAll();
        AppBus.getsInstance().getDaoSession().getQrBodyDao().delete(mList.get(mList.size()-1));
    }

}
