package com.example.library.call;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.library.kandy.TxtKandy;
import com.example.library.util.KandyLogger;
import com.genband.kandy.api.IKandyGlobalSettings;
import com.genband.kandy.api.Kandy;
import com.genband.kandy.api.services.calls.IKandyCall;
import com.genband.kandy.api.services.calls.IKandyIncomingCall;
import com.genband.kandy.api.services.calls.IKandyOutgoingCall;
import com.genband.kandy.api.services.calls.KandyCallResponseListener;
import com.genband.kandy.api.services.calls.KandyCallServiceNotificationListener;
import com.genband.kandy.api.services.calls.KandyCallState;
import com.genband.kandy.api.services.calls.KandyOutgingVoipCallOptions;
import com.genband.kandy.api.services.calls.KandyOutgoingPSTNCallOptions;
import com.genband.kandy.api.services.calls.KandyRecord;
import com.genband.kandy.api.services.calls.KandyView;
import com.genband.kandy.api.services.common.KandyCameraInfo;
import com.genband.kandy.api.services.common.KandyMissedCallMessage;
import com.genband.kandy.api.services.common.KandyResponseListener;
import com.genband.kandy.api.services.common.KandyWaitingVoiceMailMessage;
import com.genband.kandy.api.utils.KandyIllegalArgumentException;
import com.genband.kandy.api.utils.KandyLog;

/**
 * Created by DELL on 2017/7/19.
 */

public class KandyCall implements KandyCallServiceNotificationListener {
    private final static String TAG=KandyCall.class.getSimpleName();

    private  String API_KEY = "DAK858161e538ed4802896ad0f1520f9118";
    private  String API_SECRET = "DAS96c8a0f63f0f42a4917871967a27e098";
    public IKandyCall mCurrentCall=null;
    private Context mContext;

    public  AlertDialog dialog=null;
    public AlertDialog mInCommingCallDialog;

    public KandyCallListener mKandyCallListener;

    public KandyCall(){

    }

    public void initKandy(Context context,String Apikey,String ApiSecret){
        mContext=context;
        if (Apikey!=null){
            API_KEY=Apikey;
            API_SECRET=ApiSecret;
        }
        Kandy.getKandyLog().setLogLevel(KandyLog.Level.VERBOSE);
        Kandy.initialize(mContext, API_KEY, API_SECRET);
        Kandy.getKandyLog().setLogger(new KandyLogger());
        IKandyGlobalSettings settings = Kandy.getGlobalSettings();
        settings.setKandyHostURL("https://api.kandycn.com");
        Kandy.getGlobalSettings().setWebRTCLogsEnabled(true);
        Kandy.getGlobalSettings().setPowerSaverEnable(false);
        registerCallListener();
    }

    public void setKandyCallListener(KandyCallListener listener){
        mKandyCallListener=listener;
    }

    public void registerCallListener(){
        Kandy.getServices().getCallService().registerNotificationListener(this);
    }

    public void doCall(final String number,final boolean isVideo, boolean ispstn,final KandyView remote,final KandyView local,final CallRequestCallBack callBack){
        boolean bIsPSTNCall = ispstn;
        if (bIsPSTNCall){
            Log.d(TAG,"PSTN");
            mCurrentCall= Kandy.getServices().getCallService().createPSTNCall(null, number, KandyOutgoingPSTNCallOptions.NONE);
        }else {
            Log.i(TAG,"VOIP");
            KandyRecord callee=null;
            try {
                callee=new KandyRecord(number);
            } catch (KandyIllegalArgumentException e) {
                e.printStackTrace();
            }
            mCurrentCall= Kandy.getServices().getCallService().createVoipCall(null,callee,isVideo? KandyOutgingVoipCallOptions.START_CALL_WITH_VIDEO: KandyOutgingVoipCallOptions.AUDIO_ONLY_CALL);
        }
        if (mCurrentCall==null){
            if (callBack!=null){
                callBack.onRequrestFailer();
            }
            return;
        }
        if (remote==null){
            Log.d(TAG, "mRemoteView is null");
            return;
        }
        if (local==null){
            Log.d(TAG, "mLocalView is null");
            return;
        }
        mCurrentCall.setRemoteVideoView(remote);
        mCurrentCall.setLocalVideoView(local);
        mCurrentCall.getCallee().getUri();
        mCurrentCall.getCallee().getUserName();
        ((IKandyOutgoingCall) mCurrentCall).establish(new KandyCallResponseListener() {
            @Override
            public void onRequestSucceeded(IKandyCall call) {
                if (callBack!=null){
                    callBack.onRequrestSuccess();
                }
            }
            @Override
            public void onRequestFailed(IKandyCall call, int responseCode, String err) {
                if (callBack!=null){
                    callBack.onRequrestFailer();
                }
            }
        });
    }

    public void doMute(KandyCallResponseListener listener) {
        if (mCurrentCall==null){
            return;
        }
        mCurrentCall.mute(listener);
    }
    public void doUnMute(KandyCallResponseListener listener) {
        if (mCurrentCall==null){
            return;
        }
        mCurrentCall.unmute(listener);
    }

    public void switchCamer(boolean isFront, KandyResponseListener listener){
        if (mCurrentCall==null){
            return;
        }
        if (isFront){
            mCurrentCall.switchCamera(KandyCameraInfo.FACING_FRONT,null,listener);
        }else {
            mCurrentCall.switchCamera(KandyCameraInfo.FACING_BACK,null,listener);
        }
    }

    public void cameraOpenOrOff(boolean isOnpen,KandyCallResponseListener listener){
        if (mCurrentCall==null){
            return;
        }
        if (isOnpen){
            Log.d(TAG, "cameraOpenOrOff: open");
            mCurrentCall.startVideoSharing(listener);
        }else {
            Log.d(TAG, "cameraOpenOrOff: close");
            mCurrentCall.stopVideoSharing(listener);
        }
    }


    //接收通话
    public void accep(KandyCallResponseListener listener){
        if (mCurrentCall==null){
            return;
        }
        boolean flag=false;
        if (mCurrentCall.canReceiveVideo()){
            flag=true;
        }
        ((IKandyIncomingCall)mCurrentCall).accept(flag,listener);
    }

    @Override
    public void onIncomingCall(IKandyIncomingCall iKandyIncomingCall) {
        Log.d(TAG, "onIncomingCall: ");
        mCurrentCall=iKandyIncomingCall;
        if (mKandyCallListener!=null){
            Log.d(TAG, "onIncomingCall: 1");
            mKandyCallListener.onIncomingCall(iKandyIncomingCall.canReceiveVideo());
        }
        if (mListener!=null){
            mListener.onInComming();
        }
    }

    @Override
    public void onMissedCall(KandyMissedCallMessage kandyMissedCallMessage) {

    }

    @Override
    public void onWaitingVoiceMailCall(KandyWaitingVoiceMailMessage kandyWaitingVoiceMailMessage) {

    }
    @Override
    public void onCallStateChanged(KandyCallState kandyCallState, IKandyCall iKandyCall) {
        Log.d(TAG,"onCallStateChanged"+kandyCallState);
        if (kandyCallState == KandyCallState.TERMINATED) {
            if (mKandyCallListener!=null){
             mKandyCallListener.onTenminaten(iKandyCall.getTerminationReason().getStatusCode());
            }
            mCurrentCall=null;
        } else if (kandyCallState == KandyCallState.TALKING) {
            if (mKandyCallListener!=null){
             mKandyCallListener.onTalking();
            }
        }else if (kandyCallState == KandyCallState.DIALING){
            if (mKandyCallListener!=null){
                mKandyCallListener.onDialing();
            }

        }else if (kandyCallState == KandyCallState.RINGING){
            if (mKandyCallListener!=null){
            mKandyCallListener.onRinging();
            }
        }
    }

    @Override
    public void onVideoStateChanged(IKandyCall iKandyCall, boolean b, boolean b1) {

    }

    @Override
    public void onGSMCallIncoming(IKandyCall iKandyCall, String s) {

    }

    @Override
    public void onGSMCallConnected(IKandyCall iKandyCall, String s) {

    }

    @Override
    public void onGSMCallDisconnected(IKandyCall iKandyCall, String s) {

    }

    public interface CallRequestCallBack{
        void onRequrestSuccess();
        void onRequrestFailer();
    }

    public void hangUp(final CallRequestCallBack callBack){
        if (mCurrentCall!=null){
            mCurrentCall.hangup(new KandyCallResponseListener() {
                @Override
                public void onRequestSucceeded(IKandyCall iKandyCall) {
                    Log.d(TAG,"hangup onRequestSucceeded");
                    mCurrentCall=null;
                    if (callBack!=null){
                        callBack.onRequrestSuccess();
                    }
                }

                @Override
                public void onRequestFailed(IKandyCall iKandyCall, int code, String err) {
                    Log.d(TAG,"hangup onRequestFailed:"+err+">>>code"+code);
                    mCurrentCall=null;
                    if (callBack!=null){
                        callBack.onRequrestFailer();
                    }
                }
            });
        }else {
            if (callBack!=null){
                callBack.onRequrestSuccess();
            }
        }
    }


    public void rejectIncomingCall() {
        if (mCurrentCall==null){
            return;
        }
        ((IKandyIncomingCall) mCurrentCall).reject(new KandyCallResponseListener() {
            @Override
            public void onRequestSucceeded(IKandyCall call) {
                mCurrentCall=null;
                Log.i(TAG, "mCurrentIncomingCall.reject succeeded");
            }
            @Override
            public void onRequestFailed(IKandyCall call, int responseCode, String err) {
                mCurrentCall=null;
                Log.i(TAG, "mCurrentIncomingCall.reject. Error: " + err + "\nResponse code: " + responseCode);
            }
        });
    }
    public interface KandyCallListener{
        void onTalking();
        void onTenminaten(int code);
        void onIncomingCall(boolean isVideo);
        void onDialing();
        void onRinging();
    }

    public void dissDoCallDialog(){
        dialog.dismiss();
    }

    public boolean isDoCallVideo=true;
    public void showDoCallDialog(final Context context,boolean isVideo){
        isDoCallVideo=isVideo;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("请输入拨打账号：");
        final EditText edit = new EditText(context);
        edit.setText("8613818693614@xbsx.txtechnology.com.cn");
        builder.setView(edit);
        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edit.getText().toString().equals("")){
                    Toast.makeText(context,"不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                TxtKandy.getConnnectCall().skipDoCall(context,isDoCallVideo,false,true,edit.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        if (dialog==null){
            dialog=builder.create();
            dialog.setCanceledOnTouchOutside(true);
        }
        dialog.show();
    }

    public void dissInCommingDialog(){
        if (mInCommingCallDialog!=null){
            mInCommingCallDialog.dismiss();
        }
    }

    public void showInCommingDialog(Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        String uri=mCurrentCall.getCallee().getUri();
        String name=mCurrentCall.getCallee().getUserName();
        builder.setTitle(name+"向您发起来电请求");
        builder.setPositiveButton("接听", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accep(new KandyCallResponseListener() {
                    @Override
                    public void onRequestSucceeded(IKandyCall iKandyCall) {

                    }

                    @Override
                    public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                    }
                });
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("挂断", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rejectIncomingCall();
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        if (mInCommingCallDialog==null){
            mInCommingCallDialog=builder.create();
            mInCommingCallDialog.setCanceledOnTouchOutside(true);
        }
        mInCommingCallDialog.show();
    }

    private CallInCommingListener mListener;

    public void setInCommingListener(CallInCommingListener listener){
        mListener=listener;
    }

    public interface  CallInCommingListener{
        public void onInComming();
    }
}
