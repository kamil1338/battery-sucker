package com.gmail.kamil1338.batterysucker.usecase;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;

import com.gmail.kamil1338.batterysucker.App;
import com.gmail.kamil1338.batterysucker.model.Response;

import java.lang.ref.WeakReference;

/**
 * Created by kamilpierudzki on 04/04/2017.
 */

public class GpsUseCase implements BaseUseCase, LocationListener {

    private WeakReference<UseCaseCallback<Response>> callback;

    private LocationManager locationManager;
    private Handler backgroundHandler;

    public GpsUseCase(@NonNull UseCaseCallback<Response> callback) {
        this.callback = new WeakReference<>(callback);
        locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void start() {
        if (locationManager != null) {
            HandlerThread handlerThread = new HandlerThread("gps_use_case", Process.THREAD_PRIORITY_BACKGROUND);
            handlerThread.start();
            backgroundHandler = new Handler(handlerThread.getLooper());
        }
        if (locationManager != null) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, backgroundHandler.getLooper());
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, backgroundHandler.getLooper());
                    if (callback.get() != null)
                        callback.get().onGpsCallback(Response.GPS_WORKING);
                } catch (SecurityException exception) {
                    if (callback.get() != null)
                        callback.get().onGpsCallback(Response.GPS_NOT_SUPPORTED);
                }
            } else if (callback.get() != null) {
                callback.get().onGpsCallback(Response.GPS_NEED_START);
            }
        } else if (callback.get() != null) {
            callback.get().onGpsCallback(Response.GPS_NOT_SUPPORTED);
        }
    }

    @Override
    public void stop() {
        if (backgroundHandler != null) {
            backgroundHandler.removeCallbacksAndMessages(null);
            backgroundHandler.getLooper().quitSafely();
            backgroundHandler = null;
        }

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        /** Not implemented*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        /** Not implemented*/
    }

    @Override
    public void onProviderEnabled(String provider) {
        /** Not implemented*/
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (locationManager != null)
            locationManager.removeUpdates(this);

        if (callback.get() != null)
            callback.get().onGpsCallback(Response.GPS_DISABLED);
    }
}
