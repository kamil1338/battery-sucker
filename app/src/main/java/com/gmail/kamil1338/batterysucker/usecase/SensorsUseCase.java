package com.gmail.kamil1338.batterysucker.usecase;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;

import com.gmail.kamil1338.batterysucker.App;
import com.gmail.kamil1338.batterysucker.model.Response;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamilpierudzki on 10/04/2017.
 */

public class SensorsUseCase implements BaseUseCase, SensorEventListener2 {

    private WeakReference<UseCaseCallback<Response>> callback;

    private SensorManager sensorManager;
    private List<Sensor> sensors;

    public SensorsUseCase(UseCaseCallback<Response> callback) {
        this.callback = new WeakReference<>(callback);
        sensorManager = (SensorManager) App.getContext().getSystemService(Context.SENSOR_SERVICE);

        sensors = new ArrayList<>();
        if (sensorManager != null) {
            sensors.addAll(sensorManager.getSensorList(Sensor.TYPE_ALL));
        }
    }

    @Override
    public void start() {
        if (sensorManager != null) {
            for (Sensor sensor : sensors) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
            }
            if (sensors.size() > 0 && callback.get() != null) {
                callback.get().onSensorsCallback(Response.SENSORS_ENABLED);
            } else if (callback.get() != null) {
                callback.get().onSensorsCallback(Response.SENSORS_NOT_SUPPORTED);
            }
        }
    }

    @Override
    public void stop() {
        if (sensorManager != null) {
            for (Sensor sensor : sensors) {
                sensorManager.unregisterListener(this, sensor);
            }
            if (callback.get() != null) {
                callback.get().onSensorsCallback(Response.SENSORS_DISABLED);
            }
        } else if (callback.get() != null) {
            callback.get().onSensorsCallback(Response.SENSORS_NOT_SUPPORTED);
        }
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        /** Not implemented*/
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /** Not implemented*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /** Not implemented*/
    }
}
