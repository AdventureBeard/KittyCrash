package com.uidesign.braden.kittycrash;

import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;

import java.net.URI;

/**
 * Created by braden on 11/12/15.
 */
public class MusicTask extends AsyncTask{

    Context ctx;

    public MusicTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        MediaPlayer mp = MediaPlayer.create(ctx, R.raw.music);
        MediaPlayer mp2 = MediaPlayer.create(ctx, R.raw.kittycrash);
        mp.setVolume(0.5f, 0.5f);
        mp.start();
        mp2.start();
        return null;
    }
}
