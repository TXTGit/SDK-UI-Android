package com.example.library.model;

import com.genband.kandy.api.services.mpv.KandyMultiPartyConferenceParticipantCallMediaState;

/**
 * Created by pc on 2017/2/21.
 */

public class Participant {
    public String participantID;
    public String nickname;
    public int callDuration;
    public KandyMultiPartyConferenceParticipantCallMediaState audioState;
    public KandyMultiPartyConferenceParticipantCallMediaState videostate;
    public String isAdmin;

    public String getParticipantID() {
        return participantID;
    }

    public void setParticipantID(String participantID) {
        this.participantID = participantID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }

    public KandyMultiPartyConferenceParticipantCallMediaState getAudioState() {
        return audioState;
    }

    public void setAudioState(KandyMultiPartyConferenceParticipantCallMediaState audioState) {
        this.audioState = audioState;
    }

    public KandyMultiPartyConferenceParticipantCallMediaState getVideostate() {
        return videostate;
    }

    public void setVideostate(KandyMultiPartyConferenceParticipantCallMediaState videostate) {
        this.videostate = videostate;
    }

    public String isAdmin() {
        return isAdmin;
    }

    public void setAdmin(String admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "participantID='" + participantID + '\'' +
                ", nickname='" + nickname + '\'' +
                ", callDuration='" + callDuration + '\'' +
                ", audioState='" + audioState + '\'' +
                ", videostate='" + videostate + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                '}';
    }
}
