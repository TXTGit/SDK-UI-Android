package com.example.library.mpv;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.R;
import com.example.library.activity.BaseActivity;
import com.example.library.control.TxtKandy;

/**
 * Created by DELL on 2017/7/26.
 */

public class MpvInviteActivity extends BaseActivity {
    private TextView mInviteName;
    private Context mContext;
    private RelativeLayout mAdd;
    private RelativeLayout mHolde;
    private RelativeLayout mRefuse;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mpv_invite);
        initView();
    }

    private void initView() {
        mInviteName= (TextView) findViewById(R.id.sendername);
        mAdd= (RelativeLayout)findViewById(R.id.add_confence);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TxtKandy.getMpvCall().mIsMpvTalking){
                    Toast.makeText(MpvInviteActivity.this,"您当前正在会议中，若加入，请先结束当前会议",Toast.LENGTH_LONG).show();
                    return;
                }
                TxtKandy.getConnnectCall().skipMpvCall(MpvInviteActivity.this);
                MpvInviteActivity.this.finish();
            }
        });
        mHolde= (RelativeLayout)findViewById(R.id.hold_confence);
        mHolde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getMpvCall().saveData();
                MpvInviteActivity.this.finish();
            }
        });

        mRefuse= (RelativeLayout)findViewById(R.id.refuse_confence);
        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MpvInviteActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
