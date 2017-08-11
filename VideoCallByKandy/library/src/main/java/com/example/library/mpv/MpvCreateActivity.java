package com.example.library.mpv;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.R;
import com.example.library.activity.BaseActivity;
import com.example.library.control.TxtKandy;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DELL on 2017/7/25.
 */

public class MpvCreateActivity extends BaseActivity {
    private static String TAG=MpvCreateActivity.class.getSimpleName();
    private TextView mBack;
    private TextView mCancle;
    private LinearLayout mUserList;
    private EditText mUserEdit;
    private ImageView mAddUser;
    private  EditText mMpvName;
    private Button mCreateRoom;
    private List<String> mInvites;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpv_create);
        initView();
    }

    private void initView() {
        mInvites=new ArrayList<>();
        mBack= (TextView) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MpvCreateActivity.this.finish();
            }
        });

        mCancle= (TextView) findViewById(R.id.cancle);
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MpvCreateActivity.this.finish();
            }
        });

        mUserList= (LinearLayout) findViewById(R.id.addlist);
        mUserEdit= (EditText) findViewById(R.id.edit_user);
        mAddUser= (ImageView) findViewById(R.id.add_button);

        mUserEdit.setText("user4@sdkdemo.txtechnology.com.cn");
        mAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserEdit.getText().equals("")){
                    Toast.makeText(MpvCreateActivity.this,"添加的邀请人员不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }else if (!mUserEdit.getText().toString().contains("@")){
                    Toast.makeText(MpvCreateActivity.this,"你要添加的账号格式不正确！",Toast.LENGTH_LONG).show();
                    return;
                }
                addUser();
            }
        });
        mMpvName= (EditText) findViewById(R.id.mpv_name);
        mCreateRoom= (Button) findViewById(R.id.createroom);
        mCreateRoom.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMpvName.getText().toString().equals("")){
                        Toast.makeText(MpvCreateActivity.this,"请输入会议名称",Toast.LENGTH_LONG).show();
                    return;
                }

                MpvCreateActivity.this.finish();
                TxtKandy.getMpvCall().createRoomAndInvite(mInvites, new TxtMpvCallManmger.MpvCreateRequestCallBack() {
                    @Override
                    public void onSuccess(final String roomNumber) {
                        Log.d(TAG, "onSuccess: roomNumber"+roomNumber);
                        MpvCreateActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               TxtKandy.getMpvCall().skipMpvCall(MpvCreateActivity.this,mMpvName.getText().toString());
                            }
                        });
                    }
                    @Override
                    public void onFail(int errCode, String errStr) {
                        Log.d(TAG, "onFail: ");
                        MpvCreateActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MpvCreateActivity.this,"房间创建失败！",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }
    public void addUser(){
        mInvites.add(mUserEdit.getText().toString());
        View view= LayoutInflater.from(MpvCreateActivity.this).inflate(R.layout.user_item,null);
        TextView textView= (TextView) view.findViewById(R.id.mpv_user_name);
        String name=mUserEdit.getText().toString();
        textView.setText(name.split("@")[0]);
        mUserList.addView(view);
        mUserEdit.setText("");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
