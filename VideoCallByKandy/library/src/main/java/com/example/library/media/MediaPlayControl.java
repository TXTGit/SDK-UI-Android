package com.example.library.media;
import android.content.Context;
import android.media.MediaPlayer;
import com.example.library.R;
/**
 * Created by DELL on 2017/7/27.
 */

public class MediaPlayControl {

    private MediaPlayer mPlayMedia;

    public MediaPlayControl(){

    }
    public void playCallMp3(Context context){
        pauseAndStopPlay();
        creatPlayMedia(context,R.raw.ringbacktone);
    }
    public void playIncommingMp3(Context context){
        pauseAndStopPlay();
        creatPlayMedia(context,R.raw.ringtone);
    }
    public void playConnetMp3(Context context){
        pauseAndStopPlay();
        creatPlayMedia(context,R.raw.connecting);
    }
    public void hangUp(Context context){
        pauseAndStopPlay();
        creatPlayMedia(context,R.raw.hangup);
    }

    public void stopMediaPlay(){
        pauseAndStopPlay();
    }

    public void creatPlayMedia(Context context,int resid) {
        if (mPlayMedia!=null){
            mPlayMedia=null;
        }
        if (resid == R.raw.busytone) {
            mPlayMedia = MediaPlayer.create(context, resid);
        } else if (resid == R.raw.connecting) {
            mPlayMedia = MediaPlayer.create(context, resid);
            mPlayMedia.setLooping(true);
        } else if (resid == R.raw.hangup) {
            mPlayMedia = MediaPlayer.create(context, resid);
        } else if (resid == R.raw.ringbacktone) {
            mPlayMedia = MediaPlayer.create(context, resid);
            mPlayMedia.setLooping(true);
        } else if (resid == R.raw.ringtone) {
            mPlayMedia = MediaPlayer.create(context, resid);
            mPlayMedia.setLooping(true);
        }
        if (mPlayMedia != null) {
            mPlayMedia.start();
        }
    }
    public void pauseAndStopPlay() {
        if (mPlayMedia != null) {
            if (mPlayMedia.isPlaying()) {
                if (mPlayMedia.isLooping())
                    mPlayMedia.setLooping(false);
                mPlayMedia.pause();
                mPlayMedia.stop();
                mPlayMedia.reset();
                mPlayMedia.release();
                mPlayMedia = null;
            }
        }
    }


}
