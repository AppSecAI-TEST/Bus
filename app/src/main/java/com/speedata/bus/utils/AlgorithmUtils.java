package com.speedata.bus.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
 * @author :Reginer in  2017/8/4 2:19.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class AlgorithmUtils {
    private static final long XOR = 201782342;
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALszO9xwPNFcjT7ObmPucUPDixGtUItErqNIvlyJamRIOOW4Nx9tEu3IVZJkoIg9y4o0Cclv4E4Rn09jzgOGqC5y5SkX6YVX3v1yYnMRXlAj1nXmGYvdzy5pK2dXBvj9MqVvyREgApJRf5WFT/mCoM8oamQzbj/DzowKa/ZMMysTAgMBAAECgYAX8T+QvBIrPMkb5U8dHNwDrLjWs/H/PfOQEre2IiUnWk1cpCj4rfyjyOGY7aHSiwTDUlCGIPbxRTO49azdqpVL03itVCKpNWHRm1oyT87uMiZjRNincLFpecZuL5grFx7uk3XXDJNJnMVeRzIAeA4648Hz1kGnL7rSPl1nZMoaoQJBAOYoV4jGWlYnFNitgUnS3cT8xMyg0iFY+sB4R0UA8OM2PPooxQ8zSt7sUpNjJZeKr+C+vvTI0S2lZh3E1vE14msCQQDQOB8MiqnA0QtxdX07gQBbKuOjhaqbgwYtQNbz9bOVfuQkINyjGAYGjNDTAif3lk6esD02qO5E+xeYPxrj9BP5AkEA3+8+g/iiKpLp6Iygb8i4OBLMFGEhMosVprEgftYUiq3aFHgYi9qXLWD4UykziUfKKjzNJPRC+kukmLBNhfGsswJBAIEJJoS5MfXiFrtbWh6UFUpqS+Q8o7v3+M32jr9lFsOgfXW+P71619EbVxZNEcOnjFVfVrAXBIIPMVISWXHHAtECQCYuq87SlClUON9gok+q4Cys1Koqi11WyLLCMB3Pp1cV8CXRRfGn5yWFTW3VhOEJUQZ08NO30V4oKptfb2k3sn0=";

    /**
     * 获取二维码数据 .
     *
     * @param qrCodeContent 二维码内容
     * @return 二维码数据
     */
    public static byte[] getQrCodeByte(String qrCodeContent) {
        //base64转码
        byte[] base64DecodeByte = android.util.Base64.decode(qrCodeContent, android.util.Base64.NO_WRAP);
        //byte[]  >> long[];
        long[] longBytes = ByteUtils.byteArrToLongArr(base64DecodeByte);
        //long[] xor XOR => long[]
        long[] xorLongBytes = ByteUtils.xor(longBytes, XOR);
        //long[] => byte[]
        byte[] convertBytes = ByteUtils.longArrToByteArr(xorLongBytes);
        return getFinalByte(convertBytes);
    }

    /**
     * 获取主密钥id.
     *
     * @param qrCodeByte 二维码数据
     * @return 主密钥id
     */
    public static int getKeyId(byte[] qrCodeByte) {
        return ByteUtils.bytesToInt(ByteUtils.sub(qrCodeByte, 0, 4));
    }

    /**
     * 获取城市占位字节数.
     *
     * @param qrCodeByte 二维码数据
     * @return 城市占位字节数
     */
    public static int getCityLength(byte[] qrCodeByte) {
        return ByteUtils.sub(qrCodeByte, 4, 1)[0];
    }

    /**
     * 获取城市id.
     *
     * @param qrCodeByte 二维码数据
     * @param cityLength 城市id占位数
     * @return 城市id
     */
    public static String getCityId(byte[] qrCodeByte, int cityLength) {
        return new String(ByteUtils.sub(qrCodeByte, 5, cityLength));
    }

    /**
     * 获取rsa加密数据 .
     *
     * @param qrCodeByte 二维码数据
     * @param length     截取起始位置
     * @return sa加密数据
     */
    public static byte[] getRsaByte(byte[] qrCodeByte, int length) {
        return ByteUtils.sub(qrCodeByte, length, 128);
    }

    /**
     * 获取RSAj解密数据.
     *
     * @param rsaByte rs a加密前数据
     * @return RSA解码数据
     */
    public static byte[] getRsaDecodeByte(byte[] rsaByte) {
        byte[] decodeRSA = new byte[0];
        try {
            decodeRSA = RSAUtils3.decryptByPrivateKey(rsaByte, PRIVATE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeRSA;
    }

    /**
     * 获取账户id .
     *
     * @param rsaByte rsa解密数据
     * @return 账户id
     */
    public static long getAccountId(byte[] rsaByte) {
        return ByteUtils.byteToLong(ByteUtils.sub(rsaByte, 0, 8));
    }

    /**
     * 获取时间戳 .
     *
     * @param rsaByte rsa解密数据
     * @return 时间戳
     */
    public static long getTimestamp(byte[] rsaByte) {
        return ByteUtils.byteToLong(ByteUtils.sub(rsaByte, 8, 8));
    }

    /**
     * 获取时间是否在允许范围内.
     *
     * @param rsaByte rsa解密数据
     * @return 开关
     */
    public static boolean isAllowTime(byte[] rsaByte) {
        long timestamp = getTimestamp(rsaByte);

        Log.d("Reginer", "timestamp   is: " + timestamp);
        int startTime = ByteUtils.sub(rsaByte, 16, 1)[0];
        int endTime = ByteUtils.sub(rsaByte, 17, 1)[0];
        long allowStartTime = timestamp + startTime * 60 * 1000;
        long allowEndTime = timestamp + endTime * 60 * 1000;
        return allowStartTime < System.currentTimeMillis() && System.currentTimeMillis() < allowEndTime;
    }

    /**
     * 组装请求体.
     *
     * @param qrCode 二维码数据
     * @return 伪码
     */
    public static String createBody(String qrCode, byte state) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        // 协议类型
        try {
            bout.write(ByteUtils.shortToByte((short) 12));
            // 原始数据
//        byte[] qrCodes = qrCode.getBytes();
//        String qrCodeStr = toQrCodeBase64(qrCode);
            byte[] qrCodes = qrCode.getBytes();
            // 二维码信息
            bout.write(ByteUtils.shortToByte((short) qrCodes.length));
            bout.write(qrCodes);
            String busId = "5-1323";
            String lineNo = "19路";
            String stopNo = "3";
            String posId = "2-1232342432";
            Long ts = System.currentTimeMillis();
            byte segFlg = 0;
            String posSnum = "xxsdfklsadlf12313adkalsdjaldka";

            // POSID
            bout.write(ByteUtils.shortToByte((short) posId.getBytes().length));
            bout.write(posId.getBytes());

            // BUSID
            bout.write(ByteUtils.shortToByte((short) busId.getBytes().length));
            bout.write(busId.getBytes());

            // 刷卡时间戳
            bout.write(ByteUtils.longToByte(ts));

            // 交易流水号
            bout.write(ByteUtils.shortToByte((short) posSnum.getBytes().length));
            bout.write(posSnum.getBytes());

            // 线路编号
            bout.write(ByteUtils.shortToByte((short) lineNo.getBytes().length));
            bout.write(lineNo.getBytes());

            // 上下车站序/名称
            bout.write(ByteUtils.shortToByte((short) stopNo.getBytes().length));
            bout.write(stopNo.getBytes());

            bout.write(segFlg);
            bout.write((byte) 1);
            bout.write(state);

            byte[] arr = bout.toByteArray();
            bout.close();
            // XOR 操作
            long[] larr = ByteUtils.byteArrToLongArr(arr);
            long[] xlarr = ByteUtils.xor(larr, XOR);
            byte[] x = ByteUtils.longArrToByteArr(xlarr);
            return Base64.encodeToString(x, Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获取二维码流水数 .
     *
     * @param rsaByte rsa解密数据
     * @return 二维码流水数
     */
    public static int getAccountFlow(byte[] rsaByte) {
        return ByteUtils.sub(rsaByte, 18, 1)[0];
    }

    /**
     * 从尾部开始去掉byte为0的数据,直到遇到byte1为⽌止 => byte[]
     * 去掉最后⼀一个字节 => byte[] .
     *
     * @param convertBytes 目标byte
     * @return 真实byte
     */
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
