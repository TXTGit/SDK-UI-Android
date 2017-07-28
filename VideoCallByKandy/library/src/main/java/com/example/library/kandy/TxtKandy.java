package com.example.library.kandy;

import android.util.Log;

import com.example.library.DataMpvConnect;
import com.example.library.call.KandyCall;
import com.example.library.control.ConnectCall;
import com.example.library.media.MediaPlayControl;
import com.example.library.mpv.TxtMpvCallManmger;

/**
 * Created by DELL on 2017/7/19.
 */

public class TxtKandy {
    public static AccessKandy mAccessKandy;
    public static KandyCall mKandyCall;
    public static TxtMpvCallManmger mpvCallManmger;

    private static DataMpvConnect mDataMpv;

    public static ConnectCall mConnect;
    public static MediaPlayControl mMediaControl;
    public static AccessKandy getAccessKandy(){

        if (mAccessKandy==null)
        {
            mAccessKandy=new AccessKandy();
        }
        return mAccessKandy;
    }

    public static KandyCall getKandyCall(){
        if (mKandyCall==null){
            mKandyCall=new KandyCall();
        }
        return mKandyCall;
    }

    public static TxtMpvCallManmger getMpvCall(){
        if (mpvCallManmger==null){
            mpvCallManmger=new TxtMpvCallManmger();
        }
        return mpvCallManmger;
    }

    public static ConnectCall getConnnectCall(){
        if (mConnect==null){
            mConnect=new ConnectCall();
        }
        return mConnect;
    }

    public static DataMpvConnect getDataMpvConnnect(){
        Log.d("wdy", "getDataMpvConnnect: ");
        if (mDataMpv==null){
            mDataMpv=new DataMpvConnect();
        }
        return mDataMpv;
    }

    public static MediaPlayControl getMediaConnnect(){
        Log.d("wdy", "getDataMpvConnnect: ");
        if (mMediaControl==null){
            mMediaControl=new MediaPlayControl();
        }
        return mMediaControl;
    }
}
