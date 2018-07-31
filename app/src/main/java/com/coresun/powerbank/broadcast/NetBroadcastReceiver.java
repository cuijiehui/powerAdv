package com.coresun.powerbank.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.coresun.powerbank.entity.MsgResult;
import com.coresun.powerbank.utils.NetWorkStateUtils;
import com.coresun.powerbank.utils.RxBus;

/**
 * @author Android
 * @date 2018/7/12
 * @details 广播监听网络状态
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            // 如果相等的话就说明网络状态发生了变化
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 接口回调传过去状态的类型
                boolean isConnected = NetWorkStateUtils.getInstance().isNetworkConnected(context);
                MsgResult msgResult = new MsgResult(MsgResult.NET_WORK_STATE_TYPE,"NetBroadcastReceiver",isConnected);
                RxBus.getInstance().post(msgResult);
            }

    }


}


