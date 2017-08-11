package com.example.library.login;

import com.genband.kandy.api.Kandy;
import com.genband.kandy.api.access.KandyLoginResponseListener;
import com.genband.kandy.api.services.calls.KandyRecord;
import com.genband.kandy.api.utils.KandyIllegalArgumentException;

/**
 * Created by wdy on 2017/7/19.
 * kandy用户登录接口
 */

public class AccessKandy {
    public String mUser;
    public AccessKandy(){
    }

    public void userLogin(final String user, String passward, final LoginRequestCallBack callBack){
        KandyRecord kandyRecord = null;
        try {
            kandyRecord = new KandyRecord(user);
        } catch (KandyIllegalArgumentException e) {
            return;
        }
        Kandy.getAccess().login(kandyRecord, passward, new KandyLoginResponseListener() {
            @Override
            public void onLoginSucceeded() {
                if (callBack!=null){
                    mUser=user;
                    callBack.onSuccess();
                }
            }
            @Override
            public void onRequestFailed(int i, String s) {
                callBack.onFail(i,s);
            }
        });
    }

   public interface LoginRequestCallBack{
        public void onSuccess();
        public void onFail(int i,String s);
    }


}

