package com.speedata.bus.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.honeywell.barcode.ActiveCamera;
import com.honeywell.barcode.HSMDecodeResult;
import com.honeywell.barcode.HSMDecoder;
import com.honeywell.camera.CameraManager;
import com.honeywell.license.ActivationManager;
import com.honeywell.license.ActivationResult;
import com.honeywell.plugins.decode.DecodeResultListener;
import com.speedata.bus.PreviewService;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import static com.honeywell.barcode.Symbology.QR;

/**
 * Created by lenovo-pc on 2017/8/4.
 */

public class ScanDecode implements DecodeResultListener {
    private HSMDecoder hsmDecoder;
    private Context context;
    private CameraManager cameraManager;
    private boolean IsUtf8 = false;

    public ScanDecode(Context context) {
        this.context = context;
        initScan();
    }

    public void initScan() {
        try {
            //activate the API with your license key   trial-speed-tjian-03162017     trial-speed-tjian-08072017
            ActivationResult activationResult = ActivationManager.activate(context, "trial-speed-tjian-08072017");
            Toast.makeText(context, "Activation Result: " + activationResult, Toast.LENGTH_LONG).show();
            //get the singleton instance of the decoder
            hsmDecoder = HSMDecoder.getInstance(context);
            hsmDecoder.enableSymbology(QR);
//            PlaySound.initSoundPool(context);
            //           enableDecodeFlag();
            hsmDecoder.enableAimer(true);
            hsmDecoder.setAimerColor(Color.RED);
            hsmDecoder.enableSound(false);
            hsmDecoder.setOverlayText("条码至于预览框内");
            hsmDecoder.setOverlayTextColor(Color.WHITE);
            cameraManager = CameraManager.getInstance(context);
            hsmDecoder.addResultListener(this);
            //初始为默认前置摄像头扫码
            hsmDecoder.setActiveCamera(ActiveCamera.FRONT_FACING);//前置 摄像头

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startScan() {
        Intent intent = new Intent();
        intent.setClass(context, PreviewService.class);
        intent.setAction("com.Fxservice");
        intent.setPackage("com.scanbarcodeservice");
        context.startService(intent);

//        hsmDecoder.scanBarcode();
    }

    public void DestroyScan() {
        hsmDecoder.removeResultListener(this);
        HSMDecoder.disposeInstance();
        Intent intent = new Intent();
        intent.setClass(context, PreviewService.class);
        intent.setAction("com.Fxservice");
        intent.setPackage("com.scanbarcodeservice");
        context.stopService(intent);
    }

    /**
     * 返回解码结果
     *
     * @param hsmDecodeResults
     */
    @Override
    public void onHSMDecodeResult(HSMDecodeResult[] hsmDecodeResults) {
        displayBarcodeData(hsmDecodeResults);
    }

    String isdecodeDate = null;

    /**
     * 显示数据
     *
     * @param barcodeData 解码数据
     */
    private void displayBarcodeData(HSMDecodeResult[] barcodeData) {
        if (barcodeData.length > 0) {
            HSMDecodeResult firstResult = barcodeData[0];
//            hsmDecoder.enableSound(false);
            String decodeDate = null;

            if (isUTF8(firstResult.getBarcodeDataBytes())) {
                try {
                    decodeDate = new String(firstResult.getBarcodeDataBytes(),
                            "utf8");
                    if (decodeDate.equals(isdecodeDate)) {
                        return;
                    } else {
//                        hsmDecoder.enableSound(true);
                        isdecodeDate = decodeDate;
//                        PlaySound.play(2, 0);
                        EventBus.getDefault().post(new Myeventbus(decodeDate));
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //判断扫描的内容是否是UTF8的中文内容
    private boolean isUTF8(byte[] sx) {
        //Log.d(TAG, "begian to set codeset");
        for (int i = 0; i < sx.length; ) {
            if (sx[i] < 0) {
                if ((sx[i] >>> 5) == 0x7FFFFFE) {
                    if (((i + 1) < sx.length) && ((sx[i + 1] >>> 6) == 0x3FFFFFE)) {
                        i = i + 2;
                        IsUtf8 = true;
                    } else {
                        if (IsUtf8)
                            return true;
                        else
                            return false;
                    }
                } else if ((sx[i] >>> 4) == 0xFFFFFFE) {
                    if (((i + 2) < sx.length) && ((sx[i + 1] >>> 6) == 0x3FFFFFE) && ((sx[i + 2] >>> 6) == 0x3FFFFFE)) {
                        i = i + 3;
                        IsUtf8 = true;
                    } else {
                        if (IsUtf8)
                            return true;
                        else
                            return false;
                    }
                } else {
                    if (IsUtf8)
                        return true;
                    else
                        return false;
                }
            } else {
                i++;
            }
        }
        return true;
    }
}
