package com.coresun.powerbank.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.coresun.powerbank.MainActivity;
import com.coresun.powerbank.common.Constants;
import com.coresun.powerbank.presenter.MainPresenter;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 心跳服务，每5分钟访问一次后台是否有广告更新。
 * Created by Android on 2017/9/12.
 */

public class HeartBeatService extends Service  {

    private static final int cycleTime = 1000 * 60 * 10; //10分钟更新一次广告
    boolean is9Update = false, is15Update = false, is20Update = false, is0Update = false;

    private Timer timer;
    private TimerTask timerTask;
    MainPresenter mainPresenter;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendHeartBeat();
        return super.onStartCommand(intent, flags, startId);
    }
    public void setPresenter(MainPresenter presenter){
        mainPresenter=presenter;
    }
    private void sendHeartBeat() {

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                switch (hour) {
                    case 9:
                        if (!is9Update) {
                            getAdvData();
                            is9Update = true;
                            is15Update = false;
                            is20Update = false;
                            is0Update = false;
                        }
                        break;
                    case 15:
                        if (!is15Update) {
                            getAdvData();
                            is9Update = false;
                            is15Update = true;
                            is20Update = false;
                            is0Update = false;
                        }
                        break;
                    case 20:
                        if (!is20Update) {
                            getAdvData();
                            is9Update = false;
                            is15Update = false;
                            is20Update = true;
                            is0Update = false;
                        }
                        break;
                    case 0:
                        if (!is0Update) {
                            getAdvData();
                            is9Update = false;
                            is15Update = false;
                            is20Update = false;
                            is0Update = true;
                        }
                        break;
                }
//                getAdvData();
            }
        };

        timer.schedule(timerTask, cycleTime, cycleTime);

    }

    private void getAdvData() {
        if(MainActivity.getPresenter()!=null){
            MainActivity.getPresenter().advData(Constants.DEVICE_ID);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerTask != null && timer != null) {
            timerTask.cancel();
            timerTask = null;
            timer.cancel();
            timer = null;
        }
    }

}
