package com.coresun.powerbank.network.config;

/**
 * @author CUI
 * @data 2018/5/16.
 * @description url类
 */
public interface URLConfig {
    String GET_FULLPAGE_ADS_URL="adlist_large";                                                     //欢迎界面页获取广告
    String POST_LOGIN_URL="/api/Login/doLogin";                                                     //登录
    String POST_REGISTER_URL="/api/Login/register";                                                 //注册
    String POST_GET_TOKEN_URL="/api/System/getServerInfo";                                          //获取token
    String POST_FORGET_PWD_URL="/api/Pwd/setPwd";                                                   //忘记密码
    String POST_REGISTER_CODE_URL="app/index.php?i=1&c=entry&m=ewei_shopv" +
            "2&do=mobile&r=account.apiVerifycode2";                                                 //获取验证码
    String POST_HOME_DATA_URL = "/api/Home/homeData";                                               //获取主页信息
    String POST_CATES_URL = "/api/Charge/getCates";                                                 //缴费类列表
   String POST_CHARGE_CATES_URL="/api/Charge/getChargeCate";                                        //待缴费列表
   String POST_CHARGES_RECORD_URL="/api/Charge/chargeRecord";                                       //缴费记录
    String POSTH_FEEDBACK_URL ="/api/Opinion/submitOpinion";                                        //意见反馈接口
    String POSTH_FEEDBACK_LIST_URL ="/api/Opinion/getOpinionList";                                  //反馈意见列表
    String POSTH_DEL_FEEDBACK_URL ="/api/Opinion/delOpinion";                                       //删除反馈接口
    String POSTH_CITY_LIST_URL ="/api/City/getArea";                                                //获取城市列表接口
    String POSTH_RENT_LIST_URL ="/api/Rent/rentList";                                               //获取租赁信息列表接口
    String POSTH_RENT_DETAIL_URL ="/api/Rent/rentDetail";                                           //租赁信息详情接口
    String POSTH_UPLOAD_IMG_URL ="/api/Home/uploadImg";                                             //图片上传接口
    String POSTH_WX_PAY_URL ="/api/Wxpay/genChargeOrder";                                           //用微信支付统一下单
    String POSTH_ALI_PAY_URL ="api/Alipay/genChargeOrder";                                          //用支付宝支付统一下单
    String POSTH_USER_ENTRAN_URL ="/api/Area/getUserEntrance";                                      //门禁申请记录接口
    String POSTH_USER_COMMUNIT_URL ="/api/Area/setUserCommunity";                                   //提交申请信息（姓名，身份，社区楼栋房间信息）
    String POSTH_COMMUNITY_LIST_URL ="/api/Area/getCommunityList";                                  //获取社区楼栋列表接口
    String POSTH_UNIT_LIST_URL ="/api/Area/getUnitList";                                            //获取社区下的单元楼栋列表接口
    String POSTH_PROPERTY_LIST_URL ="/api/Area/getPropertyList";                                    //获取楼栋下级房间列表
    String POSTH_USER_INFO_URL ="/api/Appuser/setUserInfo";                                         //个人信息维护接口
    String POSTH_REPAIR_INFO_URL ="/api/Repair/submitRepairInfo";                                   //提交报修信息接口
    String POSTH_REPAIR_INFO_LIST_URL ="/api/Repair/getRepairInfoList";                             //获取报修信息列表接口
    String POSTH_REPAIR_RENT_INFO_URL ="/api/Rent/publishRentInfo";                                 //租赁信息发布接口
    String POSTH_REPAIR_MY_APPLY_URL ="/api/Rent/myRentInfo";                                       //我的发布历史接口
    String POSTH_REPAIR_DEL_RENTINFO_URL ="/api/Rent/delRentInfo";                                  //租赁信息删除接口
    String POSTH_GET_NOTICE_LIST_URL ="/api/Notice/getList";                                        //获取公告列表接口
    String POSTH_ANOTHER_BATCH_URL ="/api/Home/anotherBatch";                                       //换一批
    String POSTH_AD_GET_LIST_URL ="/api/Ad/getList";                                                //获取社区广告
    String POSTH_NOTICE_GET_THREELIST_URL ="/api/Notice/getThreeList";                              //获取社区公告
    String POSTH_UPDATE_RENT_URL ="/api/Rent/updateRent";                                           //租赁信息修改接口
    String POSTH_SUBMIT_RENOVATION_INFO_URL ="/api/Renovation/submitRenovationInfo";                //提交装修申请接口
    String POSTH_DEL_RENOVATION_INFO_URL ="/api/Renovation/delRenovationInfo";                      //删除装修申请信息接口
    String POSTH_GET_RENOVATION_INFO_URL ="/api/Renovation/getRenovationInfoList";                  //获取我的装修申请列表接口
    String POSTH_GET_REPAIR_CATE_URL ="/api/Repaircate/getRepairCate";                              // 获取保修类别接口
    String POSTH_GET_USER_INFO_URL ="/api/Appuser/getUserInfo";                                     //获取用户个人信息
    String POSTH_LEAVE_MSG_LIST_URL ="/api/Rentinfo/leaveMsgList";                                  //获取租赁信息留言列表接口
    String POSTH_LEAVE_MSG_URL ="/api/Rent/leaveMsg";                                  //获取租赁信息留言列表接口
    String POSTH_CIVILIAN_SERVICE_URL ="/api/Civilianservice/indexInfo";                            //便民服务首页界面接口
    String POSTH_CIVILIAN_DETAIL_URL ="/api/Civilianservice/getDetail";                             //查看便民服务详情接口
    String POSTH_TELEPHONE_LIST_URL ="/api/Telephone/getList";                                      //获取电话本列表数据接口
    String POSTH_CIVILIANSERVICE_CATE_URL ="/api/Civilianservice/getCate";                          //获取便民服务所有分类接口
    String POSTH_CIVILIANSERVICE_LIST_URL ="/api/Civilianservice/getList";                          //获取对应分类的便民服务信息列表接口
    String POSTH_CIVILIANSERVICE_AD_URL ="/api/Civilianservice/getAd";                              //获取对应分类的广告接口
    String POSTH_COMMENT_ACTIVITY_URL ="/api/Activity/commentActivity";                             //获取推荐活动接口
    String POSTH_COMMENT_TOPIC_URL ="/api/Topic/commentTopic";                                      //获取推荐话题接口
    String POSTH_RELEASE_TOPIC_URL ="/api/Release/topic";                                           //发布话题、拼车、跳蚤市场接口
    String POSTH_RELEASE_ACTIVITY_URL ="/api/Release/activity";                                     //用户活动发布接口
    String POSTH_ACTIVITY_LIST_URL ="/api/Activity/getList";                                        //更多活动接口
    String POSTH_ACTIVITY_DETAIL_URL ="/api/Activity/activityDetail";                               //获取活动详情界面数据接口
    String POSTH_GET_TOPIC_LIST_URL ="/api/Topic/getTopicList";                                     //获取话题拼车跳蚤市场列表接口
    String POSTH_DO_PRAISE_URL ="/api/Release/doPraise";                                            //话题点赞接口
    String POSTH_CANCEL_PRAISE_URL ="/api/Release/cancelPraise";                                    //取消点赞接口
    String POSTH_COMMENT_LIST_URL ="/api/Topic/commentList";                                        //获取话题评论列表接口
    String POSTH_RELEASE_COMMENT_TOPIC_URL ="/api/Release/commentTopic";                            //评论话题接口




}
