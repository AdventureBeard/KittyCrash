package com.uidesign.braden.kittycrash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game);
//
        setContentView(new GameView(this));
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        int screenHeight = getResources().getDisplayMetrics().heightPixels;
//        Bitmap bmp = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bmp);
//
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.parseColor("#9FB6CD"));
//        canvas.drawPaint(paint);
//        paint.setColor(Color.WHITE);
//        canvas.drawCircle(screenWidth / 2, screenHeight / 2, 100, paint);
//        FrameLayout fl = (FrameLayout) findViewById(R.id.frame_layout);
//        fl.setBackground(new BitmapDrawable(this.getResources(), bmp));



//        setContentView(new GameView(this));
    }

}
