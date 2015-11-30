package com.uidesign.braden.kittycrash;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by braden on 11/28/15.
 */
public class GameView extends View implements SensorEventListener {

    // SCREEN
    int screenWidth, screenHeight;
    private float lastTouchX;
    boolean firstRun = true;
    boolean touchRelease = false;
    int playerScore = 0;

    // BALL
    private int x;
    private int dx = 20;
    private int y;
    private int dy = 20;
    private int ballRadius = 50;
    private final int ballSpeed = 20;
    private int speedMultiplier = 1;
    private boolean paddleCollision = false;

    // PADDLE
    private Rect paddle;
    private int paddleX = 0;
    private int paddleY = 0;
    private int paddleDX;


    // LEVEL
    LevelManager levelManager;
    ArrayList<Block> levelState;

    // Accelerometer-Related Things
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

    private Paint paint;

    public GameView(Context ctx) {
        super(ctx);

        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagfield, SensorManager.SENSOR_DELAY_NORMAL);

        paint = new Paint();
        paint.setTextSize(80);

        paddle = new Rect();

        dx = ballSpeed;
        dy = ballSpeed;


        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    screenWidth = getWidth();
                    screenHeight = getHeight();
                    levelManager = new LevelManager(screenWidth, screenHeight);
                    levelState = levelManager.getCurrentLevel();
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkCollisions();
        drawBackground(canvas);
        drawPaddle(canvas);
        drawBall(canvas);
        drawLevel(canvas);
        invalidate();
    }

    private void drawBackground(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#9FB6CD"));
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + playerScore, 5 * screenWidth / 8, 120, paint);
    }

    private void drawPaddle(Canvas canvas) {
        if (firstRun) {
            paddleX = screenWidth / 2 - 300;
            paddleY = 9 * screenHeight / 10;
            firstRun = false;
        }

        if (touchRelease) {
            paddleDX = 0;
        } else if (paddle.centerX() < lastTouchX && paddle.right < screenWidth) {
            paddleDX = 20;
        } else if (paddle.centerX() > lastTouchX && paddle.left > 0) {
            paddleDX = -20;
        } else {
            paddleDX = 0;
        }
        int newPaddleX = paddleX + paddleDX;
        paddleX = newPaddleX;

        paddle.set(paddleX, paddleY, paddleX + 300, paddleY + 60);
        paint.setColor(Color.parseColor("#b82c4d"));
        canvas.drawRect(paddle, paint);
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

        if (newX >= screenWidth - ballRadius) {
            dx *= -1;
            x += -10;
        } else if (newX <= ballRadius) {
            dx *= -1;
            x += 10;
        } else if (newY >= screenHeight - ballRadius) {
            gameOver();
        } else if (newY <= ballRadius) {
            dy *= -1;
            y += 10;
        } else {
            x = newX;
            y = newY;
        }
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, ballRadius, paint);

    }


    private void drawLevel(Canvas canvas) {
        if (levelState != null) {
            for (int i = 0; i < levelState.size(); i++) {
                drawBlock(levelState.get(i), canvas);
            }
        }
    }

    private void drawBlock(Block block, Canvas canvas) {
        if (block == null || block.getHitpoints() == 0) return;
        paint.setColor(block.getColor());
        canvas.drawRect(block.getRectangle(), paint);
    }

    private void checkCollisions() {
        paddleCollision = (x > paddleX - ballRadius && x < paddleX + 300 + ballRadius) &&
                ((y > paddleY - ballRadius && y < paddleY + 60 + ballRadius));
        if (paddleCollision) {
            dy *= -1;
            y = y - (paddleY - y);
        } else {
            for (int i = 0; i < levelState.size(); i++) {
                switch (levelState.get(i).collision(x, y, ballRadius)) {
                    case 0:
                        break;
                    case 1: // TOP COLLISION
                        dy *= -1;
                        y += -10;
                        playerScore += 10 * speedMultiplier;
                        return;
                    case 2: // LEFT COLLISION
                        dx *= -1;
                        x += -10;
                        playerScore += 10 * speedMultiplier;
                        return;
                    case 3: // BOTTOM COLLISION
                        dy *= -1;
                        y += 10;
                        playerScore += 10 * speedMultiplier;
                        return;
                    case 4: // RIGHT COLLISION
                        dx *= -1;
                        x += 10;
                        playerScore += 10 * speedMultiplier;
                        return;
                }
            }
        }
    }

    private void gameOver() {
        Context ctx = this.getContext();
        Intent intent = new Intent(ctx, ScoreboardActivity.class);
        intent.putExtra("playerScore", playerScore);
        ctx.startActivity(intent);
    }

    /**
     * onSensorChanged(SensorEvent event)
     * <p/>
     * Updates the known roll of the device.
     * I didn't figure out this chain of fxn calls on my own. Much of this came from
     * the page from which the lowpass filter given in class was taken.
     *
     * @param event
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchRelease = false;
            lastTouchX = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchRelease = true;
        }

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
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                        SensorManager.AXIS_MINUS_Z, computedRotationMatrix);
            } else {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y,
                        SensorManager.AXIS_MINUS_Z, computedRotationMatrix);
            }

            SensorManager.getOrientation(computedRotationMatrix, results);
            // The getOrientation function returns roll, pitch, and yaw based on the
            // rotation matrix given. The value is in radians so we convert it to degrees.
            roll = (float) (((results[1] * 180 / Math.PI)));
        }
    }

    /**
     * lpFilter(float[] input, float[] output)
     * The lowpass filter given to us in class.
     *
     * @param input  the input float matrix
     * @param output the resulting, filtered matrix
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


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
