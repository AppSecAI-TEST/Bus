package com.speedata.javatest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class myClass {

    public static void main(String[] args) throws Exception {

        String content = "RfQGDEXENzVKalY52MqdYKkL5OCh4E+Io+y39x86FjWaNoxpyqR8ynTkbbI4hr1yeN0lxgVIp7XmVsH42nYNZP+11Ho0G1I8MRJSqBLYIiOEPbBwPnwkDVBcCatH/3K6GbcyAB/+AE9o5w0ziCstPkg8XAAAW/Jpza217OE5BUodSCgizJo/iEf0Bgw=";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALszO9xwPNFcjT7ObmPucUPDixGtUItErqNIvlyJamRIOOW4Nx9tEu3IVZJkoIg9y4o0Cclv4E4Rn09jzgOGqC5y5SkX6YVX3v1yYnMRXlAj1nXmGYvdzy5pK2dXBvj9MqVvyREgApJRf5WFT/mCoM8oamQzbj/DzowKa/ZMMysTAgMBAAECgYAX8T+QvBIrPMkb5U8dHNwDrLjWs/H/PfOQEre2IiUnWk1cpCj4rfyjyOGY7aHSiwTDUlCGIPbxRTO49azdqpVL03itVCKpNWHRm1oyT87uMiZjRNincLFpecZuL5grFx7uk3XXDJNJnMVeRzIAeA4648Hz1kGnL7rSPl1nZMoaoQJBAOYoV4jGWlYnFNitgUnS3cT8xMyg0iFY+sB4R0UA8OM2PPooxQ8zSt7sUpNjJZeKr+C+vvTI0S2lZh3E1vE14msCQQDQOB8MiqnA0QtxdX07gQBbKuOjhaqbgwYtQNbz9bOVfuQkINyjGAYGjNDTAif3lk6esD02qO5E+xeYPxrj9BP5AkEA3+8+g/iiKpLp6Iygb8i4OBLMFGEhMosVprEgftYUiq3aFHgYi9qXLWD4UykziUfKKjzNJPRC+kukmLBNhfGsswJBAIEJJoS5MfXiFrtbWh6UFUpqS+Q8o7v3+M32jr9lFsOgfXW+P71619EbVxZNEcOnjFVfVrAXBIIPMVISWXHHAtECQCYuq87SlClUON9gok+q4Cys1Koqi11WyLLCMB3Pp1cV8CXRRfGn5yWFTW3VhOEJUQZ08NO30V4oKptfb2k3sn0=";
        //base64转码
        byte[] base64DecodeByte = Base64.getDecoder().decode(content);
        //byte[]  >> long[];
        long[] longBytes = ByteUtils.byteArrToLongArr(base64DecodeByte);
        //long[] xor XOR => long[]
        long xor = 201782342;
        long[] xorLongBytes = ByteUtils.xor(longBytes, xor);
        //long[] => byte[]
        byte[] convertBytes = ByteUtils.longArrToByteArr(xorLongBytes);
        byte[] finalBytes = getFinalByte(convertBytes);
        System.out.println(Arrays.toString(finalBytes));

        int cardId = ByteUtils.bytesToInt(ByteUtils.sub(finalBytes,0,4));
        System.out.println(cardId);
        int cityLength = ByteUtils.sub(finalBytes,4,1)[0];
        String cityId = new String(ByteUtils.sub(finalBytes,5,cityLength));
        System.out.println(cityId);
        byte[] rsaByte = ByteUtils.sub(finalBytes,5+cityLength,128);

        byte[] decodeRSA = RSAUtils3.decryptByPrivateKey(rsaByte,privateKey);

        long userId = ByteUtils.byteToLong(ByteUtils.sub(decodeRSA,0,8));
        System.out.println(userId);
        long time = ByteUtils.byteToLong(ByteUtils.sub(decodeRSA,8,16));
        System.out.println(time);

    }

    private static byte[] getFinalByte(byte[] convertBytes) {

        return Arrays.copyOfRange(convertBytes, 0, convertBytes.length - 4);
    }


    private static String create(String qrCode) throws IOException {
        // 协议类型(2字节) + 原数据长度（2字节）+ 原码数据+ POSID长度（2字节）+ POSID + BUSID长度（2字节）+ BUSID + 刷卡时间戳（8字节）+ 交易流水号长度（2字节）+ 交易流水号（由POS机生成）+ 线路编号长度(2字节) + 线路编号 + 上/下车站点/序号长度(2字节) + 上/下车站点/序号 + 分段计价标识(1个字节 0-固定价格 1-分段计价，如果没传，则默认0)
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        // 协议类型
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

        byte[] arr = bout.toByteArray();
        bout.close();
        // XOR 操作
        long[] larr = ByteUtils.byteArrToLongArr(arr);
        long[] xlarr = ByteUtils.xor(larr, 201782342);
        byte[] x = ByteUtils.longArrToByteArr(xlarr);
        return Base64.getEncoder().encodeToString(x);
    }
}
