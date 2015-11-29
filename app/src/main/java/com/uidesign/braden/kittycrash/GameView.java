package com.uidesign.braden.kittycrash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.Surface;
import android.view.View;
import android.hardware.SensorEventListener;

/**
 * Created by braden on 11/28/15.
 */
public class GameView extends View implements SensorEventListener{

    int x;
    int dx = 20;

    int y;
    int dy = 20;

    int ballRadius = 50;
    int dRadius = 3;


    // BALL MOVEMENT
    final int ballSpeed = 20;
    String text = "OI";

    // Accelerometer-Related Things
    SensorManager sensorManager;
    Sensor sensorAccelerometer;
    Sensor sensorMagfield;
    float roll = 0;
    final int rollStrength = 1;             // Accelerometer roll influence on ball
    private float[] rotationMatrix = new float[9];
    private float[] inclinationMatrix = new float[9];
    private float[] computedRotationMatrix = new float[9];
    private float[] accel = new float[3];
    private float[] mag = new float[3];
    private float[] accelerometerVals = new float[3];
    private float[] magfieldVals = new float[9];
    private float[] results = new float[3];

    Paint paint;


    public GameView(Context ctx) {
        super(ctx);

        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagfield, SensorManager.SENSOR_DELAY_NORMAL);

        paint = new Paint();
        paint.setTextSize(30);

        x = getWidth()/ 2;
        y = getHeight()/2;
        dx = ballSpeed;
        dy = ballSpeed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int accelX = (int) Math.round(roll * rollStrength);

        if (x < 0 || y < 0 || (x == 0 && y == 0)) {
            x = canvas.getWidth() / 2;
            y = canvas.getHeight() / 2;
        }

        if (x + dx + accelX > canvas.getWidth() - ballRadius || x + dx + accelX <= 0 + ballRadius) {
            dx = dx * -1;
        } else if (y + dy > canvas.getHeight() - ballRadius || y + dy <= 0 + ballRadius) {
            dy = dy * -1;
        } else {
            x = x + dx + accelX;
            y = y + dy;
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#9FB6CD"));
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, ballRadius, paint);
        canvas.drawText(text, 50, 50, paint);
        invalidate();
    }

    /** onSensorChanged(SensorEvent event)
     *
     * Updates the known roll of the device.
     *
     * I didn't do this math on my own! This is difficult stuff. Much of this came from
     * the page from which the lowpass filter given in class was taken.
     *
     * @param event
     */

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
