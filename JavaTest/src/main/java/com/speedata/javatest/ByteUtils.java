package com.speedata.javatest;

/**
 * Created by zhijian.zhang@chelaile.net.cn on 17/7/10.
 */
public class ByteUtils {

    /**
     * @return 两位的字节数组
     * @功能 短整型与字节的转换
     * 低位到高位
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    public static void print2Console(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (byte b :
                bs) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(b);
            i++;
        }
        System.out.println(sb.toString());
    }

    /**
     * @return 短整型
     * @功能 字节的转换与短整型
     *
     * 低位到高位
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * @return 四位的字节数组
     * @方法功能 整型与字节数组的转换
     *
     * 位到高位
     */
    public static byte[] intToByte(int i) {
        byte[] bt = new byte[4];
        bt[0] = (byte) (0xff & i);
        bt[1] = (byte) ((0xff00 & i) >> 8);
        bt[2] = (byte) ((0xff0000 & i) >> 16);
        bt[3] = (byte) ((0xff000000 & i) >> 24);
        return bt;
    }

    /**
     * @return 整型
     * @方法功能 字节数组和整型的转换
     *
     * 低位到高位
     */
    public static int bytesToInt(byte[] bytes) {
        int num = bytes[0] & 0xFF;
        num |= ((bytes[1] << 8) & 0xFF00);
        num |= ((bytes[2] << 16) & 0xFF0000);
        num |= ((bytes[3] << 24) & 0xFF000000);
        return num;
    }


    /**
     * @return 长整型
     * @方法功能 字节数组和长整型的转换
     * 低位到高位
     */
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();
            // 将最低位保存在最低位
            temp = temp >> 8;
            // 向右移8位
        }
        return b;
    }

    /**
     * @return 长整型
     * @方法功能 字节数组和长整型的转换
     *
     * 低位到高位
     */
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff; // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    public static long[] xor(long[] ls, long xorv) {
        long[] res = new long[ls.length];
        int i = 0;
        for (long b : ls) {
            res[i] = b ^ xorv;
            ++i;
        }
        return res;
    }

    public static long[] byteArrToLongArr(byte[] dest) {
        long[] res = new long[dest.length % 4 == 0 ? dest.length / 4 : dest.length / 4 + 1];

        int i = 0;
        int k = 0;
        while (i < dest.length) {
            long sum = 0;
            for (int j = 3; j >= 0; j--) {
                if (i + j < dest.length) {
                    sum = (sum << 8) + (dest[i + j] < 0 ? dest[i + j] + 256 : dest[i + j]);
                }
            }
            i += 4;
            res[k++] = sum;
        }
        return res;


    }

    public static byte[] longArrToByteArr(long[] dest) {
        byte[] res = new byte[dest.length * 4];
        int j = 0;
        int k = 0;
        while (j < dest.length) {
            long temp = dest[j];
            int u = 0;
            while (temp != 0) {
                u++;
                res[k] = (byte) (temp & 0xff);
                temp = temp >> 8;
                k++;
            }
            while (u < 4) {
                u++;
                res[k++] = 0;
            }
            j++;
        }
        return res;

    }


    public static boolean eq(byte[] b1, byte[] b2) {
        if (b1 == null && b2 == null) {
            return true;
        }
        if (b1 == null || b2 == null) {
            return false;
        }
        if (b1.length == b2.length) {
            int i = 0;
            for (byte b : b1) {
                if (b != b2[i]) {
                    System.out.println(i + " == " + b + " == " + b2[i]);
                    return false;
                }
                ++i;
            }
            return true;
        }
        return false;
    }

    public static boolean eq(long[] b1, long[] b2) {
        if (b1 == null && b2 == null) {
            return true;
        }
        if (b1 == null || b2 == null) {
            return false;
        }
        if (b1.length == b2.length) {
            int i = 0;
            for (long b : b1) {
                if (b != b2[i]) {
                    System.out.println(i + " == " + b + " == " + b2[i]);
                    return false;
                }
                ++i;
            }
            return true;
        }
        return false;
    }


    public static byte[] sub(byte[] bytes, int start, int length) {
        byte[] res = new byte[length];
        System.arraycopy(bytes, start, res, 0, length);
        return res;
    }


    public static byte[] cutZero(byte[] bytes, byte flag) {
        int index = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] == 1) {
                index = i;
                break;
            }
        }
        return sub(bytes, 0, index + 1);
    }


    /**
     * hex字符串转byte数组<br/>
     * 2个hex转为一个byte
     * @param src
     * @return
     */
    public static byte[] hex2Bytes1(String src) {
        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        int[] b = new int[2];

        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            for (int j = 0; j < 2; j++) {
                if (chs[i + j] >= '0' && chs[i + j] <= '9') {
                    b[j] = (chs[i + j] - '0');
                } else if (chs[i + j] >= 'A' && chs[i + j] <= 'F') {
                    b[j] = (chs[i + j] - 'A' + 10);
                } else if (chs[i + j] >= 'a' && chs[i + j] <= 'f') {
                    b[j] = (chs[i + j] - 'a' + 10);
                }
            }

            b[0] = (b[0] & 0x0f) << 4;
            b[1] = (b[1] & 0x0f);
            res[c] = (byte) (b[0] | b[1]);
        }
        return res;
    }

    /**
     * hex字符串转byte数组<br/>
     * 2个hex转为一个byte
     * @param src
     * @return
     */
    public static byte[] hex2Bytes(String src) {
        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            res[c] = (byte) (Integer.parseInt(new String(chs, i, 2), 16));
        }

        return res;
    }

    /**
     * byte数组转hex字符串<br/>
     * 一个byte转为2个hex字符
     * @param src
     * @return
     */
    public static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

}
