package com.speedata.javatest;

import java.util.Arrays;
import java.util.Base64;

public class myClass {

    public static void main(String[] args) {

        String content = "RfQGDEXENzVKalY52MqdYKkL5OCh4E+Io+y39x86FjWaNoxpyqR8ynTkbbI4hr1yeN0lxgVIp7XmVsH42nYNZP+11Ho0G1I8MRJSqBLYIiOEPbBwPnwkDVBcCatH/3K6GbcyAB/+AE9o5w0ziCstPkg8XAAAW/Jpza217OE5BUodSCgizJo/iEf0Bgw=";
        String key = "MIICXQIBAAKBgQC7MzvccDzRXI0+zm5j7nFDw4sRrVCLRK6jSL5ciWpkSDjluDcfbRLtyFWSZKCIPcuKNAnJb+BOEZ9PY84DhqgucuUpF+mFV979cmJzEV5QI9Z15hmL3c8uaStnVwb4/TKlb8kRIAKSUX+VhU/5gqDPKGpkM24/w86MCmv2TDMrEwIDAQABAoGAF/E/kLwSKzzJG+VPHRzcA6y41rPx/z3zkBK3tiIlJ1pNXKQo+K38o8jhmO2h0osEw1JQhiD28UUzuPWs3aqVS9N4rVQiqTVh0ZtaMk/O7jImY0TYp3CxaXnGbi+YKxce7pN11wyTSZzFXkcyAHgOOuPB89ZBpy+60j5dZ2TKGqECQQDmKFeIxlpWJxTYrYFJ0t3E/MTMoNIhWPrAeEdFAPDjNjz6KMUPM0re7FKTYyWXiq/gvr70yNEtpWYdxNbxNeJrAkEA0DgfDIqpwNELcXV9O4EAWyrjo4Wqm4MGLUDW8/WzlX7kJCDcoxgGBozQ0wIn95ZOnrA9NqjuRPsXmD8a4/QT+QJBAN/vPoP4oiqS6eiMoG/IuDgSzBRhITKLFaaxIH7WFIqt2hR4GIvaly1g+FMpM4lHyio8zST0QvpLpJiwTYXxrLMCQQCBCSaEuTH14ha7W1oelBVKakvkPKO79/jN9o6/ZRbDoH11vj+9etfRG1cWTRHDp4xVX1awFwSCDzFSEllxxwLRAkAmLqvO0pQpVDjfYKJPquAsrNSqKotdVsiywjAdz6dXFfAl0UXxp+clhU1t1YThCVEGdPDTt9FeKCqbX29pN7J9";
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

        String cardId = ByteUtils.bytes2Hex(Arrays.copyOfRange(convertBytes, 0, 3));
        System.out.println(cardId);

    }

    private static byte[] getFinalByte(byte[] convertBytes) {

        return Arrays.copyOfRange(convertBytes, 0, convertBytes.length - 4);
    }
}
