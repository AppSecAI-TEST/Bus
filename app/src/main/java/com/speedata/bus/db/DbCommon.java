package com.speedata.bus.db;

import android.util.Base64;
import android.util.Log;

import com.speedata.bus.AppBus;
import com.speedata.bus.R;
import com.speedata.bus.net.NetManager;
import com.speedata.bus.utils.AlgorithmUtils;
import com.speedata.bus.utils.PlaySound;

import java.util.List;


public class DbCommon {

    /**
     * 将需要上传到网络的内容保存到数据库 .
     *
     * @param content 二维码内容
     */
    public  static void saveInDb(String content) {
        //获取二维码真码
        byte[] qrCodeByte = AlgorithmUtils.getQrCodeByte(content);
        //获取主密钥id
        int keyId = AlgorithmUtils.getKeyId(qrCodeByte);
        //获取城市占位字节数
        int cityLength = AlgorithmUtils.getCityLength(qrCodeByte);
        //获取城市id
        String cityId = AlgorithmUtils.getCityId(qrCodeByte, cityLength);
        //获取RSA加密数据
        byte[] rsaByte = AlgorithmUtils.getRsaByte(qrCodeByte, 5 + cityLength);
        //获取RSA解密数据
        byte[] decodeRSA = AlgorithmUtils.getRsaDecodeByte(rsaByte);
        //获取账户id
        long userId = AlgorithmUtils.getAccountId(decodeRSA);
        //获取扫码时间是否允许
        boolean isAllowTime = AlgorithmUtils.isAllowTime(decodeRSA);
        String qrCode = Base64.encodeToString(qrCodeByte, Base64.NO_WRAP);
        String body;
        if (isAllowTime){
            body = AlgorithmUtils.createBody(qrCode,(byte) 0);
            PlaySound.playerSound(AppBus.getsInstance(),R.raw.success);
        }else {
            body = AlgorithmUtils.createBody(qrCode,(byte) 1);
            PlaySound.playerSound(AppBus.getsInstance(),R.raw.flash);
        }
        QrBody qrBody = new QrBody(body, cityId, true);
        QrBodyDao mDao = AppBus.getsInstance().getDaoSession().getQrBodyDao();
        mDao.insertOrReplace(qrBody);
        List<QrBody> qrBodyList = mDao.loadAll();
        Log.d("Reginer", "saveInDb  qrBodyList.size  is:::" + qrBodyList.size());
    }
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
