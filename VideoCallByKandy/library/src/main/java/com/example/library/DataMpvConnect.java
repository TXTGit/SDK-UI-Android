package com.example.library;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.library.model.ConferenceRoom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/7/26.
 */

public class DataMpvConnect {
    private static String TAG=DataMpvConnect.class.getSimpleName();
    private SharedPreferences sp;
    private List<ConferenceRoom> mDatas;
    private SharedPreferences.Editor editor;
    private Context mContext;

    private static final String DATA="data";
    public DataMpvConnect(){
    }


    public void init(Context context){
        mContext=context;
        sp=context.getSharedPreferences("mpvdata",Context.MODE_PRIVATE);
        editor=sp.edit();
        mDatas=new Gson().fromJson(sp.getString(DATA,""),new TypeToken<List<ConferenceRoom>>(){}.getType());
        Log.d(TAG, "init: mDatas"+mDatas);
    }

     public  List<ConferenceRoom> getRoomData(){
      return mDatas;
     }

     public void clearData(){
         mDatas.clear();
         editor.clear();
         editor.commit();

     }
     public void addRoom(ConferenceRoom room){
         if (mDatas==null){
             mDatas=new ArrayList<>();
         }
         Log.d(TAG, "addRoom: addRoom");
         mDatas.add(room);
         String json=new Gson().toJson(mDatas);
         Log.d(TAG, "addRoom: addRoom json"+json);
         editor.putString(DATA,json);
         editor.commit();
     }
}
