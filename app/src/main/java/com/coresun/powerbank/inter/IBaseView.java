package com.coresun.powerbank.inter;

import android.content.Context;

public interface IBaseView {
    /**
     * 显示正在加载view
     */
    void showLoading();
    /**
     * 关闭正在加载view
     */
    void hideLoading();
    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);
    /**
     * 显示请求错误提示
     */
    void showErr();
    /**
     * 获取上下文
     * @return 上下文
     */
    Context getContext();
    /**
     * 访问网络失败
     * @param msg
     */
      void onFailure(String msg) ;

    /**
     * 访问网络异常
     */
      void onError() ;

}
