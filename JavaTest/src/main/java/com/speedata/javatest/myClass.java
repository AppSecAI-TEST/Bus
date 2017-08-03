package com.speedata.javatest;

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
}
