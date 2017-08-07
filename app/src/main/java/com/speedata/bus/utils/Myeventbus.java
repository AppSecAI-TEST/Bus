package com.speedata.bus.utils;

/**
 * Created by lenovo-pc on 2017/8/4.
 */

public class Myeventbus {

    private String DecodeMsg;

    public Myeventbus(String decodeMsg) {
        DecodeMsg = decodeMsg;
    }

    public String getDecodeMsg() {
        return DecodeMsg;
    }

    public void setDecodeMsg(String decodeMsg) {
        DecodeMsg = decodeMsg;
    }
}
