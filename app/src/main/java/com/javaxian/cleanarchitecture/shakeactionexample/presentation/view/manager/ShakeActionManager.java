package com.javaxian.cleanarchitecture.shakeactionexample.presentation.view.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeActionManager implements SensorEventListener {

    private Context context;

    private SensorManager sensorManager;
    private Sensor sensor;

    private IShakeActionListener listener;

    private static final float ALPHA = 0.8f;
    private static final int ACCELERATION_MAX_RANGE = 20;
    private static final int COUNTER_MIN = 3;
    private static final int TIME_INTERVAL = 500;

    private float gravity[] = new float[3];

    private int counterMoves = 0;
    private long firstTimeMove = 0;
    private long nextTimeMove = 0;

    public ShakeActionManager(Context context, IShakeActionListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void startShakeAction() {

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED);
        registerShakeAction();
    }

    public void registerShakeAction() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterShakeAction() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        gravity[0] = gravityLowPassFilter(sensorEvent.values[0], 0);
        gravity[1] = gravityLowPassFilter(sensorEvent.values[1], 1);
        gravity[2] = gravityLowPassFilter(sensorEvent.values[2], 2);

        float xAccelaration = gravityHighPassFilter(sensorEvent.values[0], 0);
        float yAcceleration = gravityHighPassFilter(sensorEvent.values[1], 1);
        float zAcceleration = gravityHighPassFilter(sensorEvent.values[2], 2);


        float maxAcceleration = Math.max(Math.max(xAccelaration, yAcceleration), zAcceleration);

        if (maxAcceleration > ACCELERATION_MAX_RANGE) {

            if (counterMoves == 0) {
                counterMoves++;
                firstTimeMove = System.currentTimeMillis();
                Log.i(ShakeActionManager.class.getName(), "first: " + firstTimeMove);
            } else {
                nextTimeMove = System.currentTimeMillis();

                if ((nextTimeMove - firstTimeMove) < TIME_INTERVAL) {
                    counterMoves++;
                    Log.i(ShakeActionManager.class.getName(), "counter: " + counterMoves);
                    if (counterMoves >= COUNTER_MIN) {
                        if (listener != null) {
                            listener.onShakeAction();
                            counterMoves = 0;
                            firstTimeMove = System.currentTimeMillis();
                        }
                    }

                } else {
                    counterMoves = 0;
                    firstTimeMove = System.currentTimeMillis();
                }

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private float gravityLowPassFilter(float value, int index) {

        return (gravity[index] = ALPHA * gravity[0] + (1 - ALPHA) * value);

    }

    private float gravityHighPassFilter(float value, int index) {

        return (value - gravity[index]);

    }

}
