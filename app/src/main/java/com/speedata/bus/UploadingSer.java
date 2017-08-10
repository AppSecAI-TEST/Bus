package com.speedata.bus;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

import com.speedata.bus.db.DbCommon;

public class UploadingSer extends Service {
    public UploadingSer() {
    }
//
//    @Override
//    public boolean onStartJob(JobParameters jobParameters) {
//        handler.sendMessage(Message.obtain( handler, 1, jobParameters ));
//        return false;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters jobParameters) {
//        return false;
//    }
//
//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            Toast.makeText( getApplicationContext(),
//                    "JobService task running", Toast.LENGTH_SHORT )
//                    .show();
//            DbCommon.getPostData();
////            jobFinished( (JobParameters) msg.obj, false );
//            return true;
//        }
//    });

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("com.uploding");
        registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert mConnectivityManager != null;
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    //网络连接
                    String name = netInfo.getTypeName();
                    //WiFi网络 有线网络 3g网络
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI || netInfo.getType() == ConnectivityManager.TYPE_ETHERNET
                            || netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                        Toast.makeText(context, "有网络,进行上传数据", Toast.LENGTH_SHORT).show();
                        DbCommon.getPostData();
                    } else {

                        Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
