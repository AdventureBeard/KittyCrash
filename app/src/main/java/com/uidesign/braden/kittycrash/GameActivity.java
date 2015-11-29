package com.uidesign.braden.kittycrash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity {

//
//    // SCREEN
//    private int screenWidth;
//    private int screenHeight;
//    private Canvas canvas;
//    private Bitmap bmp;
//    private FrameLayout fl;
//
//
//    // BALL
//    private int x;
//    private int dx = 20;
//    private int y;
//    private int dy = 20;
//    private int ballRadius = 50;
//    private final int ballSpeed = 20;
//
//    // PADDLE
//    private Rect paddle;
//    private int paddleX;
//    private int paddleY;
//
//
//    // Accelerometer-Related Things
//    private SensorManager sensorManager;
//    private Sensor sensorAccelerometer;
//    private Sensor sensorMagfield;
//    private float roll = 0;
//    private final int rollStrength = 1;             // Accelerometer roll influence on ball
//    private float[] rotationMatrix = new float[9];
//    private float[] inclinationMatrix = new float[9];
//    private float[] computedRotationMatrix = new float[9];
//    private float[] accel = new float[3];
//    private float[] mag = new float[3];
//    private float[] accelerometerVals = new float[3];
//    private float[] magfieldVals = new float[9];
//    private float[] results = new float[3];
//    private String text = "OI";
//
//    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));



//        setContentView(R.layout.activity_game);
//
//        screenWidth = getResources().getDisplayMetrics().widthPixels;
//        screenHeight = getResources().getDisplayMetrics().heightPixels;
//        bmp = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(bmp);
//
//
//        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
//        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorMagfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(this, sensorMagfield, SensorManager.SENSOR_DELAY_NORMAL);
//
//        paint = new Paint();
//        paint.setTextSize(30);
//
//        dx = ballSpeed;
//        dy = ballSpeed;
//        fl = (FrameLayout) findViewById(R.id.frame_layout);
//        fl.setBackground(new BitmapDrawable(this.getResources(), bmp));
//
//
//
//        Handler mainHandler = new Handler();
//        Runnable mainAnimation = new Runnable() {
//            public void run() {
//                drawPaddle(canvas);
//                drawBall(canvas);
//                fl.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bmp));
//            }
//        };
//
////        setContentView(new GameView(this));
//    }
//
//    protected void drawBall(Canvas canvas) {
//        // FIXME: 11/28/15  - Fix edge sticking
//        int accelX = (int) Math.round(roll * rollStrength);
//
//        if (x < 0 || y < 0 || (x == 0 && y == 0)) {
//            x = canvas.getWidth() / 2;
//            y = canvas.getHeight() / 2;
//        }
//        int newX = x + dx + accelX;
//        int newY = y + dy;
//
//        if (newX >= canvas.getWidth() - ballRadius || newX <= ballRadius) {
//            dx = dx * -1;
//        } else if (newY >= canvas.getHeight() - ballRadius || newY <= 0 + ballRadius) {
//            dy = dy * -1;
//        } else {
//            x = newX;
//            y = newY;
//        }
//        paint.setColor(Color.WHITE);
//        canvas.drawCircle(x, y, ballRadius, paint);
//        canvas.drawText("" + accelX, 50, 50, paint);
//    }
//
//    private void drawPaddle(Canvas canvas) {
//
//        paddleX = screenWidth / 2;
//        paddleY = 9 * screenHeight / 10;
//
//        Rect rect = new Rect();
//        rect.set(paddleX, paddleY, paddleX + 300, paddleY + 60);
//
//        paint.setColor(Color.GREEN);
//        canvas.drawRect(rect, paint);
//    }
//
//    /** onSensorChanged(SensorEvent event)
//     *
//     * Updates the known roll of the device.
//     *
//     * I didn't do this math on my own! This is difficult stuff. Much of this came from
//     * the page from which the lowpass filter given in class was taken.
//     *
//     * @param event
//     */
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        Sensor sensor = event.sensor;
//
//        // Grab all of the values from our sensors
//        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            accelerometerVals = lpFilter(event.values.clone(), accelerometerVals);
//            accel[0] = event.values[0];
//            accel[1] = event.values[1];
//            accel[2] = event.values[2];
//        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            magfieldVals = lpFilter(event.values, magfieldVals);
//            mag[0] = event.values[0];
//            mag[1] = event.values[1];
//            mag[2] = event.values[2];
//        }
//
//        /* If both sensors have data for us, let's calculate the roll
//         of the phone. This ended up being more of a task than I expected.
//         The "orientation sensor" has since been deprecated, with online resources
//         indicating that calculating phone orientation from both the acceleration and
//         magnetic field sensors is the best method.
//
//         */
//
//        if (magfieldVals != null && accelerometerVals != null) {
//            SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix,
//                    accelerometerVals, magfieldVals);
//            float rotation = getWindowManager().getDefaultDisplay().getRotation();
//
//            // We need to know if the phone is rotated. If so, we need to tell
//            // our sensors that our coordinate system is 90 degrees difference.
//            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
//                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_X,
//                        SensorManager.AXIS_MINUS_Z, computedRotationMatrix);
//            } else {
//                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_Y,
//                        SensorManager.AXIS_MINUS_Z, computedRotationMatrix);
//            }
//
//            SensorManager.getOrientation(computedRotationMatrix, results);
//            // The getOrientation function returns roll, pitch, and yaw based on the
//            // rotation matrix given. The value is in radians so we convert it to degrees.
//            roll = (float) (((results[1] * 180 / Math.PI)));
//            text = roll + "";
//        }
//
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    /** lpFilter(float[] input, float[] output)
//     * The lowpass filter given to us in class.
//     *
//     * @param input     the input float matrix
//     * @param output    the resulting, filtered matrix
//     * @return
//     */
//    protected float[] lpFilter(float[] input, float[] output) {
//        float ALPHA = 0.25f;
//        if (output == null) {
//            return input;
//        }
//
//        for (int i = 0; i < input.length; i++) {
//            output[i] = output[i] + ALPHA * (input[i] - output[i]);
//        }
//        return output;
    }
}
