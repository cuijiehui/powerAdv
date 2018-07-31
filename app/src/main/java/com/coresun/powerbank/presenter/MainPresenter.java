package com.coresun.powerbank.presenter;

import com.coresun.powerbank.MainActivity;
import com.coresun.powerbank.base.BaseBean;
import com.coresun.powerbank.base.BasePresenter;
import com.coresun.powerbank.entity.AdvDataBean;
import com.coresun.powerbank.entity.VersionBean;
import com.coresun.powerbank.inter.CallBack;
import com.coresun.powerbank.model.DataModel;
import com.coresun.powerbank.model.Token;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * @author Android
 * @date 2018/7/13
 * @details
 */

public class MainPresenter extends BasePresenter<MainActivity> {
    public void initData(String number,String ip){
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number",number);
        jsonObject.addProperty("ip",ip);

        String json = jsonObject.toString();
        Logger.i("json=()" + json);
        DataModel.request(Token.API_INIT_DATA_MODEL)
                .params(json)
                .execute(new CallBack<BaseBean<Object>>() {
                    @Override
                    public void onSuccess(BaseBean<Object> data) {
                        getView().getInitData();
                    }

                    @Override
                    public void onFailure(String msg) {
                        getView().onFailure(msg);
                    }

                    @Override
                    public void onError() {
                        getView().onError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void startTime(String out_trade_no ,String time ,String type){
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("out_trade_no",out_trade_no);
        jsonObject.addProperty("time",time);
        jsonObject.addProperty("type",type);
        String json = jsonObject.toString();
        Logger.i("json=()" + json);
        DataModel.request(Token.API_START_TIME_MODEL)
                .params(json)
                .execute(new CallBack<BaseBean<Object>>() {
                    @Override
                    public void onSuccess(BaseBean<Object> data) {
                        getView().startTime();
                    }

                    @Override
                    public void onFailure(String msg) {
                        getView().onFailure(msg);
                    }

                    @Override
                    public void onError() {
                        getView().onError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void checkPower(String number ,int electricity){
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number",number);
        jsonObject.addProperty("electricity",electricity);
        String json = jsonObject.toString();
        Logger.i("json=()" + json);
        DataModel.request(Token.API_CHECK_POWER_MODEL)
                .params(json)
                .execute(new CallBack<BaseBean<Object>>() {
                    @Override
                    public void onSuccess(BaseBean<Object> data) {
                        getView().checkPower();
                    }

                    @Override
                    public void onFailure(String msg) {
                        getView().onFailure(msg);
                    }

                    @Override
                    public void onError() {
                        getView().onError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void advData(String number){
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number",number);
        String json = jsonObject.toString();
        Logger.i("json=()" + json);
        DataModel.request(Token.API_ADV_DATA_MODEL)
                .params(json)
                .execute(new CallBack<BaseBean<AdvDataBean>>() {
                    @Override
                    public void onSuccess(BaseBean<AdvDataBean> data) {
                        getView().advData(data.getData());
                    }

                    @Override
                    public void onFailure(String msg) {
                        getView().onFailure(msg);
                    }

                    @Override
                    public void onError() {
                        getView().onError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void checkVersion(String number){
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number",number);
        String json = jsonObject.toString();
        Logger.i("json=()" + json);
        DataModel.request(Token.API_CHECK_VERSION_MODEL)
                .params(json)
                .execute(new CallBack<BaseBean<VersionBean>>() {
                    @Override
                    public void onSuccess(BaseBean<VersionBean> data) {
                        getView().checkVersion(data.getData());
                    }

                    @Override
                    public void onFailure(String msg) {
                        getView().onFailure(msg);
                    }

                    @Override
                    public void onError() {
                        getView().onError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
