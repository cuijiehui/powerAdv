package com.coresun.powerbank.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coresun.powerbank.R;
import com.coresun.powerbank.base.AdvBaseFragment;
import com.coresun.powerbank.entity.AdvBean;
import com.coresun.powerbank.widget.EmptyControlVideo;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

/**
 * Created by Administrator on 2018/3/28/028.
 */

public class VideoPlayFragment extends AdvBaseFragment {

    EmptyControlVideo empty_control_video;
   private String URL="";
    private VideoAllCallBack mCallBack;
    AdvBean mAdvBean;

    public VideoPlayFragment() {
    }

    @SuppressLint("ValidFragment")
    public VideoPlayFragment(AdvBean advBean, VideoAllCallBack mCallBack) {
        this.mAdvBean = advBean;
        this.mCallBack = mCallBack;
        setTYPE(advBean.getType());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_play_fragment,null);
        empty_control_video=(EmptyControlVideo) view.findViewById(R.id.empty_control_video);
        setVideoPlay();
        return view;
    }

    /**
     * 设置视频数据
      */
    private void setVideoPlay(){
        empty_control_video.setUp(mAdvBean.getPath(),true,null);
        empty_control_video.setVideoAllCallBack(mCallBack);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(empty_control_video!=null){
            empty_control_video.startPlayLogic();//开始视频
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(empty_control_video!=null){
            empty_control_video.onVideoPause();//暂停视频
        }
    }
}
