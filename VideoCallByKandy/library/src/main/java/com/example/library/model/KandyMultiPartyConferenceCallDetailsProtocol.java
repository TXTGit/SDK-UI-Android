package com.example.library.model;

import java.util.List;

/**
 * Created by pc on 2017/2/21.
 */

public class KandyMultiPartyConferenceCallDetailsProtocol {
   private ConferenceRoom conferenceRoom;
    private List<Participant> participants;

    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "KandyMultiPartyConferenceCallDetailsProtocol{" +
                "conferenceRoom=" + conferenceRoom +
                ", participants=" + participants +
                '}';
    }
}
