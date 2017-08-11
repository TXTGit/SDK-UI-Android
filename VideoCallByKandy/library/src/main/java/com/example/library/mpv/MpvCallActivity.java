package com.example.library.mpv;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.library.R;
import com.example.library.activity.BaseActivity;
import com.example.library.call.KandyCall;
import com.example.library.control.TxtKandy;
import com.example.library.model.KandyMultiPartyConferenceCallDetailsProtocol;
import com.example.library.model.Participant;
import com.genband.kandy.api.services.calls.IKandyCall;
import com.genband.kandy.api.services.calls.KandyCallResponseListener;
import com.genband.kandy.api.services.calls.KandyView;
import com.genband.kandy.api.services.common.KandyResponseListener;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantCallMediaState;

import java.util.List;
import java.util.Locale;

/**
 * Created by DELL on 2017/7/25.
 */
public class MpvCallActivity extends BaseActivity implements KandyCall.KandyCallListener {
    private final static String TAG=MpvCallActivity.class.getSimpleName();
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
    private String mUsenamePhone;

    private String mMpvName;

    private boolean mIsFront=true;
    private boolean mIsMute=false;
    private boolean mIsCameraOpen=true;

    public static final String MPVNAME="MPVNAME";
    public static final String MPVNUMBER="MPVNAMER";

    private Runnable mTalkingRunnable;
    private Handler mTalkHandler;
    private long mStartTime=0;
    private LinearLayout mMpvSetting;

    private ListView mMumberList;
    private ImageView mCancleDialog;
    private Button mAddButton;
    private int clickPosition=-1;
    private TextView mMumberNum;
    private List<Participant> participants;
    private MumberAdater mAdater;
    private RelativeLayout mMpvCall,mSettingCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpvcall);
        TxtKandy.getKandyCall().setKandyCallListener(this);
        mMpvName=getIntent().getStringExtra(MPVNAME);
        mUsenamePhone=getIntent().getStringExtra(MPVNUMBER);
        initView();
        initSettingView();
        TxtKandy.getMpvCall().mIsMpvTalking=true;
    }

    private void initView() {
        mMpvCall= (RelativeLayout) findViewById(R.id.call);
        mSettingCall= (RelativeLayout) findViewById(R.id.mpv_setting_layout);

        mRemoteView= (KandyView) findViewById(R.id.remote);
        mLoacalView= (KandyView) findViewById(R.id.local);
        mVideoLayout= (RelativeLayout) findViewById(R.id.videolayout);
        mUserVideoName= (TextView) findViewById(R.id.name);
        mUserVideoName.setText(mMpvName);
        mCallVideoStatue= (TextView) findViewById(R.id.callstatus);
        mCallVideoStatue.setText("正在接入会议");
        mCallTime= (TextView) findViewById(R.id.call_time);
        mHangUplayout= (LinearLayout) findViewById(R.id.hangulayout);
        mHangUplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MpvCallActivity.this.finish();
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {

                    }
                    @Override
                    public void onRequrestFailer() {

                    }
                });
                TxtKandy.getMpvCall().leaveMpv(new TxtMpvCallManmger.MpvRequestCallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFail(int errCode, String errStr) {

                    }
                });
            }
        });

        mMpvSetting= (LinearLayout) findViewById(R.id.mpv_setting);
        mMpvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingCall.setVisibility(View.VISIBLE);
                getMpvDatail();
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
                        MpvCallActivity.this.finish();
                    }

                    @Override
                    public void onRequrestFailer() {

                    }
                });
                TxtKandy.getMpvCall().leaveMpv(new TxtMpvCallManmger.MpvRequestCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int errCode, String errStr) {

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
                            MpvCallActivity.this.runOnUiThread(new Runnable() {
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
                            MpvCallActivity.this.runOnUiThread(new Runnable() {
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
                            MpvCallActivity.this.runOnUiThread(new Runnable() {
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
                            MpvCallActivity.this.runOnUiThread(new Runnable() {
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
        mTalkingRunnable=new Runnable() {
            @Override
            public void run() {
                long time=System.currentTimeMillis()-mStartTime;
                String talking_time=generateTime(time);
                mCallTime.setText(talking_time);
                mTalkHandler.postDelayed(mTalkingRunnable,1000);
            }
        };
        mTalkHandler=new Handler();
        doCall();
    }

    private void initSettingView() {
        mMumberList= (ListView) findViewById(R.id.mumberlist);
        mCancleDialog= (ImageView) findViewById(R.id.cancle);
        mCancleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mSettingCall.setVisibility(View.GONE);
            }
        });
        mAdater=new MumberAdater();
        mAddButton= (Button) findViewById(R.id.addmumber);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getMpvCall().showInvitaDialog(MpvCallActivity.this);
            }
        });
        mMumberList.setAdapter(mAdater);
        mMumberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TxtKandy.getMpvCall().isAdamin){
                    return;
                }
                clickPosition=position;
                mAdater.notifyDataSetChanged();
            }
        });

        mMumberNum= (TextView)findViewById(R.id.num);
        if (participants!=null){
            mMumberNum.setText(participants.size()+"");
        }
    }


    private Handler mReshHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                if (participants!=null){
                    Log.d(TAG, "handleMessage: ");
                    mMumberNum.setText(participants.size()+"");
                    mAdater.notifyDataSetChanged();

                }
            }
        }
    };

    public void getMpvDatail() {
        TxtKandy.getMpvCall().getConferenceDetail(new TxtMpvCallManmger.MpvGetRoomDetailRequestCallBack() {
            @Override
            public void onSuccess(final KandyMultiPartyConferenceCallDetailsProtocol dataJson) {
                Log.d(TAG, "onSuccess: dataJson"+dataJson);
                if (participants!=null){
                    participants.clear();
                    participants=null;
                }
                participants=dataJson.getParticipants();
                if (participants!=null){
                    mReshHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFail(int errCode, String errStr) {

            }
        });
    }


    private void doCall() {
        TxtKandy.getKandyCall().doCall(mUsenamePhone, true, false, mRemoteView, mLoacalView, new KandyCall.CallRequestCallBack() {
            @Override
            public void onRequrestSuccess() {

            }

            @Override
            public void onRequrestFailer() {

            }
        });
    }

    private String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60);
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onTalking() {
        mStartTime=System.currentTimeMillis();
        mTalkHandler.postDelayed(mTalkingRunnable,0);
        mCallTime.setVisibility(View.VISIBLE);
        mCallVideoStatue.setVisibility(View.GONE);
        mButtonItem.setVisibility(View.VISIBLE);
        mHangUplayout.setVisibility(View.GONE);
        mMpvSetting.setVisibility(View.VISIBLE);
        TxtKandy.getMediaConnnect().pauseAndStopPlay();
    }

    @Override
    public void onTenminaten(int code) {
        TxtKandy.getMediaConnnect().hangUp(MpvCallActivity.this);
        TxtKandy.getMediaConnnect().stopMediaPlay();
        MpvCallActivity.this.finish();
    }

    @Override
    public void onIncomingCall(boolean isVideo) {

    }

    @Override
    public void onDialing() {
        mCallVideoStatue.setText("正在接入会议");
        TxtKandy.getMediaConnnect().playConnetMp3(MpvCallActivity.this);
    }

    @Override
    public void onRinging() {
        mCallVideoStatue.setText("正在接入会议");
    }

    @Override
    public void onBackPressed() {
    }

    public class MumberAdater extends BaseAdapter {
        @Override
        public int getCount() {
            return participants!=null?participants.size():0;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(MpvCallActivity.this, R.layout.mumber_item, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MpvCallActivity.MyViewHolder) convertView.getTag();
            }
            if (participants.get(position).getParticipantID()!=null&&!participants.get(position).getParticipantID().equals("")){
                vh.mName.setText(participants.get(position).getParticipantID().split("@")[0]);
            }else {
                vh.mName.setText("默认的不显示的账号");
            }
            if (clickPosition == position){
                if (vh.mHide.getVisibility()==View.VISIBLE){
                    vh.mHide.setVisibility(View.GONE);
                }else {
                    vh.mHide.setVisibility(View.VISIBLE);
                }
            }else {
                if (vh.mHide.getVisibility()==View.VISIBLE){
                    vh.mHide.setVisibility(View.GONE);
                }
            }
            vh.mRemoveveMumberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (participants.get(position).getParticipantID()==null&&!participants.get(position).getParticipantID().equals("")){
                        return;
                    }
                    TxtKandy.getMpvCall().remove(participants.get(position).getParticipantID(), new TxtMpvCallManmger.MpvRequestCallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(int errCode, String errStr) {

                        }
                    });
                }
            });
            vh.mMuteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (participants.get(position).getAudioState()== KandyMultiPartyConferenceParticipantCallMediaState.INCOMING_AND_OUTGOING){
                        if (participants.get(position).getParticipantID()==null&&!participants.get(position).getParticipantID().equals("")){
                            return;
                        }
                        TxtKandy.getMpvCall().mute(participants.get(position).participantID, new TxtMpvCallManmger.MpvRequestCallBack() {
                            @Override
                            public void onSuccess() {
                                getMpvDatail();
                            }
                            @Override
                            public void onFail(int errCode, String errStr) {
                                Log.d(TAG, "onFail:errCode"+errCode+"   errStr:"+errStr);
                            }
                        });
                    }else {
                        TxtKandy.getMpvCall().unmute(participants.get(position).participantID, new TxtMpvCallManmger.MpvRequestCallBack() {
                            @Override
                            public void onSuccess() {
                                getMpvDatail();
                            }
                            @Override
                            public void onFail(int errCode, String errStr) {
                                Log.d(TAG, "onFail:errCode"+errCode+"   errStr:"+errStr);
                            }
                        });
                    }
                }
            });
            vh.mCameraLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (participants.get(position).getVideostate()== KandyMultiPartyConferenceParticipantCallMediaState.INCOMING_AND_OUTGOING){
                        if (participants.get(position).getParticipantID()==null&&!participants.get(position).getParticipantID().equals("")){
                            return;
                        }
                        TxtKandy.getMpvCall().disableVideo(participants.get(position).participantID, new TxtMpvCallManmger.MpvRequestCallBack() {
                            @Override
                            public void onSuccess() {
                                getMpvDatail();
                            }
                            @Override
                            public void onFail(int errCode, String errStr) {
                                Log.d(TAG, "onFail:errCode"+errCode+"   errStr:"+errStr);
                            }
                        });
                    }else {
                        TxtKandy.getMpvCall().enableVideo(participants.get(position).participantID, new TxtMpvCallManmger.MpvRequestCallBack() {
                            @Override
                            public void onSuccess() {
                                getMpvDatail();
                            }
                            @Override
                            public void onFail(int errCode, String errStr) {
                                Log.d(TAG, "onFail:errCode"+errCode+"   errStr:"+errStr);
                            }
                        });
                    }
                }
            });
            return convertView;
        }
    }
    class MyViewHolder {
        TextView mName;
        LinearLayout mHide;
        TextView hide1;
        TextView hide2;
        TextView hide3;
        LinearLayout mRemoveveMumberLayout;
        LinearLayout mMuteLayout;
        LinearLayout mCameraLayout;

        public MyViewHolder(View itemView) {
            mName= (TextView) itemView.findViewById(R.id.name);
            mHide= (LinearLayout) itemView.findViewById(R.id.ll_hide);
            hide1= (TextView) itemView.findViewById(R.id.hide_1);
            hide2= (TextView) itemView.findViewById(R.id.hide_2);
            hide3= (TextView) itemView.findViewById(R.id.hide_3);
            mRemoveveMumberLayout= (LinearLayout) itemView.findViewById(R.id.remove);
            mMuteLayout= (LinearLayout) itemView.findViewById(R.id.mpv_mute);
            mCameraLayout= (LinearLayout) itemView.findViewById(R.id.mpv_camera);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TxtKandy.getMpvCall().mIsMpvTalking=false;
    }

}
