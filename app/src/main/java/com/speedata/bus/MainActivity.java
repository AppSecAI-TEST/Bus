package com.speedata.bus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.speedata.bus.utils.AlgorithmUtils;

import java.util.Arrays;

import okhttp3.Call;
import win.reginer.http.RHttp;
import win.reginer.http.callback.StringCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String content = "RfQGDEXENzVKalY52MqdYKkL5OCh4E+Io+y39x86FjWaNoxpyqR8ynTkbbI4hr1yeN0lxgVIp7XmVsH42nYNZP+11Ho0G1I8MRJSqBLYIiOEPbBwPnwkDVBcCatH/3K6GbcyAB/+AE9o5w0ziCstPkg8XAAAW/Jpza217OE5BUodSCgizJo/iEf0Bgw=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        test();
    }

    private void test() {
        //获取二维码真码
        byte[] qrCodeByte = AlgorithmUtils.getQrCodeByte(content);
        System.out.println(Arrays.toString(qrCodeByte));
        //获取主密钥id
        int keyId = AlgorithmUtils.getKeyId(qrCodeByte);
        System.out.println(keyId);
        //获取城市占位字节数
        int cityLength = AlgorithmUtils.getCityLength(qrCodeByte);
        //获取城市id
        String cityId = AlgorithmUtils.getCityId(qrCodeByte, cityLength);
        System.out.println(cityId);
        //获取RSA加密数据
        byte[] rsaByte = AlgorithmUtils.getRsaByte(qrCodeByte, 5 + cityLength);
        //获取RSA解密数据
        byte[] decodeRSA = AlgorithmUtils.getRsaDecodeByte(rsaByte);
        //获取账户id
        long userId = AlgorithmUtils.getAccountId(decodeRSA);
        System.out.println(userId);
        //获取扫码时间是否允许
        boolean isAllowTime = AlgorithmUtils.isAllowTime(decodeRSA);
        System.out.println(isAllowTime);
        int accountFlow = AlgorithmUtils.getAccountFlow(decodeRSA);
        System.out.println("accountFlow  is::" + accountFlow);

        postString(qrCodeByte, cityId);

    }

    /**
     * 组装上传数据.
     *
     * @param qrCodeByte 二维码数据
     * @param cityId     城市id
     */
    private void postString(byte[] qrCodeByte, String cityId) {
        String url = String.format("https://dev.chelaile.net.cn/buspay/api/trade/offline?cityId=%s", cityId);
        String qrCode = Base64.encodeToString(qrCodeByte, Base64.NO_WRAP);
        String body = AlgorithmUtils.createBody(qrCode);
        Log.d("Reginer", "postString:  is:::" + body);
        RHttp.postString().url(url).content(body).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("Reginer", "onError: " + Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(String response) {
                Log.d("Reginer", "onResponse: " + response);
            }
        });
    }
}
