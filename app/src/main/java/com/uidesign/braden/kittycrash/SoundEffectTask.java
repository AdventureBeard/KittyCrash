package com.uidesign.braden.kittycrash;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

/**
 * Created by braden on 11/12/15.
 */
public class SoundEffectTask extends AsyncTask{

    Context ctx;
    MediaPlayer mp;

    public SoundEffectTask(Context ctx) {

    }

    @Override
    protected Object doInBackground(Object[] params) {
        mp.start();
        return null;
    }
}
