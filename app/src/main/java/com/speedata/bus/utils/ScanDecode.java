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

import static com.honeywell.barcode.Symbology.AZTEC;
import static com.honeywell.barcode.Symbology.C128_ISBT;
import static com.honeywell.barcode.Symbology.CODABAR;
import static com.honeywell.barcode.Symbology.CODABLOCK_F;
import static com.honeywell.barcode.Symbology.CODE11;
import static com.honeywell.barcode.Symbology.CODE128;
import static com.honeywell.barcode.Symbology.CODE39;
import static com.honeywell.barcode.Symbology.CODE93;
import static com.honeywell.barcode.Symbology.COMPOSITE;
import static com.honeywell.barcode.Symbology.COMPOSITE_WITH_UPC;
import static com.honeywell.barcode.Symbology.COUPON_CODE;
import static com.honeywell.barcode.Symbology.DATAMATRIX;
import static com.honeywell.barcode.Symbology.DATAMATRIX_RECTANGLE;
import static com.honeywell.barcode.Symbology.EAN13;
import static com.honeywell.barcode.Symbology.EAN13_2CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.EAN13_5CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.EAN13_ISBN;
import static com.honeywell.barcode.Symbology.EAN8;
import static com.honeywell.barcode.Symbology.EAN8_2CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.EAN8_5CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.GS1_128;
import static com.honeywell.barcode.Symbology.HANXIN;
import static com.honeywell.barcode.Symbology.HK25;
import static com.honeywell.barcode.Symbology.I25;
import static com.honeywell.barcode.Symbology.IATA25;
import static com.honeywell.barcode.Symbology.KOREA_POST;
import static com.honeywell.barcode.Symbology.M25;
import static com.honeywell.barcode.Symbology.MICROPDF;
import static com.honeywell.barcode.Symbology.MSI;
import static com.honeywell.barcode.Symbology.OCR;
import static com.honeywell.barcode.Symbology.PDF417;
import static com.honeywell.barcode.Symbology.QR;
import static com.honeywell.barcode.Symbology.RSS_14;
import static com.honeywell.barcode.Symbology.RSS_EXPANDED;
import static com.honeywell.barcode.Symbology.RSS_LIMITED;
import static com.honeywell.barcode.Symbology.S25;
import static com.honeywell.barcode.Symbology.TELEPEN;
import static com.honeywell.barcode.Symbology.TRIOPTIC;
import static com.honeywell.barcode.Symbology.UPCA;
import static com.honeywell.barcode.Symbology.UPCA_2CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.UPCA_5CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.UPCE_2CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.UPCE_5CHAR_ADDENDA;
import static com.honeywell.barcode.Symbology.UPCE_EXPAND;

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
            //activate the API with your license key   trial-speed-tjian-03162017  trial-testa-tjian-07282017   trial-speed-tjian-08072017
            ActivationResult activationResult = ActivationManager.activate(context, "trial-testa-tjian-07282017");
            Toast.makeText(context, "Activation Result: " + activationResult, Toast.LENGTH_LONG).show();
            //get the singleton instance of the decoder
            hsmDecoder = HSMDecoder.getInstance(context);
            initEnableDecode();
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

    //初始化时全使能条码类型
    private void initEnableDecode() {

        hsmDecoder.enableSymbology(UPCA);
        hsmDecoder.enableSymbology(UPCA_2CHAR_ADDENDA);
        hsmDecoder.enableSymbology(UPCA_5CHAR_ADDENDA);
        hsmDecoder.enableSymbology(UPCE_EXPAND);
        hsmDecoder.enableSymbology(UPCE_2CHAR_ADDENDA);
        hsmDecoder.enableSymbology(UPCE_5CHAR_ADDENDA);
        hsmDecoder.enableSymbology(EAN8);
        hsmDecoder.enableSymbology(EAN8_2CHAR_ADDENDA);
        hsmDecoder.enableSymbology(EAN8_5CHAR_ADDENDA);

        hsmDecoder.enableSymbology(EAN13);
        hsmDecoder.enableSymbology(EAN13_2CHAR_ADDENDA);
        hsmDecoder.enableSymbology(EAN13_5CHAR_ADDENDA);
        hsmDecoder.enableSymbology(EAN13_ISBN);

        hsmDecoder.enableSymbology(CODE128);
        hsmDecoder.enableSymbology(GS1_128);
        hsmDecoder.enableSymbology(C128_ISBT);
        hsmDecoder.enableSymbology(CODE39);
        hsmDecoder.enableSymbology(COUPON_CODE);
        hsmDecoder.enableSymbology(TRIOPTIC);
        hsmDecoder.enableSymbology(I25);
        hsmDecoder.enableSymbology(S25);
        hsmDecoder.enableSymbology(IATA25);
        hsmDecoder.enableSymbology(M25);
        hsmDecoder.enableSymbology(CODE93);
        hsmDecoder.enableSymbology(CODE11);
        hsmDecoder.enableSymbology(CODABAR);
        hsmDecoder.enableSymbology(TELEPEN);
        hsmDecoder.enableSymbology(MSI);
        hsmDecoder.enableSymbology(RSS_14);
        hsmDecoder.enableSymbology(RSS_LIMITED);
        hsmDecoder.enableSymbology(RSS_EXPANDED);
        hsmDecoder.enableSymbology(CODABLOCK_F);
        hsmDecoder.enableSymbology(PDF417);
        hsmDecoder.enableSymbology(MICROPDF);
        hsmDecoder.enableSymbology(COMPOSITE);
        hsmDecoder.enableSymbology(COMPOSITE_WITH_UPC);
        hsmDecoder.enableSymbology(AZTEC);
        hsmDecoder.enableSymbology(DATAMATRIX);
        hsmDecoder.enableSymbology(DATAMATRIX_RECTANGLE);
        hsmDecoder.enableSymbology(QR);
        hsmDecoder.enableSymbology(HANXIN);
        hsmDecoder.enableSymbology(HK25);
        hsmDecoder.enableSymbology(KOREA_POST);
        hsmDecoder.enableSymbology(OCR);
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
            hsmDecoder.enableSound(false);
            String decodeDate = null;

            if (isUTF8(firstResult.getBarcodeDataBytes())) {
                try {
                    decodeDate = new String(firstResult.getBarcodeDataBytes(),
                            "utf8");
                    if (decodeDate.equals(isdecodeDate)) {
                        return;
                    } else {
                        hsmDecoder.enableSound(true);
                        isdecodeDate = decodeDate;
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
