package com.coresun.powerbank.model;

import com.coresun.powerbank.base.BaseModel;
import com.orhanobut.logger.Logger;

public class DataModel {
    public static BaseModel request(String token){
        BaseModel model = null;
        try{
            model = (BaseModel) Class.forName(token).newInstance();
        }catch (ClassNotFoundException e){
            Logger.e(e.getLocalizedMessage());
        }catch (InstantiationException e){
            Logger.e(e.getLocalizedMessage());
        }catch (IllegalAccessException e){
            Logger.e(e.getLocalizedMessage());
        }
        return model;
    }
}
