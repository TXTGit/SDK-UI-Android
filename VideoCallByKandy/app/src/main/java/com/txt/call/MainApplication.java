package com.txt.call;


import android.util.Log;

import com.example.library.application.BaseKandyApplication;
import com.example.library.kandy.TxtKandy;

/**
 * Created by DELL on 2017/7/19.
 */

public class MainApplication extends BaseKandyApplication {

    private final static String TAG=MainApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: MainApplication");

    }
}
