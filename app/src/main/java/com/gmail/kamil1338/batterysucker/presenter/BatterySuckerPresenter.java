package com.gmail.kamil1338.batterysucker.presenter;

import android.support.annotation.NonNull;

import com.gmail.kamil1338.batterysucker.Contract;
import com.gmail.kamil1338.batterysucker.model.ConfigDataModel;
import com.gmail.kamil1338.batterysucker.model.Response;
import com.gmail.kamil1338.batterysucker.usecase.BaseUseCase;
import com.gmail.kamil1338.batterysucker.usecase.BluetoothUseCase;
import com.gmail.kamil1338.batterysucker.usecase.CpuUseCase;
import com.gmail.kamil1338.batterysucker.usecase.GpsUseCase;
import com.gmail.kamil1338.batterysucker.usecase.SensorsUseCase;
import com.gmail.kamil1338.batterysucker.utils.MainThread;

import java.lang.ref.WeakReference;

/**
 * Created by kamilpierudzki on 30/03/2017.
 */

public class BatterySuckerPresenter implements Contract.Presenter, BaseUseCase.UseCaseCallback<Response> {

    private WeakReference<Contract.View<Response>> view;

    private BaseUseCase cpuUseCase;
    private BluetoothUseCase bluetoothUseCase;
    private GpsUseCase gpsUseCase;
    private SensorsUseCase sensorsUseCase;

    public BatterySuckerPresenter(@NonNull Contract.View<Response> view) {
        this.view = new WeakReference<>(view);
        this.view.get().setPresenter(this);

        cpuUseCase = new CpuUseCase(this);
        bluetoothUseCase = new BluetoothUseCase(this);
        gpsUseCase = new GpsUseCase(this);
        sensorsUseCase = new SensorsUseCase(this);
    }

    @Override
    public void start() {
        if (view.get() != null) {
            ConfigDataModel configDataModel = view.get().prepareConfig();
            if (configDataModel.isCpu())
                cpuUseCase.start();
            if (configDataModel.isBluetooth())
                bluetoothUseCase.start();
            if (configDataModel.isGps())
                gpsUseCase.start();
            if (configDataModel.isSensors())
                sensorsUseCase.start();
        }
    }

    @Override
    public void stop() {
        cpuUseCase.stop();
        bluetoothUseCase.stop();
        gpsUseCase.stop();
        sensorsUseCase.stop();
    }

    @Override
    public void cpu(boolean start) {
        if (start)
            cpuUseCase.start();
        else
            cpuUseCase.stop();
    }

    @Override
    public void bluetooth(boolean start) {
        if (start)
            bluetoothUseCase.start();
        else
            bluetoothUseCase.stop();
    }

    @Override
    public void gps(boolean start) {
        if (start)
            gpsUseCase.start();
        else
            gpsUseCase.stop();
    }

    @Override
    public void sensors(boolean start) {
        if (start)
            sensorsUseCase.start();
        else
            sensorsUseCase.stop();
    }

    @Override
    public void onCpuCallback(Response response) {
        if (view.get() != null)
            view.get().onCpuResult(response);
    }

    @Override
    public void onBluetoothCallback(final Response response) {
        MainThread.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (view.get() != null)
                    view.get().onBluetoothResult(response);
            }
        });
    }

    @Override
    public void onGpsCallback(final Response response) {
        MainThread.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (view.get() != null)
                    view.get().onGpsResult(response);
            }
        });
    }

    @Override
    public void onSensorsCallback(Response response) {
        if (view.get() != null) {
            view.get().onSensorsResult(response);
        }
    }
}
