package com.speedata.bus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.speedata.bus.utils.AlgorithmUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String content = "RfQGDEXENzVKalY52MqdYKkL5OCh4E+Io+y39x86FjWaNoxpyqR8ynTkbbI4hr1yeN0lxgVIp7XmVsH42nYNZP+11Ho0G1I8MRJSqBLYIiOEPbBwPnwkDVBcCatH/3K6GbcyAB/+AE9o5w0ziCstPkg8XAAAW/Jpza217OE5BUodSCgizJo/iEf0Bgw=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
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
        int accountFlow =AlgorithmUtils.getAccountFlow(decodeRSA);
        System.out.println("accountFlow  is::" +accountFlow);
    }

    private static byte[] getFinalByte(byte[] convertBytes) {
        int length = 0;
        for (int i = convertBytes.length - 1; i >= 0; i--) {
            if (convertBytes[i] == 0 && convertBytes[i - 1] == 1) {
                length = i;
            }
        }
        return Arrays.copyOfRange(convertBytes, 0, length - 1);
    }
}
