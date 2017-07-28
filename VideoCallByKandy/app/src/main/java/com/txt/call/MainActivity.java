package com.txt.call;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.kandy.AccessKandy;
import com.example.library.kandy.TxtKandy;

public class MainActivity extends Activity {
    private String mUser="8613052335273@xbsx.txtechnology.com.cn";
    private String mPassward="a821dec88de74a";
    private final String TAG=MainActivity.class.getSimpleName();
    private Button mDoCall;
    private Button  mDoMpvCall;
    private TextView mPhone;
    private ProgressDialog mProgreeDialog;
    private RadioGroup mRdiaoGroup;
    private RadioButton mVideoButton;
    private RadioButton mAudioButton;
    private boolean isVideo=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLogin();
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
        mPhone.setText(mUser);
        mDoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().showDoCallDialog(MainActivity.this,isVideo);
                //TxtKandy.getConnnectCall().skipDoCall(MainActivity.this,false,false,true,"8613818693614@xbsx.txtechnology.com.cn");
            }
        });
        mDoMpvCall= (Button) findViewById(R.id.dompvcall);
        mDoMpvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getConnnectCall().skipDoCallMpv(MainActivity.this);
            }
        });
    }

    private void userLogin() {
        mProgreeDialog=ProgressDialog.show(this,null,"正在登录用户，请稍后....",true,false);
        TxtKandy.getAccessKandy().userLogin(mUser, mPassward, new AccessKandy.LoginRequestCallBack() {
            @Override
            public void onSuccess() {
                mProgreeDialog.dismiss();
                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(int i, String s) {
                Log.d(TAG, "onFail: s"+s);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgreeDialog.dismiss();
                        Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
