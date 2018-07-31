package com.coresun.powerbank.base;

import com.coresun.powerbank.inter.CallBack;

import java.util.Map;

import io.reactivex.ObservableTransformer;

public abstract class BaseModel<T> {
    //数据请求参数
    protected String[] mParams;
    //所在的线程
    private ObservableTransformer transformer;
    /**
     * 设置数据请求参数
     * @param args 参数数组
     */
    public  BaseModel params(String... args){
        mParams = args;
        return this;
    }
    // 添加Callback并执行数据请求
    // 具体的数据请求由子类实现
    public abstract void execute(CallBack<T> callback);
    // 执行Get网络请求，此类看需求由自己选择写与不写
    protected void requestGetAPI(String url,CallBack<T> callback){
        //这里写具体的网络请求
    }
    // 执行Post网络请求，此类看需求由自己选择写与不写
    protected void requestPostAPI(String url, Map params, CallBack<T> callback){
        //这里写具体的网络请求
    }

    public ObservableTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(ObservableTransformer transformer) {
        this.transformer = transformer;
    }
}
