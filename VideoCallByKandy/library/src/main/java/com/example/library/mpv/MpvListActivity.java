package com.example.library.mpv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.library.R;
import com.example.library.activity.BaseActivity;
import com.example.library.control.TxtKandy;
import com.example.library.model.ConferenceRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/7/25.
 */
public class MpvListActivity extends BaseActivity {
    private static String TAG=MpvListActivity.class.getSimpleName();
    private ListView mRoomList;
    private TextView mBack;
    private TextView mAdd;
    private ListMpvAdater mDataAdater;
    private List<ConferenceRoom> mRooms;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpv_list);
        initView();
    }

    private void initView() {
        mRoomList= (ListView) findViewById(R.id.mpvlist);
        mBack= (TextView) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MpvListActivity.this.finish();
            }
        });
        mAdd= (TextView) findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MpvListActivity.this,MpvCreateActivity.class);
                startActivity(intent);
            }
        });
        mRooms=new ArrayList<>();
        if (TxtKandy.getDataMpvConnnect().getRoomData()!=null&&!TxtKandy.getDataMpvConnnect().getRoomData().equals("")){
            List<ConferenceRoom> rooms=TxtKandy.getDataMpvConnnect().getRoomData();
            for (int i=rooms.size()-1;i>-1;i--){
                mRooms.add(rooms.get(i));
            }
        }
        mDataAdater=new ListMpvAdater();
        mRoomList= (ListView) findViewById(R.id.mpvlist);
        mRoomList.setAdapter(mDataAdater);
        Log.d(TAG, "initView: "+mRooms);
        mRoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TxtKandy.getMpvCall().skipListCall(MpvListActivity.this,mRooms.get(position));
            }
        });
        Log.d(TAG, "initView: 1");
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mRooms!=null){
            mRooms.clear();
            Log.d(TAG, "onResume: 11111"+TxtKandy.getDataMpvConnnect().getRoomData());
            if (TxtKandy.getDataMpvConnnect().getRoomData()!=null&&!TxtKandy.getDataMpvConnnect().getRoomData().equals("")){
                List<ConferenceRoom> rooms=TxtKandy.getDataMpvConnnect().getRoomData();
                for (int i=rooms.size()-1;i>-1;i--){
                    mRooms.add(rooms.get(i));
                }
            }
            if (mDataAdater!=null){
                mDataAdater.notifyDataSetChanged();
            }
        }
    }

    public class ListMpvAdater extends BaseAdapter{
        @Override
        public int getCount() {
            return mRooms!=null?mRooms.size():0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyHolder vh;
            if (convertView == null) {
                convertView = View.inflate(MpvListActivity.this, R.layout.mpv_list_item, null);
                vh=new MyHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyHolder) convertView.getTag();
            }
            if (mRooms.get(position).getmRoomName()!=null&&!mRooms.get(position).getmRoomName().equals("")){
                vh.mName.setText(mRooms.get(position).getmRoomName());
            }else {
                vh.mName.setText(mRooms.get(position).getRoomNumber());
            }
            return convertView;
        }

    }

    class MyHolder {
        TextView mName;
        public MyHolder(View itemView) {
            mName= (TextView) itemView.findViewById(R.id.mpvName);
        }
    }

}
