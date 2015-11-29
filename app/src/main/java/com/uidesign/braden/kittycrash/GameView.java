package com.uidesign.braden.kittycrash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.hardware.SensorEventListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by braden on 11/28/15.
 */
public class GameView extends View implements SensorEventListener {

    // SCREEN
    private int screenWidth;
    private int screenHeight;
    private float lastTouchX;
    private float lastTouchY;
    boolean firstRun = true;

    Handler collisionHandler;
    Runnable collisionCheck;

    // BALL
    private int x;
    private int dx = 20;
    private int y;
    private int dy = 20;
    private int ballRadius = 50;
    private final int ballSpeed = 20;
    private boolean blockCollision = false;

    // PADDLE
    private Rect paddle;
    private int paddleX = 0;
    private int paddleY = 0;
    private int paddleDX;

    // Accelerometer-Related Things
    private boolean ignoreSensor;
    private Handler ignoreSensorHandler;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagfield;
    private float roll = 0;
    private final int rollStrength = 1;             // Accelerometer roll influence on ball
    private float[] rotationMatrix = new float[9];
    private float[] inclinationMatrix = new float[9];
    private float[] computedRotationMatrix = new float[9];
    private float[] accel = new float[3];
    private float[] mag = new float[3];
    private float[] accelerometerVals = new float[3];
    private float[] magfieldVals = new float[9];
    private float[] results = new float[3];
    private String text = "OI";

    private Paint paint;

    public GameView(Context ctx) {
        super(ctx);

        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagfield, SensorManager.SENSOR_DELAY_NORMAL);
        ignoreSensorHandler = new Handler();

        paint = new Paint();
        paint.setTextSize(30);

        dx = ballSpeed;
        dy = ballSpeed;
//
//        collisionHandler = new Handler();
//        collisionCheck = new Runnable() {
//            @Override
//            public void run() {
//               checkCollisions();
//            }
//        };
//        collisionHandler.postDelayed(collisionCheck, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkCollisions();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#9FB6CD"));
        canvas.drawPaint(paint);
        drawPaddle(canvas);
        drawBall(canvas);
        invalidate();
    }

    private void drawPaddle(Canvas canvas) {
        if (firstRun) {
            paddleX = getWidth() / 2 - 300;
            paddleY = 9 * getHeight() / 10;
            firstRun = false;
        }

        if (paddleX + 150 < lastTouchX) {
            paddleDX = 20;
        } else if (paddleX + 150 > lastTouchX) {
            paddleDX = -20;
        } else {
            paddleDX = 0;
        }

        int newPaddleX = paddleX + paddleDX;
        paddleX = newPaddleX;

        Rect rect = new Rect();
        rect.set(paddleX, paddleY, paddleX + 300, paddleY + 60);
        paint.setColor(Color.GREEN);
        canvas.drawRect(rect, paint);
    }

    protected void drawBall(Canvas canvas) {
        // FIXME: 11/28/15  - Fix edge sticking
        int accelX = (int) Math.round(roll * rollStrength);


        if (x < 0 || y < 0 || (x == 0 && y == 0)) {
            x = canvas.getWidth() / 2;
            y = canvas.getHeight() / 2;
        }
        int newX = x + dx + accelX;
        int newY = y + dy;

        if (blockCollision) {
            dy *= -1;
            y = y - (paddleY - y);
        } else if (newX >= canvas.getWidth() - ballRadius || newX <= ballRadius) {
            dx = dx * -1;
        } else if (newY >= canvas.getHeight() - ballRadius || newY <= 0 + ballRadius) {
            dy = dy * -1;
        } else {
            x = newX;
            y = newY;
        }
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, ballRadius, paint);
        canvas.drawText("" + accelX, 50, 50, paint);
        canvas.drawText("Last Touched: " + lastTouchX, 50, 120, paint);
        canvas.drawText("Paddle X: " + paddleX, 50, 160, paint);
        canvas.drawText("Ball X: " + x, 50, 200, paint);
        canvas.drawText("Ball Y: " + y, 50, 240, paint);
        canvas.drawText("Ball DX: " + dx, 50, 280, paint);
        canvas.drawText("Ball DY: " + dy, 50, 320, paint);
        canvas.drawText("Paddle DX: " + paddleDX, 50, 360, paint);
        canvas.drawText("Block Collision: " + blockCollision, 50, 400, paint);
        canvas.drawText("Ignore Sensor: " + ignoreSensor, 50, 440, paint);

    }

    private void checkCollisions() {
        if ((x > paddleX - ballRadius && x < paddleX + 300 + ballRadius) &&
                ((y > paddleY - ballRadius && y < paddleY + 60 + ballRadius))) {
            blockCollision = true;
        } else {
            blockCollision = false;
        }
//        collisionHandler.postDelayed(collisionCheck, 10);
    }

    /** onSensorChanged(SensorEvent event)
     *
     * Updates the known roll of the device.
     * I didn't figure out this chain of fxn calls on my own. Much of this came from
     * the page from which the lowpass filter given in class was taken.
     *
     * @param event
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lastTouchX = event.getX();
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        // Grab all of the values from our sensors
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerVals = lpFilter(event.values.clone(), accelerometerVals);
            accel[0] = event.values[0];
            accel[1] = event.values[1];
            accel[2] = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magfieldVals = lpFilter(event.values, magfieldVals);
            mag[0] = event.values[0];
            mag[1] = event.values[1];
            mag[2] = event.values[2];
        }

        /* If both sensors have data for us, let's calculate the roll
         of the phone. This ended up being more of a task than I expected.
         The "orientation sensor" has since been deprecated, with online resources
         indicating that calculating phone orientation from both the acceleration and
         magnetic field sensors is the best method.
         */
        if (magfieldVals != null && accelerometerVals != null) {
            SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix,
                    accelerometerVals, magfieldVals);
            float rotation = getRotation();

            // We need to know if the phone is rotated. If so, we need to tell
            // our sensors that our coordinate system is 90 degrees difference.
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_X,
                        SensorManager.AXIS_MINUS_Z, computedRotationMatrix);
            } else {
                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_Y,
                        SensorManager.AXIS_MINUS_Z, computedRotationMatrix);
            }

            SensorManager.getOrientation(computedRotationMatrix, results);
            // The getOrientation function returns roll, pitch, and yaw based on the
            // rotation matrix given. The value is in radians so we convert it to degrees.
            roll = (float) (((results[1] * 180 / Math.PI)));
            text = roll + "";
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /** lpFilter(float[] input, float[] output)
     * The lowpass filter given to us in class.
     *
     * @param input     the input float matrix
     * @param output    the resulting, filtered matrix
     * @return
     */
    protected float[] lpFilter(float[] input, float[] output) {
        float ALPHA = 0.25f;
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
