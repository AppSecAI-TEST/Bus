package com.speedata.bus.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by lenovo-pc on 2017/8/10.
 */

public class PlaySound {

//    private static Map<Integer, Integer> mapSRC;
//    private static SoundPool sp; //声音池

    public static void playerSound(Context context, int source) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, source);
        mediaPlayer.start();
    }

    //初始化声音池
//    public static void initSoundPool(Context context) {
//        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
//        mapSRC = new HashMap<>();
//        mapSRC.put(1, sp.load(context, R.raw.error, 0));
//        mapSRC.put(2, sp.load(context, R.raw.welcome, 0));
//        mapSRC.put(3, sp.load(context, R.raw.msg, 0));
//        mapSRC.put(4, sp.load(context, R.raw.flash, 0));
//        mapSRC.put(5, sp.load(context, R.raw.wrong, 0));
//        mapSRC.put(6, sp.load(context, R.raw.success, 0));
//    }

//    /**
//     * 播放声音池的声音
//     */
//    public static void play(int sound, int number) {
////        initSoundPool(context);
//
//        sp.play(mapSRC.get(sound),//播放的声音资源
//                1.0f,//左声道，范围为0--1.0
//                1.0f,//右声道，范围为0--1.0
//                0, //优先级，0为最低优先级
//                number,//循环次数,0为不循环
//                0);//播放速率，0为正常速率
//    }
}
