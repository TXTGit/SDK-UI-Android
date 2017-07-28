package com.example.library.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.library.R;
import com.example.library.call.KandyCall;
import com.example.library.control.ConnectCall;
import com.example.library.kandy.TxtKandy;
import com.genband.kandy.api.services.calls.IKandyCall;
import com.genband.kandy.api.services.calls.KandyCallResponseListener;
import com.genband.kandy.api.services.calls.KandyView;
import com.genband.kandy.api.services.common.KandyResponseListener;

import java.util.Locale;

/**
 * Created by DELL on 2017/7/24.
 */

public class DoCallActivity extends BaseActivity implements KandyCall.KandyCallListener{
    private static final String TAG=DoCallActivity.class.getSimpleName();
    private boolean mIsVideoCall=false;
    private boolean mIsPstn=false;

    private boolean mIsGoing=false;
    //video
    private KandyView mRemoteView;
    private KandyView mLoacalView;
    private RelativeLayout mVideoLayout;

    private TextView mUserVideoName;
    private TextView mCallVideoStatue;
    private TextView mCallTime;
    private LinearLayout mHangUplayout;


    private LinearLayout mButtonItem;
    private LinearLayout mHanguoItemButton;
    private LinearLayout mCamerOffOrOpen;
    private ImageView mCamerabutton;
    private LinearLayout mVideoMute;
    private ImageView mVideoMutebutton;
    private LinearLayout mCameraChange;


    //audio
    private RelativeLayout mAudioLayout;
    private TextView mAudioName;
    private TextView mAudioStatus;
    private TextView mAudioTime;


    private LinearLayout mAudioMute;
    private ImageView mAudioMute_img;

    private LinearLayout mSpeakLayout;

    private ImageView mSpeakLaout_img;

    private LinearLayout mAudioHangUp;
    private boolean mIsFront=true;
    private boolean mIsMute=false;
    private boolean mIsCameraOpen=true;
    private String mUsenamePhone;

    private LinearLayout mAudioItemLayout;


    //incoming

    private RelativeLayout mInComingLayout;
    private TextView InCommingName;
    private TextView mInComingType;
    private LinearLayout mAcceptButton;
    private LinearLayout mRejectButton;

    private RelativeLayout mGoingLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        TxtKandy.getKandyCall().setKandyCallListener(this);
        mUsenamePhone=getIntent().getStringExtra(ConnectCall.NUMBER);
        mIsVideoCall=getIntent().getBooleanExtra(ConnectCall.ISVIDEO,false);
        mIsPstn=getIntent().getBooleanExtra(ConnectCall.ISPSTN,false);
        mIsGoing=getIntent().getBooleanExtra(ConnectCall.ISGOING,true);
        initView();
    }


    private void initView() {
        String name=mUsenamePhone.split("@")[0];

        mGoingLayout= (RelativeLayout) findViewById(R.id.outgoing);
        mRemoteView= (KandyView) findViewById(R.id.remote);
        mLoacalView= (KandyView) findViewById(R.id.local);
        mVideoLayout= (RelativeLayout) findViewById(R.id.videolayout);
        mUserVideoName= (TextView) findViewById(R.id.name);
        mUserVideoName.setText(name);
        mCallVideoStatue= (TextView) findViewById(R.id.callstatus);
        mCallVideoStatue.setText("正在拨打视频，请稍后....");
        mCallTime= (TextView) findViewById(R.id.call_time);
        mHangUplayout= (LinearLayout) findViewById(R.id.hangulayout);
        mHangUplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoCallActivity.this.finish();
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {

                    }
                    @Override
                    public void onRequrestFailer() {

                    }
                });
            }
        });


        //下面Button组
        mButtonItem= (LinearLayout) findViewById(R.id.video_button_item);
        mButtonItem.setVisibility(View.GONE);

        mHanguoItemButton= (LinearLayout) findViewById(R.id.hangup_talk);

        mHanguoItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {
                        DoCallActivity.this.finish();
                    }

                    @Override
                    public void onRequrestFailer() {

                    }
                });
            }
        });
        mCamerOffOrOpen= (LinearLayout) findViewById(R.id.camera);
        mCamerabutton= (ImageView) findViewById(R.id.camerabutton);
        mCamerOffOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: mCamerOffOrOpen");
                if (mIsCameraOpen){
                    Log.d(TAG, "onClick: mCamerOffOrOpen1");
                    TxtKandy.getKandyCall().cameraOpenOrOff(false, new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            mIsCameraOpen=false;
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCamerabutton.setBackgroundResource(R.drawable.camera_open);
                                }
                            });
                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }else {
                    Log.d(TAG, "onClick: mCamerOffOrOpen2");
                    TxtKandy.getKandyCall().cameraOpenOrOff(true, new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            mIsCameraOpen=true;
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCamerabutton.setBackgroundResource(R.drawable.camera_off);
                                }
                            });
                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });

                }

            }
        });


        mVideoMute= (LinearLayout) findViewById(R.id.video_mute);
        mVideoMutebutton= (ImageView) findViewById(R.id.mute_button);
        mVideoMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsMute){
                    TxtKandy.getKandyCall().doUnMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mVideoMutebutton.setBackgroundResource(R.drawable.mute);
                                }
                            });
                            mIsMute=false;

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().doMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            mIsMute=true;
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mVideoMutebutton.setBackgroundResource(R.drawable.unmute);
                                }
                            });

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }

            }
        });
        mCameraChange= (LinearLayout) findViewById(R.id.camera_change);
        mCameraChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFront){
                    TxtKandy.getKandyCall().switchCamer(false, new KandyResponseListener() {
                        @Override
                        public void onRequestSucceded() {
                            mIsFront=false;

                        }

                        @Override
                        public void onRequestFailed(int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().switchCamer(true, new KandyResponseListener() {
                        @Override
                        public void onRequestSucceded() {
                            mIsFront=true;
                        }

                        @Override
                        public void onRequestFailed(int i, String s) {

                        }
                    });

                }
            }
        });

        mSpeakLayout= (LinearLayout) findViewById(R.id.speak);
        mSpeakLaout_img= (ImageView) findViewById(R.id.speakbutton);
        mSpeakLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mAudioMute= (LinearLayout) findViewById(R.id.audio_mute);

        mAudioMute_img= (ImageView) findViewById(R.id.audio_mute_button);

        mAudioMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsMute){
                    TxtKandy.getKandyCall().doUnMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAudioMute_img.setBackgroundResource(R.drawable.mute);
                                }
                            });
                            mIsMute=false;

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().doMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            mIsMute=true;
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAudioMute_img.setBackgroundResource(R.drawable.unmute);
                                }
                            });

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }
            }
        });

        mAudioHangUp= (LinearLayout) findViewById(R.id.auido_hang);
        mAudioHangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {
                        DoCallActivity.this.finish();
                    }

                    @Override
                    public void onRequrestFailer() {

                    }
                });
            }
        });

        mAudioLayout= (RelativeLayout) findViewById(R.id.audiolayout);
        mAudioName= (TextView) findViewById(R.id.audio_name);
        mAudioName.setText(name);
        mAudioStatus= (TextView) findViewById(R.id.audio_status);
        mAudioTime= (TextView) findViewById(R.id.audio_time);
        mStartTime=System.currentTimeMillis();
        mTalkingRunnable=new Runnable() {
            @Override
            public void run() {
                long time=System.currentTimeMillis()-mStartTime;
                String talking_time=generateTime(time);
                if (mIsVideoCall){
                    mCallTime.setText(talking_time);
                }else {
                    mAudioTime.setText(talking_time);
                }
                mTalkHandler.postDelayed(mTalkingRunnable,1000);
            }
        };
        mTalkHandler=new Handler();

        if (mIsVideoCall){
            mVideoLayout.setVisibility(View.VISIBLE);
            mAudioLayout.setVisibility(View.GONE);
        }else {
            mVideoLayout.setVisibility(View.GONE);
            mAudioLayout.setVisibility(View.VISIBLE);
        }
        mAudioItemLayout= (LinearLayout) findViewById(R.id.audio_button_item);
        mAudioItemLayout.setVisibility(View.GONE);
        initCommngView();
    }

    private void initCommngView() {
        mInComingLayout= (RelativeLayout) findViewById(R.id.incomminglayout);
        mInComingType= (TextView) findViewById(R.id.incoming_state);
        mAcceptButton= (LinearLayout) findViewById(R.id.accept);
        InCommingName= (TextView) findViewById(R.id.incoming_name);
        InCommingName.setText(mUsenamePhone);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().accep(new KandyCallResponseListener() {
                    @Override
                    public void onRequestSucceeded(IKandyCall iKandyCall) {

                    }

                    @Override
                    public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                    }
                });
            }
        });
        mRejectButton= (LinearLayout) findViewById(R.id.reject);
        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().rejectIncomingCall();
            }
        });
        if (mIsVideoCall){
            mInComingType.setText("请求视频通话");
        }else {
            mInComingType.setText("请求语音通话");
        }
        if (mIsGoing){
            doCall();
            mGoingLayout.setVisibility(View.VISIBLE);
            mInComingLayout.setVisibility(View.GONE);
        }else {
            mGoingLayout.setVisibility(View.GONE);
            mInComingLayout.setVisibility(View.VISIBLE);
            TxtKandy.getKandyCall().mCurrentCall.setLocalVideoView(mLoacalView);
            TxtKandy.getKandyCall().mCurrentCall.setRemoteVideoView(mRemoteView);
        }
    }

    private void doCall() {
        TxtKandy.getKandyCall().doCall(mUsenamePhone, mIsVideoCall, mIsPstn, mRemoteView, mLoacalView, new KandyCall.CallRequestCallBack() {
            @Override
            public void onRequrestSuccess() {

            }

            @Override
            public void onRequrestFailer() {

            }
        });
    }

    private Runnable mTalkingRunnable;
    private Handler mTalkHandler;
    private long mStartTime=0;


    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60);
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onTalking() {
        TxtKandy.getMediaConnnect().pauseAndStopPlay();
        mGoingLayout.setVisibility(View.VISIBLE);
        mInComingLayout.setVisibility(View.GONE);
        mTalkHandler.postDelayed(mTalkingRunnable,0);
        if (mIsVideoCall){
            mCallTime.setVisibility(View.VISIBLE);
            mCallVideoStatue.setVisibility(View.GONE);
            mButtonItem.setVisibility(View.VISIBLE);

        }else {
            mAudioTime.setVisibility(View.VISIBLE);
            mAudioStatus.setVisibility(View.GONE);
            mAudioItemLayout.setVisibility(View.VISIBLE);
        }
        mHangUplayout.setVisibility(View.GONE);
    }

    @Override
    public void onTenminaten(int code) {
        TxtKandy.getMediaConnnect().hangUp(DoCallActivity.this);
        TxtKandy.getMediaConnnect().stopMediaPlay();
        DoCallActivity.this.finish();
    }

    @Override
    public void onIncomingCall(boolean isVideo) {

    }
    @Override
    public void onDialing() {
        TxtKandy.getMediaConnnect().playConnetMp3(DoCallActivity.this);
        Log.d(TAG, "onDialing: ");
        if (mIsVideoCall){
            mCallVideoStatue.setText("正在拨打视频，请稍后....");
        }else {
            mAudioStatus.setText("正在拨打音频，请稍后...");
        }
    }

    @Override
    public void onRinging() {
        Log.d(TAG, "onRinging: ");
        TxtKandy.getMediaConnnect().playCallMp3(DoCallActivity.this);
        if (mIsVideoCall){
            mCallVideoStatue.setText("正在连通视频，请稍后...");
        }else {
            mAudioStatus.setText("正在连通音频，请稍后...");
        }
    }
}
