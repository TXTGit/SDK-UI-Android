package com.example.library.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.library.kandy.TxtKandy;
import com.example.library.service.KandyCallNotifationService;

/**
 * Created by DELL on 2017/7/19.
 */

public class BaseKandyApplication extends Application {
    protected String KEY="";
    protected String SCRET="";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BaseKandyApplication", "onCreate:BaseKandyApplication ");
        TxtKandy.getKandyCall().initKandy(this,null,null);
        TxtKandy.getMpvCall();
        TxtKandy.getDataMpvConnnect().init(this);
        Intent intent=new Intent(this, KandyCallNotifationService.class);
        startService(intent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
