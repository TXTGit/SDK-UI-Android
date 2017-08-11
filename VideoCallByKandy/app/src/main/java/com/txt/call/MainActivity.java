package com.txt.call;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.login.AccessKandy;
import com.example.library.control.TxtKandy;

public class MainActivity extends Activity {
    private String mUser="user2@sdkdemo.txtechnology.com.cn";
    private String mPassward="ai123456";
    private final String TAG=MainActivity.class.getSimpleName();
    private Button mDoCall;
    private Button  mDoMpvCall;
    private TextView mPhone;
    private ProgressDialog mProgreeDialog;
    private RadioGroup mRdiaoGroup;
    private RadioButton mVideoButton;
    private RadioButton mAudioButton;
    private boolean isVideo=true;
    private EditText mUserEdit;
    private EditText mPasswardEdit;
    private Button mLoginButton;
    private TextView mLoginStatus;

    private boolean isLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // userLogin();
        initView();
    }

    private void initView() {
        mRdiaoGroup= (RadioGroup) findViewById(R.id.redioGoup);
        mVideoButton= (RadioButton) findViewById(R.id.video);
        mAudioButton= (RadioButton) findViewById(R.id.audio);
        mRdiaoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (mVideoButton.getId()==checkedId){
                    isVideo=true;
                    Log.d(TAG, "onCheckedChanged: 1");
                }else if (mAudioButton.getId()==checkedId){
                    isVideo=false;
                    Log.d(TAG, "onCheckedChanged: 2");
                }
            }
        });

        mDoCall= (Button) findViewById(R.id.docall);
        mPhone= (TextView) findViewById(R.id.phone);
        mDoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin){
                    Toast.makeText(MainActivity.this,"请先进行登录",Toast.LENGTH_LONG).show();
                    return;
                }
                TxtKandy.getKandyCall().showDoCallDialog(MainActivity.this,isVideo);
            }
        });
        mDoMpvCall= (Button) findViewById(R.id.dompvcall);
        mDoMpvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin){
                    Toast.makeText(MainActivity.this,"请先进行登录",Toast.LENGTH_LONG).show();
                    return;
                }
                TxtKandy.getConnnectCall().skipDoCallMpv(MainActivity.this);
            }
        });
        mUserEdit= (EditText) findViewById(R.id.edit_user);
        mUserEdit.setText(mUser);
        mPasswardEdit= (EditText) findViewById(R.id.edit_passward);
        mPasswardEdit.setText(mPassward);
        mLoginButton= (Button) findViewById(R.id.loginbutton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(mUserEdit.getText().toString(),mPasswardEdit.getText().toString());
            }
        });
        mLoginStatus= (TextView) findViewById(R.id.loginstatus);
    }

    private void userLogin(final String user,String passward) {
        mProgreeDialog=ProgressDialog.show(this,null,"正在登录用户，请稍后....",true,false);
        TxtKandy.getAccessKandy().userLogin(user, passward, new AccessKandy.LoginRequestCallBack() {
            @Override
            public void onSuccess() {
                mProgreeDialog.dismiss();
                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                mPhone.setText(user);
                mLoginStatus.setText("登录成功");
                isLogin=true;
            }

            @Override
            public void onFail(int i, String s) {
                Log.d(TAG, "onFail: s"+s);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLogin=false;
                        mProgreeDialog.dismiss();
                        Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
