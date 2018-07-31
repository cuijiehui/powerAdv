package com.coresun.powerbank.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Android
 * @date 2018/7/12
 * @details 用于rx通信实体类
 */

public class MsgResult implements Parcelable {
    public final static int NET_WORK_STATE_TYPE = 0;//网络状态
    public final static int SOCKET_UTILS_TYPE = 1;//短链接socket信息
    private int TYPE;//类型
    private String Msg;//信息
    private Object content;//传递的数据

    public MsgResult(int TYPE, String msg, Object content) {
        this.TYPE = TYPE;
        Msg = msg;
        this.content = content;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    protected MsgResult(Parcel in) {
        TYPE = in.readInt();
        Msg = in.readString();
    }

    public static final Creator<MsgResult> CREATOR = new Creator<MsgResult>() {
        @Override
        public MsgResult createFromParcel(Parcel in) {
            return new MsgResult(in);
        }

        @Override
        public MsgResult[] newArray(int size) {
            return new MsgResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(TYPE);
        dest.writeString(Msg);
    }
}
