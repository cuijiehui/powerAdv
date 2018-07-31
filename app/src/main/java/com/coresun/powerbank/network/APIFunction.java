package com.coresun.powerbank.network;

import com.coresun.powerbank.base.BaseBean;
import com.coresun.powerbank.entity.AdvDataBean;
import com.coresun.powerbank.entity.VersionBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author CUI
 * @data 2018/5/16.
 * @description 网络请求类
 */
public interface APIFunction {

    @POST("api/Ladder/getSowing")
    Observable<BaseBean<AdvDataBean>> getAdvDataCall(@Body RequestBody body);

    @POST("api/Equip/setEquipIp")
    Observable<BaseBean<Object>> getInitDataCall(@Body RequestBody body);

    @POST("api/Socket/checkPower")
    Observable<BaseBean<Object>> getCheckPowerCall(@Body RequestBody body);

    @POST("api/Socket/startTime")
    Observable<BaseBean<Object>> getStartTime(@Body RequestBody body);
    @POST("api/Version/getVersion")
    Observable<BaseBean<VersionBean>> getCheckUpdate(@Body RequestBody body);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}
