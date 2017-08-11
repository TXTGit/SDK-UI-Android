package com.example.library.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.library.call.KandyCall;
import com.example.library.control.TxtKandy;
import com.example.library.mpv.MpvInviteActivity;
import com.example.library.mpv.TxtMpvCallManmger;

/**
 * Created by DELL on 2017/7/26.
 * 服务监听是否有mpv会议来电
 */

public class KandyCallNotifationService extends Service implements KandyCall.CallInCommingListener,TxtMpvCallManmger.MpvInviteCallBack{
    private final static String TAG=KandyCallNotifationService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        TxtKandy.getKandyCall().setInCommingListener(this);
        TxtKandy.getMpvCall().setMpvInviteCallBack(this);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onInviteMpvRecieved(final String sender) {
        Log.d(TAG, "onInviteMpvRecieved: onInviteMpvRecieved");
        Intent intent=new Intent(this, MpvInviteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onInComming() {
        Log.d(TAG, "onInComming: ");
        TxtKandy.getConnnectCall().skipIncommingDoCall(this);
    }
}
