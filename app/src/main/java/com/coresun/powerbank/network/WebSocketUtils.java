package com.coresun.powerbank.network;

import android.os.Handler;
import android.os.Message;

import com.coresun.powerbank.entity.MsgResult;
import com.coresun.powerbank.entity.SocketBean;
import com.coresun.powerbank.utils.RxBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Android
 * @date 2018/7/12
 * @details socket短链接
 */

public class WebSocketUtils {
    private static ScheduledExecutorService mExecutorService = Executors.newScheduledThreadPool(5);
    public static final int MSG_TYPE_INFO=300;//获取充电宝信息
    public static final int MSG_TYPE_SWITCH_NO=301; //开
    public static final int MSG_TYPE_SWITCH_OFF=302; //关
    public static final int MSG_TYPE_POWER=303;//获取充电宝电量
    public synchronized static void startSocket(String host ,int port ,String content ,int type){
        SocketBean socketBean = new SocketBean(host,port,content,type);
        mExecutorService.execute(new connectService(socketBean));
    }

    private static class connectService implements Runnable {
        SocketBean socketBean;

        public connectService(SocketBean socketBean) {
            this.socketBean = socketBean;
        }

        @Override
        public void run() {
            Socket socket=null;
            BufferedReader in=null;
            PrintWriter out=null;
            String getLine=null;
            try {
                socket = new Socket(socketBean.getHOST(), socketBean.getPORT());//连接服务器
                socket.setSoTimeout(60 * 1000);
                in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));//接收消息的流对象
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);//发送消息的流对象
//                    String content = "{\"MsgType\":\"GetSystemInfo\"}";
                out.println(socketBean.getmContent());//点击按钮发送消息

                if ((getLine = in.readLine()) != null) {//读取接收的信息
                    getLine += "\n";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                socket=null;
                in=null;
                out=null;
                MsgResult msgResult =new MsgResult(MsgResult.SOCKET_UTILS_TYPE,socketBean.getmType()+"",getLine);
                RxBus.getInstance().post(msgResult);
            }
        }
    }
}
