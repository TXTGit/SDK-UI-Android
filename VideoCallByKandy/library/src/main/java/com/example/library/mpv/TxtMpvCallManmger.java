package com.example.library.mpv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.library.control.TxtKandy;
import com.example.library.model.ConferenceRoom;
import com.example.library.model.KandyMultiPartyConferenceCallDetailsProtocol;
import com.example.library.model.Participant;
import com.genband.kandy.api.Kandy;
import com.genband.kandy.api.services.calls.KandyRecord;
import com.genband.kandy.api.services.common.IKandyDomain;
import com.genband.kandy.api.services.common.KandyResponseListener;
import com.genband.kandy.api.services.mpv.IKandyMultiPartyConferenceNotificationListener;
import com.genband.kandy.api.services.mpv.IKandyMultiPartyConferenceParticipant;
import com.genband.kandy.api.services.mpv.IKandyMultiPartyConferenceRoomDetails;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceActionType;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceAnnotation;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceCreateAndInviteListener;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceFailedInvitees;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceInvite;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceInviteListener;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceInvitees;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantActionParmas;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantFailedActionParmas;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantHold;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantJoined;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantLeft;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantMute;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantNameChanged;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantRemoved;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantUnHold;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantUnMute;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantVideoDisabled;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantVideoEnabled;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceRoom;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceRoomDetailsListener;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceRoomRemoved;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceSuccessfullInvitees;
import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceUpdateParticipantActionsListener;
import com.genband.kandy.api.utils.KandyIllegalArgumentException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/7/21.
 */

public class TxtMpvCallManmger implements IKandyMultiPartyConferenceNotificationListener {
        private static final String TAG=TxtMpvCallManmger.class.getSimpleName();
        public String mRoomNum;
        private String mConferenceId;
        private String mRoomPinCode;
        private String mRoomPstnNumber;
        private ArrayList<KandyRecord> mKandyRecords;
        public AlertDialog mInCommingCallDialog;
        public boolean isAdamin=false;
        public MpvInviteCallBack mCallBack;

        public static boolean mIsMpvTalking;
        public TxtMpvCallManmger(){
            registerMultiPartyConferenceListener();
        }
        public void registerMultiPartyConferenceListener() {
            Log.d(TAG, "registerMultiPartyConferenceListener: ");
            Kandy.getServices().getMultiPartyConferenceService().registerNotificationListener(this);
        }
        //添加监听结论
        public void setMpvInviteCallBack(MpvInviteCallBack callback){
            mCallBack=callback;
        }

        //创建房间并邀请成员
        public void createRoomAndInvite(List<String> invites, final MpvCreateRequestCallBack callBack){
            if (mKandyRecords==null){
                mKandyRecords=new ArrayList<>();
            }else {
                mKandyRecords.clear();
            }
            if (invites!=null){
                for (String s:invites){
                    KandyRecord record=null;
                    try {
                        record =new KandyRecord(s);
                    } catch (KandyIllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    mKandyRecords.add(record);
                }
            }
            KandyMultiPartyConferenceInvitees kandyInvitees = new KandyMultiPartyConferenceInvitees();
            if (kandyInvitees==null){
                if (callBack!=null){
                    callBack.onFail(-1,"create Failure");
                    return;
                }
            }
            kandyInvitees.setInviteByChat(mKandyRecords);
            Kandy.getServices().getMultiPartyConferenceService().createRoomAndInvite(KandyMultiPartyConferenceAnnotation.NICKNAME, kandyInvitees, new KandyMultiPartyConferenceCreateAndInviteListener() {
                @Override
                public void onRequestSuceeded(KandyMultiPartyConferenceRoom kandyMultiPartyConferenceRoom, KandyMultiPartyConferenceSuccessfullInvitees kandyMultiPartyConferenceSuccessfullInvitees, KandyMultiPartyConferenceFailedInvitees failedInvite) {
                    Log.d(TAG, "createRoomAndInvite:onRequestSuceeded: "
                            + " createdRoom = "+kandyMultiPartyConferenceRoom
                            +" inviteesSeccess = "+kandyMultiPartyConferenceSuccessfullInvitees
                            +" failedInvite = "+failedInvite);
                    String roomNumber=kandyMultiPartyConferenceRoom.getRoomNumber();
                    String conferenceId=kandyMultiPartyConferenceRoom.getConferenceId();
                    if (roomNumber!=null){
                        mRoomNum=roomNumber;
                        mConferenceId=conferenceId;
                        mRoomPinCode=kandyMultiPartyConferenceRoom.getRoomPinCode();
                        mRoomPstnNumber=kandyMultiPartyConferenceRoom.getRoomPSTNNumber();
                        callBack.onSuccess(mRoomNum);
                    }else {
                        callBack.onFail(-1,"create Failure");
                    }
                }
                @Override
                public void onRequestFailed(int i, String s) {
                    callBack.onFail(i, s);
                }
            });

        }

        //离开会议
        public void leaveMpv(final MpvRequestCallBack callBack){
            if (mConferenceId==null){
                callBack.onFail(-1,"mConferenceId is null");
                return;
            }
            Kandy.getServices().getMultiPartyConferenceService().leave(mConferenceId, new KandyResponseListener() {
                @Override
                public void onRequestSucceded() {
                    callBack.onSuccess();
                }

                @Override
                public void onRequestFailed(int i, String s) {
                    Log.d(TAG, "onRequestFailed: i"+i+"s"+s);
                    callBack.onFail(i,s);
                }
            });
        }

        //销毁会议
        public void destory(final MpvRequestCallBack callBack){
            if (mConferenceId==null){
                callBack.onFail(-1,"destroyRoom is null");
                return;
            }

            Kandy.getServices().getMultiPartyConferenceService().destroyRoom(mConferenceId, new KandyResponseListener() {
                @Override
                public void onRequestSucceded() {
                    callBack.onSuccess();
                }

                @Override
                public void onRequestFailed(int i, String s) {
                    callBack.onFail(i,s);
                }
            });
        }
        //当被邀请的此回调函数将被调用
        @Override
        public void onInviteRecieved(KandyMultiPartyConferenceInvite kandyMultiPartyConferenceInvite) {
            Log.d(TAG, "onInviteRecieved: ");
            kandyMultiPartyConferenceInvite.markAsReceived(new KandyResponseListener()
            {
                @Override
                public void onRequestFailed(int responseCode, String err)
                {
                    Log.e(TAG, "onInviteRecieved:markAsReceived:onRequestFailed: "+ " responseCode = "+responseCode+" err = "+err);
                }

                @Override
                public void onRequestSucceded()
                {
                    Log.d(TAG, "onInviteRecieved:markAsReceived::onRequestSucceded: "+ " ");
                }
            });

            KandyMultiPartyConferenceRoom conferenceRoom = kandyMultiPartyConferenceInvite.getRoom();
            String sender=kandyMultiPartyConferenceInvite.getSender().getUri();
            if (conferenceRoom==null){
                Log.d(TAG,"come in room is null");
            }
            mConferenceId=conferenceRoom.getConferenceId();
            mRoomNum=conferenceRoom.getRoomNumber();
            mRoomPstnNumber=conferenceRoom.getRoomPSTNNumber();
            mRoomPinCode=conferenceRoom.getRoomPinCode();
            ConferenceRoom room=new ConferenceRoom();
            room.setRoomNumber(mRoomNum);
            room.setConferenceID(mConferenceId);
            room.setPinCode(mRoomPinCode);
            room.setRoomPSTNNumber(mRoomPstnNumber);
            TxtKandy.getDataMpvConnnect().addRoom(room);
            if (mCallBack!=null){
                Log.d(TAG, "onInviteRecieved: 1");
                mCallBack.onInviteMpvRecieved(sender);
            }

        }


        @Override
        public void onParticipantMute(KandyMultiPartyConferenceParticipantMute kandyMultiPartyConferenceParticipantMute) {

        }

        @Override
        public void onParticipantUnMute(KandyMultiPartyConferenceParticipantUnMute kandyMultiPartyConferenceParticipantUnMute) {

        }

        @Override
        public void onParticipantJoinedRoom(KandyMultiPartyConferenceParticipantJoined kandyMultiPartyConferenceParticipantJoined) {

        }

        @Override
        public void onParticipantLeftRoom(KandyMultiPartyConferenceParticipantLeft kandyMultiPartyConferenceParticipantLeft) {

        }

        @Override
        public void onParticipantNameChanged(KandyMultiPartyConferenceParticipantNameChanged kandyMultiPartyConferenceParticipantNameChanged) {

        }

        @Override
        public void onParticipantVideoEnabled(KandyMultiPartyConferenceParticipantVideoEnabled kandyMultiPartyConferenceParticipantVideoEnabled) {

        }

        @Override
        public void onParticipantVideoDisabled(KandyMultiPartyConferenceParticipantVideoDisabled kandyMultiPartyConferenceParticipantVideoDisabled) {

        }

        @Override
        public void onParticipantHold(KandyMultiPartyConferenceParticipantHold kandyMultiPartyConferenceParticipantHold) {

        }

        @Override
        public void onParticipantUnHold(KandyMultiPartyConferenceParticipantUnHold kandyMultiPartyConferenceParticipantUnHold) {

        }

        @Override
        public void onParticipantRemoved(KandyMultiPartyConferenceParticipantRemoved kandyMultiPartyConferenceParticipantRemoved) {

        }

        @Override
        public void onConferenceRoomRemoved(KandyMultiPartyConferenceRoomRemoved kandyMultiPartyConferenceRoomRemoved) {

        }

        //静音效果
        public void mute(String participantID,MpvRequestCallBack callback){
            Log.d(TAG, "mute: participantID");
            performControlActionOnParticipant(participantID,callback, KandyMultiPartyConferenceActionType.MUTE);
        }

        public void unmute(String participantID,MpvRequestCallBack callback){
            Log.d(TAG, "unmute: ");
            performControlActionOnParticipant(participantID,callback,KandyMultiPartyConferenceActionType.UNMUTE);
        }
        //挂起效果
        public void hold(String participantID,MpvRequestCallBack callback){
            performControlActionOnParticipant(participantID,callback,KandyMultiPartyConferenceActionType.HOLD);
        }

        public void unHold(String participantID, MpvRequestCallBack callback){
            performControlActionOnParticipant(participantID,callback,KandyMultiPartyConferenceActionType.UNHOLD);
        }
        //开关摄像头
        public void enableVideo(String participantID, MpvRequestCallBack callback){
            performControlActionOnParticipant(participantID,callback,KandyMultiPartyConferenceActionType.ENABLE_VIDEO);
        }

        public void disableVideo(String participantID, MpvRequestCallBack callback){
            performControlActionOnParticipant(participantID,callback,KandyMultiPartyConferenceActionType.DISABLE_VIDEO);
        }
        //移除会议成员
        public void remove(String participantID, MpvRequestCallBack callback){
            performControlActionOnParticipant(participantID,callback,KandyMultiPartyConferenceActionType.REMOVE);
        }

        private void performControlActionOnParticipant(String participantID, final MpvRequestCallBack callback, KandyMultiPartyConferenceActionType actionType){
            KandyMultiPartyConferenceParticipantActionParmas actionParam = new KandyMultiPartyConferenceParticipantActionParmas(participantID, actionType);
            List<KandyMultiPartyConferenceParticipantActionParmas> participantActionParams = new ArrayList<KandyMultiPartyConferenceParticipantActionParmas>();
            participantActionParams.add(actionParam);
            Log.d(TAG, "performControlActionOnParticipant: mConferenceId"+mConferenceId);
            Kandy.getServices().getMultiPartyConferenceService().updateParticipantActions(mConferenceId, participantActionParams, new KandyMultiPartyConferenceUpdateParticipantActionsListener() {
                @Override
                public void onRequestSuceeded(List<KandyMultiPartyConferenceParticipantActionParmas> list, List<KandyMultiPartyConferenceParticipantFailedActionParmas> list1) {
                    Log.d(TAG, "onRequestSuceeded: performControlActionOnParticipant"+list);
                    Log.d(TAG, "onRequestSuceeded: performControlActionOnParticipant"+list1);
                    callback.onSuccess();
                }

                @Override
                public void onRequestFailed(int i, String s) {
                    Log.d(TAG, "onRequestFailed: i"+i+"s"+s);
                    callback.onFail(-1,s);
                }
            });
        }

        public void skipMpvCall(Context context,String mpvName){
            ConferenceRoom room=new ConferenceRoom();
            room.setRoomNumber(mRoomNum);
            room.setConferenceID(mConferenceId);
            room.setPinCode(mRoomPinCode);
            room.setRoomPSTNNumber(mRoomPstnNumber);
            room.setmRoomName(mpvName);
            TxtKandy.getDataMpvConnnect().addRoom(room);
            callRoomNumber(context,mRoomNum,mpvName);
        }
        public void saveData(){
            ConferenceRoom room=new ConferenceRoom();
            room.setRoomNumber(mRoomNum);
            room.setConferenceID(mConferenceId);
            room.setPinCode(mRoomPinCode);
            room.setRoomPSTNNumber(mRoomPstnNumber);
            room.setmRoomName("");
            TxtKandy.getDataMpvConnnect().addRoom(room);
        }


        public void callRoomNumber(Context context,String roomNumber,String mpvName){
            KandyRecord roomRecord = createKandyRecordWithDomain(roomNumber);
            if (roomRecord!=null){
                Intent intent=new Intent(context,MpvCallActivity.class);
                intent.putExtra(MpvCallActivity.MPVNAME,mpvName);
                intent.putExtra(MpvCallActivity.MPVNUMBER,roomRecord.getUri());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                TxtKandy.getMediaConnnect().playCallMp3(context);
            }
        }


        private KandyRecord createKandyRecordWithDomain(String number) {
            IKandyDomain kandyDomain = Kandy.getSession().getKandyDomain();
            if(kandyDomain == null)
            {
                Log.e(TAG,"createKandyRecordWithDomain:  missing domain" );
                return null;
            }
            String kandyDomainName = kandyDomain.getName();

            KandyRecord record = null;
            try
            {
                record = new KandyRecord(number,kandyDomainName);
            }
            catch (KandyIllegalArgumentException e)
            {
                Log.e(TAG,"createKandyRecordWithDomain: "  + e.getLocalizedMessage());
            }
            return record;
        }


        public interface MpvRequestCallBack{
            public void onSuccess();
            public void onFail(int errCode,String errStr);
        }

        public interface MpvCreateRequestCallBack{
            public void onSuccess(String roomNumber);
            public void onFail(int errCode,String errStr);
        }


        public interface MpvGetRoomDetailRequestCallBack{
            public void onSuccess(KandyMultiPartyConferenceCallDetailsProtocol dataJson);
            public void onFail(int errCode,String errStr);
        }
        //邀请群成员
        public void inviteWithInviteeArr(final List<String> inviteeArr, final MpvRequestCallBack callback){
            if (inviteeArr==null){
                return;
            }
            ArrayList<KandyRecord> inviteesByChat=new ArrayList<>();
            for(int i=0;i<inviteeArr.size();i++){
                KandyRecord record=null;
                try {
                    record =new KandyRecord(inviteeArr.get(i));
                } catch (KandyIllegalArgumentException e) {
                    e.printStackTrace();
                    callback.onFail(-1,"inviteeArr is null");
                    return;
                }
                inviteesByChat.add(record);
            }

            KandyMultiPartyConferenceInvitees kandyInvitees = new KandyMultiPartyConferenceInvitees();
            kandyInvitees.setInviteByChat(inviteesByChat);
            kandyInvitees.setInviteBySMS(new ArrayList<String>());
            kandyInvitees.setInviteByMail(new ArrayList<String>());
            Kandy.getServices().getMultiPartyConferenceService().invite(mConferenceId, kandyInvitees, new KandyMultiPartyConferenceInviteListener() {
                @Override
                public void onRequestSuceeded(KandyMultiPartyConferenceSuccessfullInvitees kandyMultiPartyConferenceSuccessfullInvitees, KandyMultiPartyConferenceFailedInvitees kandyMultiPartyConferenceFailedInvitees) {
                    Log.d(TAG, "onRequestSuceeded: inviteWithInviteeArr");
                    callback.onSuccess();
                }

                @Override
                public void onRequestFailed(int i, String s) {
                    Log.d(TAG, "onRequestFailed: inviteWithInviteeArr i"+i+"s"+s);
                    callback.onFail(-1,s);
                }
            });

        }
        //获取群详情内容
        public void getConferenceDetail(final MpvGetRoomDetailRequestCallBack cb){
            if (mConferenceId.equals("")||mConferenceId==null){
                cb.onFail(-1,"ConferenceId is null");
                return;
            }

            Log.d(TAG, "getConferenceDetail: mConferenceId"+mConferenceId);
            Kandy.getServices().getMultiPartyConferenceService().getRoomDetails(mConferenceId, new KandyMultiPartyConferenceRoomDetailsListener() {
                @Override
                public void onRequestSuceeded(IKandyMultiPartyConferenceRoomDetails iKandyMultiPartyConferenceRoomDetails) {
                    if (iKandyMultiPartyConferenceRoomDetails==null){
                        cb.onFail(-1,"RoomDetails is null");
                        return;
                    }
                    Log.d(TAG, "onRequestSuceeded: iKandyMultiPartyConferenceRoomDetails");
                    KandyMultiPartyConferenceCallDetailsProtocol callDetails=new KandyMultiPartyConferenceCallDetailsProtocol();
                    ConferenceRoom conferenceRoom=new ConferenceRoom();
                    conferenceRoom.setConferenceID(iKandyMultiPartyConferenceRoomDetails.getConferenceRoom().getConferenceId());
                    conferenceRoom.setPinCode(iKandyMultiPartyConferenceRoomDetails.getConferenceRoom().getRoomPinCode());
                    conferenceRoom.setRoomNumber(iKandyMultiPartyConferenceRoomDetails.getConferenceRoom().getRoomNumber());
                    conferenceRoom.setRoomPSTNNumber(iKandyMultiPartyConferenceRoomDetails.getConferenceRoom().getRoomPSTNNumber());
                    callDetails.setConferenceRoom(conferenceRoom);
                    List<Participant> participants=new ArrayList<Participant>();
                    List<String> adintors=iKandyMultiPartyConferenceRoomDetails.getAdministrators();
                    Log.d(TAG,"adintors"+adintors);
                    isAdamin=false;
                    for (String admin:adintors){
                        if (TxtKandy.getAccessKandy().mUser.equals(admin)){
                            isAdamin=true;
                            Log.d(TAG, "onRequestSuceeded: isAdamin"+isAdamin);
                        }
                    }
                    Log.d(TAG,"adintors"+iKandyMultiPartyConferenceRoomDetails.getParticipants());
                    for (IKandyMultiPartyConferenceParticipant participant:iKandyMultiPartyConferenceRoomDetails.getParticipants()){
                        Participant participantOther=new Participant();
                        Log.d(TAG, "onRequestSuceeded: "+participant.getParticipantId());
                        participantOther.setAudioState(participant.getAudioState());
                        participantOther.setVideostate(participant.getVideoState());
                        participantOther.setCallDuration(participant.getCallDuration());
                        participantOther.setParticipantID(participant.getParticipantId());
                        String displayName ="";
                        if (displayName == null || displayName.equals("")) {
                            if (participant.getNickName()!=null&&!participant.getNickName().equals("")){
                                displayName=participant.getNickName();
                            }else {
                                displayName = participant.getParticipantId().split("@")[0];
                            }
                        }
                        participantOther.setNickname(displayName);
                        participantOther.setAdmin("0");
                        for (String admin:adintors){
                            if (admin.equals(participant.getParticipantId())){
                                participantOther.setAdmin("1");
                            }
                        }
                        participants.add(participantOther);
                    }
                    callDetails.setParticipants(participants);
                    String json= new Gson().toJson(callDetails);
                    Log.d(TAG,"getRoomDetails"+json);
                    if (json!=null){
                        cb.onSuccess(callDetails);
                    }

                }
                @Override
                public void onRequestFailed(int i, String s) {
                    cb.onFail(i,"s");
                }
            });

        }
        //显示邀请的对话框
        public void showInvitaDialog(final Context context){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("请输入邀请的账号：");
            final EditText edit = new EditText(context);
            edit.setText("aiiphone6p@xbsx.txtechnology.com.cn");
            builder.setView(edit);
            builder.setPositiveButton("邀请", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<String> invaties=new ArrayList<String>();
                    if (edit.getText().toString().equals("")){
                        Toast.makeText(context,"邀请人不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String invatile=edit.getText().toString();
                    if (!invatile.contains("@")){
                        invatile=createKandyRecordWithDomain(invatile).getUri();
                    }
                    Log.d(TAG, "showInvitaDialog: invatile"+invatile);
                    invaties.add(invatile);
                    inviteWithInviteeArr(invaties, new MpvRequestCallBack() {
                        @Override
                        public void onSuccess() {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"邀请成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFail(int errCode, String errStr) {
                            Toast.makeText(context,"邀请失败",Toast.LENGTH_SHORT).show();
                        }
                    });
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
            if (mInCommingCallDialog==null){
                mInCommingCallDialog=builder.create();
                mInCommingCallDialog.setCanceledOnTouchOutside(true);
            }
            mInCommingCallDialog.show();
        }

        public interface MpvInviteCallBack{
            public void onInviteMpvRecieved(String sender);
        }

        public void skipListCall(Context context,ConferenceRoom room){
            mConferenceId=room.getConferenceID();
            mRoomNum=room.getRoomNumber();
            mRoomPinCode=room.getPinCode();
            mRoomPstnNumber=room.getRoomPSTNNumber();
            callRoomNumber(context,mRoomNum,room.getmRoomName());
        }

}
