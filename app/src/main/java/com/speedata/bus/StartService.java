package com.speedata.bus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.speedata.bus.db.QrBody;
import com.speedata.bus.db.QrBodyDao;
import com.speedata.bus.utils.AlgorithmUtils;
import com.speedata.bus.utils.Myeventbus;
import com.speedata.bus.utils.ScanDecode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class StartService extends Service {
    private String content = null;
    private ScanDecode scanDecode = null;

    public StartService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
        scanDecode = new ScanDecode(this);
        scanDecode.startScan();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDecodeMsG(Myeventbus myeventbus) {
        content = myeventbus.getDecodeMsg();
        if (!TextUtils.isEmpty(content)) {
//            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
            saveInDb(content);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 将需要上传到网络的内容保存到数据库 .
     *
     * @param content 二维码内容
     */
    private void saveInDb(String content) {
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
        }else {
            body = AlgorithmUtils.createBody(qrCode,(byte) 1);
        }
        QrBody qrBody = new QrBody(body, cityId, true);
        QrBodyDao mDao = AppBus.getsInstance().getDaoSession().getQrBodyDao();
        mDao.insertOrReplace(qrBody);
        List<QrBody> qrBodyList = mDao.loadAll();
        Log.d("Reginer", "saveInDb  qrBodyList.size  is:::" + qrBodyList.size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
        scanDecode.DestroyScan();
    }
}
