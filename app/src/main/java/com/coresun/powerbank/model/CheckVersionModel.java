package com.coresun.powerbank.model;

import com.coresun.powerbank.base.BaseBean;
import com.coresun.powerbank.base.BaseModel;
import com.coresun.powerbank.base.BaseObserver;
import com.coresun.powerbank.common.MyApplication;
import com.coresun.powerbank.entity.VersionBean;
import com.coresun.powerbank.inter.CallBack;
import com.coresun.powerbank.network.RetrofitFactory;

import okhttp3.RequestBody;

/**
 * @author Android
 * @date 2018/7/13
 * @details
 */

public class CheckVersionModel extends BaseModel<BaseBean<VersionBean>> {
    @Override
    public void execute(final CallBack<BaseBean<VersionBean>> callback) {
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),mParams[0]);
        RetrofitFactory.getInstence().API()
                .getCheckUpdate(body)
                .compose(MyApplication.getInstance().<BaseBean<VersionBean>>setThread())
                .subscribe(new BaseObserver<VersionBean>() {
                    @Override
                    protected void onSuccees(BaseBean<VersionBean> t) throws Exception {
                        callback.onSuccess(t);
                    }

                    @Override
                    protected void onCodeError(BaseBean<VersionBean> t) throws Exception {
                        callback.onFailure(t.getMsg());
                    }

                    @Override
                    protected void onError(Throwable e, boolean isNetWorkError) throws Exception {
                        callback.onError();
                    }
                });
    }
}
