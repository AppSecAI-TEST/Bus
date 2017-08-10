package com.speedata.bus.net;

import android.util.Log;
import android.widget.Toast;

import com.speedata.bus.AppBus;
import com.speedata.bus.db.DbCommon;

import okhttp3.Call;
import win.reginer.http.RHttp;
import win.reginer.http.callback.StringCallback;


public class NetManager {

    private static final String URL = "https://dev.chelaile.net.cn/buspay/api/trade/offline?cityId=%s";

    /**
     * 组装上传数据.
     *
     * @param qrCodeByte 二维码数据
     * @param cityId     城市id
     */
    public static void postString(final String qrCodeByte, String cityId) {

        Log.d("Reginer", "postString:  is:::" + qrCodeByte);
        String url =String.format(URL,cityId);
        RHttp.postString().url(url).content(qrCodeByte).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("Reginer", "onError: " + Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(String response) {
                Log.d("Reginer", "onResponse33: " + response);
                switch (response) {
                    case "1":
                        DbCommon.deleteItem();
                        DbCommon.getPostData();
                        break;
                    case "-1":
                        Toast.makeText(AppBus.getsInstance(), "数据解析失败", Toast.LENGTH_SHORT).show();
                        break;
                    case "-2":
                        Toast.makeText(AppBus.getsInstance(), "记录数据失败，需要提示POS机重传", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }
}
