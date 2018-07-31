package com.coresun.powerbank;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.coresun.powerbank.base.AdvBaseFragment;
import com.coresun.powerbank.base.BaseActivtity;
import com.coresun.powerbank.base.BasePresenter;
import com.coresun.powerbank.common.Constants;
import com.coresun.powerbank.entity.AdvBean;
import com.coresun.powerbank.entity.AdvDataBean;
import com.coresun.powerbank.entity.AdvTxt;
import com.coresun.powerbank.entity.MsgResult;
import com.coresun.powerbank.entity.SocketInfoBean;
import com.coresun.powerbank.entity.SocketPowerBean;
import com.coresun.powerbank.entity.VersionBean;
import com.coresun.powerbank.entity.WebSocketBackBean;
import com.coresun.powerbank.frament.ImageViewFragment;
import com.coresun.powerbank.frament.VideoPlayFragment;
import com.coresun.powerbank.network.MyWebSocket;
import com.coresun.powerbank.network.MyWebSocketListener;
import com.coresun.powerbank.network.RetrofitFactory;
import com.coresun.powerbank.network.WebSocketUtils;
import com.coresun.powerbank.presenter.MainPresenter;
import com.coresun.powerbank.service.HeartBeatService;
import com.coresun.powerbank.utils.FileUtils;
import com.coresun.powerbank.utils.RxBus;
import com.coresun.powerbank.utils.SharedPreferencesUtils;
import com.coresun.powerbank.utils.ToastUtils;
import com.coresun.powerbank.widget.CustomViewPager;
import com.coresun.powerbank.widget.NoPreloadViewPager;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivtity {

    @BindView(R.id.adv_viewpager)
    CustomViewPager advViewpager;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    private Observable<MsgResult> observable;
    private Context mContext;
    private MyWebSocket myWebSocket;
    ArrayList<AdvTxt> mAdvTxts = new ArrayList<>();
    LinkedList<AdvTxt> showAdvTxts = new LinkedList<>();
    private List<AdvBaseFragment> advDataList = new ArrayList<>();
    private static MainPresenter mainPresenter = new MainPresenter();
    private int viewPagerIndex; //ViewPager的下标值
    public Handler mHandler;
    MyBaseAdapter myBaseAdapter;
    SharedPreferencesUtils mSharedPreferencesUtils;
    public long mTime = -1;//充电时间
    String out_trade_no = "";

    @Override
    public BasePresenter initPresenter() {
        return mainPresenter;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        registerRxBus();
    }

    @Override
    public void doBusiness(Context mContext) {
        mContext = this;
        mHandler = new Handler();
        mSharedPreferencesUtils = new SharedPreferencesUtils(mContext);
        String sdcardPath = System.getenv("EXTERNAL_STORAGE");
        Constants.APK_PATH = sdcardPath + File.separator + "Download" +File.separator+ "powerbank.apk";
                               //接收RxBus发送的事件
//        if (NetWorkStateUtils.getInstance().isNetworkConnected(mContext)) {
            initData();
//        } else {
//            Logger.w("网络不可用！");
//        }
//        startWebSocket();
//        ToastUtils.showShort(this,"版本号1.0.1");
    }

    @Override
    public View initBack() {
        return null;
    }


    /**
     * 初始化数据 首先去获取到唯一码
     */
    private void initData() {
        Logger.e("initData");
        getDeviceInfo(WebSocketUtils.MSG_TYPE_INFO);//获得充电宝信息
    }

    /**
     * 获取充电宝的信息
     *
     * @param type 是获取唯一码还是获取电量 操作不一样所以分开
     */
    private void getDeviceInfo(int type) {
        String content = "{\"MsgType\":\"GetSystemInfo\"}";
        WebSocketUtils.startSocket("127.0.0.1", 6677, content, type);
    }

    /**
     * 打开电源开关
     *
     * @param ctlType 1 = 开 2 =关
     */
    private void startPowerSwitch(int ctlType) {
        //"{\"MsgType\":\"ChargerCtl\",\"CtlType\":\"Enable\"}"
        String content = "";
        if (ctlType == WebSocketUtils.MSG_TYPE_SWITCH_NO) {
            content = "{\"MsgType\":\"ChargerCtl\",\"CtlType\":\"Enable\"}";
        } else if (ctlType == WebSocketUtils.MSG_TYPE_SWITCH_OFF) {
            content = "{\"MsgType\":\"ChargerCtl\",\"CtlType\":\"Disable\"}";
        }
        Logger.i("socket测试=" + content);
        WebSocketUtils.startSocket("127.0.0.1", 6677, content, ctlType);
    }

    /**
     * 接收RxBus发送的事件
     */
    private void registerRxBus() {
        observable = RxBus.getInstance().register(MsgResult.class);
        observable.subscribeOn(Schedulers.single()).subscribe(new Consumer<MsgResult>() {
            @Override
            public void accept(MsgResult advResult) throws Exception {
                Logger.e("subscribeOn="+advResult.getTYPE());
                switch (advResult.getTYPE()) {
                    case MsgResult.NET_WORK_STATE_TYPE://网络改变
                        boolean isConnet = (boolean) advResult.getContent();
                        if (isConnet) {
                            initData();
                        }
                        break;
                    case MsgResult.SOCKET_UTILS_TYPE://短socket信息
                        String socketMsg = (String) advResult.getContent();
                        int socketType = new Integer(advResult.getMsg());
                        Logger.e("SOCKET_UTILS_TYPE="+socketType);
                        if (!TextUtils.isEmpty(socketMsg)) {
                            Gson gson = new Gson();
                            switch (socketType) {
                                case WebSocketUtils.MSG_TYPE_INFO: //获取充电宝信息
                                    SocketInfoBean socketInfoBean = gson.fromJson(socketMsg, SocketInfoBean.class);
                                    Logger.e("充电宝信息：" + socketInfoBean.toString());
                                    Constants.DEVICE_ID = socketInfoBean.getIMEI1();
                                    startWebSocket();
                                    break;
                                case WebSocketUtils.MSG_TYPE_POWER: //获取充电宝电量
                                    SocketInfoBean socketPowerBean = gson.fromJson(socketMsg, SocketInfoBean.class);
                                    mainPresenter.checkPower(Constants.DEVICE_ID, socketPowerBean.getBatCapacity());
                                    break;
                                case WebSocketUtils.MSG_TYPE_SWITCH_NO:
                                    Logger.i("socket测试" + socketMsg);
                                    SocketPowerBean socketCallBean = gson.fromJson(socketMsg, SocketPowerBean.class);
                                    if (socketCallBean.getErrorCode() == 0000) {
                                        mHandler.postDelayed(OFFrunnable, mTime * 1000);
                                        String time = System.currentTimeMillis() + "";
                                        String sTime = time.toString().substring(0, time.toCharArray().length - 3);
                                        Logger.i("socket测试：" + sTime + "=time...." + "time=" + time);
                                        mainPresenter.startTime(out_trade_no, sTime, "1");
                                        Constants.isSwitchNo = true;
                                    } else {
                                        String time = System.currentTimeMillis() + "";
                                        String sTime = time.toString().substring(0, time.toCharArray().length - 3);
                                        Logger.i("socket测试：" + sTime + "=time...." + "time=" + time);
                                        mainPresenter.startTime(out_trade_no, sTime, "2");
                                    }
                                    break;
                                case WebSocketUtils.MSG_TYPE_SWITCH_OFF:
                                    SocketPowerBean socketCallBean2 = gson.fromJson(socketMsg, SocketPowerBean.class);
                                    if (socketCallBean2.getErrorCode() == 0000) {
                                        Constants.isSwitchNo = false;
                                    }
                                    break;


                            }
                        } else {

                        }
                        break;

                }
            }
        });
        Logger.e("registerRxBus");

    }

    /**
     * 启动socket链接服务器
     */
    private void startWebSocket() {
        myWebSocket = MyWebSocket.getInstance();
        myWebSocket.init("120.76.77.241", 2345, this, myWebSocketListener);
        myWebSocket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(observable!=null){
            RxBus.getInstance().unregister(MsgResult.class,observable);
        }
    }

    private MyWebSocketListener myWebSocketListener = new MyWebSocketListener() {
        @Override
        public void onOpen(String response) {
            Logger.e("onOpen=" + response);

        }

        @Override
        public void onMessage(String message) {
            Gson gson = new Gson();
            WebSocketBackBean webSocketBackBean = gson.fromJson(message, WebSocketBackBean.class);
            Logger.e("message=" + message);
            switch (webSocketBackBean.getType()) {
                case 1:
                    Constants.DEVICE_SOCKET_ID = webSocketBackBean.getId();
                    mainPresenter.initData(Constants.DEVICE_ID, Constants.DEVICE_SOCKET_ID);
                    break;
                case 2:
                    getDeviceInfo(WebSocketUtils.MSG_TYPE_POWER);//获取充电宝电量
                    break;
                case 3:
                    mTime = webSocketBackBean.getTime();
                    out_trade_no = webSocketBackBean.getOut_trade_no();
                    startPowerSwitch(WebSocketUtils.MSG_TYPE_SWITCH_NO);
                    break;
            }

        }

        @Override
        public void onFailure(String response) {
            Logger.e("onOpen=" + response);
            if (myWebSocket != null) {
                myWebSocket.reStartSockt();
            }
        }
    };

    public static MainPresenter getPresenter() {
        return mainPresenter;
    }

    /**
     * 初始化viewpage
     */
    private void initViewPager() {
        myBaseAdapter = new MyBaseAdapter(getSupportFragmentManager(), advDataList);
        advViewpager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AdvBaseFragment fragment1 = advDataList.get(position);
                int time = mAdvTxts.get(position).getTime();
                Logger.i("接口测试：图片时间=" + time);
                if (fragment1.getTYPE() == 1) {
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            scrollAdvViewPager();
//                        }
//                    }, time * 1000);
                    delayScroll(time);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        advViewpager.setAdapter(myBaseAdapter);
    }

    private void delayScroll(int time) {
        mHandler.postDelayed(runnable, time * 1000);
    }

    class MyBaseAdapter extends FragmentPagerAdapter {
        List<AdvBaseFragment> mDataList = new ArrayList<>();

        public MyBaseAdapter(FragmentManager fm, List<AdvBaseFragment> dataList) {
            super(fm);
            mDataList = dataList;
        }

        @Override
        public Fragment getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }
    }

    private void sendHeartBeat() {
        Intent heartBeatService = new Intent(this, HeartBeatService.class);
        startService(heartBeatService);
    }

    public void getInitData() {
        Logger.i("getInitData=");
        mainPresenter.advData(Constants.DEVICE_ID);
        mainPresenter.checkVersion(Constants.DEVICE_ID);
        sendHeartBeat();                            //开始定时获取广告服务
    }

    public void startTime() {

    }

    public void checkPower() {

    }

    public void advData(AdvDataBean advDataBean) {
        String strs = advDataBean.getUrl();
        Logger.i("advData=" + strs);
        if (strs != null) {
            dowAdvData(strs);//去下载播放列表
        }
    }

    private void dowAdvData(String strs) {

        File folder = new File(FileUtils.getVideoPath());
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }
        Logger.e("FileUtils.getVideoPath=" + FileUtils.getVideoPath());
        final AdvTxt advTxt = new AdvTxt();
        advTxt.setFilename(FileUtils.getFileName(strs));
        advTxt.setUrl(strs);
        if (!TextUtils.isEmpty(strs)) {
            Constants.ADV_DATA_URL = strs;
            RetrofitFactory.getInstence().API()
                    .downloadFile(strs)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<ResponseBody, AdvTxt>() {
                        @Override
                        public AdvTxt apply(ResponseBody responseBody) throws Exception {
                            return FileUtils.writeResponseBodyToDisk(responseBody, advTxt);
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<AdvTxt>() {
                        @Override
                        public void accept(AdvTxt advTxt) throws Exception {
                            if (TextUtils.isEmpty(advTxt.getPath())) {
                                mainPresenter.advData(Constants.DEVICE_ID);
                            } else {
                                Constants.ADV_DATA_PATH = advTxt.getPath();
                                dowAdvFile();
                            }
                        }
                    });
        }else{
            mainPresenter.advData(Constants.DEVICE_ID);

        }

    }

    private void dowAdvFile() {
        if (!TextUtils.isEmpty(Constants.ADV_DATA_PATH)) {
            File file = new File(Constants.ADV_DATA_PATH);
            if (file.exists()) {
                mAdvTxts = FileUtils.refreshData(file);
            }
            List<Observable<AdvTxt>> observables = new ArrayList<>();
            showAdvTxts.clear();
            if (mAdvTxts != null) {
                showAdvTxts.addAll(mAdvTxts);
                int i =0;
                for (final AdvTxt advTxts : mAdvTxts) {
                    Logger.i("advTxtsname=" + advTxts.getFilename());
                    String path = FileUtils.getVideoPath() + advTxts.getFilename();
                    File advFile = new File(path);
                    advTxts.setSequence(i);
                    i++;
                    if (advFile.exists() && FileUtils.fileIsEquals(advFile, advTxts.getSize())) {
                        AdvTxt advTxt =  showAdvTxts.get(mAdvTxts.indexOf(advTxts));
                        advTxt.setPath(path);
                        Logger.e("i="+i);
                        Logger.e("mAdvTxts="+mAdvTxts.size());
                        if(i==mAdvTxts.size()){
                            showData();
                        }
                        continue;
                    }
                    Logger.i("advTxts.getUrl()=" + advTxts.getUrl());

                    observables.add(RetrofitFactory.getInstence().API()
                            .downloadFile(advTxts.getUrl())
                            .subscribeOn(Schedulers.io())
                            .map(new Function<ResponseBody, AdvTxt>() {
                                @Override
                                public AdvTxt apply(ResponseBody responseBody) throws Exception {
                                    return FileUtils.writeResponseBodyToDisk(responseBody, advTxts);
                                }
                            }).subscribeOn(Schedulers.io()));
                }
                Observable.merge(observables).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<AdvTxt>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Logger.i("onSubscribe");
                            }

                            @Override
                            public void onNext(AdvTxt s) {
                                Logger.e("onNext=" + s.getPath());
                                AdvTxt advTxt =  showAdvTxts.get(mAdvTxts.indexOf(s));
                                advTxt.setPath(s.getPath());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError=");

                            }

                            @Override
                            public void onComplete() {
                                Logger.e("onComplete");
                                showData();
                            }
                        });

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        showData();
    }

    private void showData() {
        if(showAdvTxts.size()>0){
            advDataList.clear();
            for (AdvTxt advTxt : showAdvTxts) {
                AdvBean advBean = new AdvBean();
                advBean.setId(advTxt.getId());
                advBean.setAdvName(advTxt.getFilename());
                advBean.setType(advTxt.getType());
                advBean.setUrl(advTxt.getUrl());
                advBean.setPath(advTxt.getPath());
                if (advTxt.getType() == 1) {
                    //图片
                    Logger.i("展示测试：图片" + advTxt.toString());
                    ImageViewFragment imageViewFragment = new ImageViewFragment(advBean);
                    advDataList.add(imageViewFragment);
                } else if (advTxt.getType() == 2) {
                    //视频
                    Logger.i("展示测试：视频" + advTxt.toString());
                    VideoPlayFragment fragment =
                            new VideoPlayFragment(advBean, gsySampleCallBack);
                    advDataList.add(fragment);
                }
            }
            mHandler.removeCallbacks(runnable);//防止多个计时器在运行
            if (myBaseAdapter == null) {
                initViewPager();
            } else {
                myBaseAdapter.notifyDataSetChanged();
            }
            ivSplash.setVisibility(View.GONE);
            if (advDataList.get(0).getTYPE() == 1 && advDataList.size() > 1) {
                int time = mAdvTxts.get(0).getTime();
                delayScroll(time);
            }
//        mSharedPreferencesUtils.put(Constants.SHARE_KEY_ADV_CATALOG_NAME, advTxt.getPath());
            String oldAdv = (String) mSharedPreferencesUtils.getSharedPreference(Constants.SHARE_KEY_ADV_CATALOG_NAME, SharedPreferencesUtils.SHARE_STRING);
            if (TextUtils.isEmpty(oldAdv)) {
                mSharedPreferencesUtils.put(Constants.SHARE_KEY_ADV_CATALOG_NAME, Constants.ADV_DATA_PATH);
            } else {
                FileUtils.deleteAdvFile(oldAdv, Constants.ADV_DATA_PATH);
                if (!Constants.ADV_DATA_PATH.equals(oldAdv)) {
                    FileUtils.deleteFile(oldAdv);
                }
                mSharedPreferencesUtils.put(Constants.SHARE_KEY_ADV_CATALOG_NAME, Constants.ADV_DATA_PATH);
            }
        }

    }

    private GSYSampleCallBack gsySampleCallBack = new GSYSampleCallBack() {
        @Override
        public void onAutoComplete(String url, Object... objects) {
            scrollAdvViewPager();
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            scrollAdvViewPager();
        }
    };
    Runnable OFFrunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            mTime = -1;
            startPowerSwitch(WebSocketUtils.MSG_TYPE_SWITCH_OFF);
        }
    };

    private void scrollAdvViewPager() {
        viewPagerIndex = advViewpager.getCurrentItem();
        if (viewPagerIndex == advDataList.size() - 1) {
            viewPagerIndex = 0;
        } else {
            viewPagerIndex += 1;
        }
        advViewpager.setCurrentItem(viewPagerIndex);
    }

    public void checkVersion(VersionBean versionBean) {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            int oldCode = info.versionCode;
            Logger.i("接口测试= updategetCode=" + versionBean.getCode());
            Logger.i("接口测试= updategetCode=" + oldCode);
            Constants.APK_CODE=oldCode+"";
            if (oldCode < versionBean.getCode()) {
                    RetrofitFactory.getInstence().API()
                            .downloadFile(versionBean.getUrl())
                            .subscribeOn(Schedulers.io())
                            .map(new Function<ResponseBody, Boolean>() {
                                @Override
                                public Boolean apply(ResponseBody responseBody) throws Exception {
                                    boolean isOk =false;
                                    try{
                                        isOk=  FileUtils.weiteFile(responseBody, Constants.APK_PATH);
                                    }catch (Exception e){
                                        Logger.e("exception="+e.getMessage());
                                        Logger.e("exception="+Constants.APK_PATH);
                                    }

                                        return isOk;
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean isOk) throws Exception {
                                    if(isOk){
                                        if(!Constants.isSwitchNo){
                                            Logger.e("pakager="+Constants.APK_PATH);
                                            Intent intent = new Intent();
                                            intent.setAction("com.hilan.updater");
                                            intent.putExtra("from", getPackageName());
                                            intent.putExtra("path", Constants.APK_PATH);
                                            intent.putExtra("class_from", "com.coresun.powerbank.MainActivity");
                                            sendBroadcast(intent);
                                        }

                                    }

                                }
                            });

            }

        }
    }

    @Override
    public void onError() {
        showToast("网络异常，请稍后重试");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (MainActivity.this){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    });
                }
            }
        }, 3000);

    }


}
