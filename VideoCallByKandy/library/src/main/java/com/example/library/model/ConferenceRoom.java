package com.example.library.model;

/**
 * Created by pc on 2017/2/21.
 */

public class ConferenceRoom {
    private String conferenceID;
    private String roomNumber;
    private String roomPSTNNumber;
    private String pinCode;
    private String mRoomName;

    public String getmRoomName() {
        return mRoomName;
    }

    public void setmRoomName(String mRoomName) {
        this.mRoomName = mRoomName;
    }

    public String getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(String conferenceID) {
        this.conferenceID = conferenceID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomPSTNNumber() {
        return roomPSTNNumber;
    }

    public void setRoomPSTNNumber(String roomPSTNNumber) {
        this.roomPSTNNumber = roomPSTNNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
