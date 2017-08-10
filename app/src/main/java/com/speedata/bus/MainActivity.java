package com.speedata.bus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.speedata.bus.db.DbCommon;
import com.speedata.bus.utils.Myeventbus;
import com.speedata.bus.utils.ScanDecode;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //    String content = "RfQGDEXENzXDv2sHQ1PbwhX2tinz0YXLCiGySQ0sqjb9Hyw7f/Z9ZVSWYRRYydYK/wTVatTz6j3YmnfF7t1P68hcrtw+RWEmdoHHznhLvf1Pzm4X/bUJrqh6FlCC7A1fW5IGD987HCqHpk3Zn5/xPP3mDT747Lx0tX9txS6XMbBKWBe8vAR530f0Bgw=";
    String content = "";
    TextView textView;
    ScanDecode scanDecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        org.greenrobot.eventbus.EventBus.getDefault().register(this);

        scanDecode = new ScanDecode(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        textView = findViewById(R.id.tvmsg);
        scanDecode.startScan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDecodeMsG(Myeventbus myeventbus) {
        content = myeventbus.getDecodeMsg();
        if (!TextUtils.isEmpty(content)) {
            textView.setText(content);
            DbCommon.saveInDb(content);
//            DbCommon.getPostData();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_test) {
            DbCommon.saveInDb(content);
        } else if (view.getId() == R.id.start) {
            scanDecode.startScan();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
        scanDecode.DestroyScan();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
