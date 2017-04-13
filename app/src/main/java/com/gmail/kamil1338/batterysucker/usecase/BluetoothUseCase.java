package com.gmail.kamil1338.batterysucker.usecase;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;

import com.gmail.kamil1338.batterysucker.App;
import com.gmail.kamil1338.batterysucker.model.Response;

import java.lang.ref.WeakReference;

/**
 * Created by kamilpierudzki on 03/04/2017.
 */

public class BluetoothUseCase implements BaseUseCase {

    private WeakReference<UseCaseCallback<Response>> callback;

    private BroadcastReceiver broadcastReceiver;
    private BluetoothAdapter bluetoothAdapter;
    private Handler backgroundHandler;

    public BluetoothUseCase(@NonNull final UseCaseCallback<Response> callback) {
        this.callback = new WeakReference<>(callback);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                    if (bluetoothAdapter != null && bluetoothAdapter.isEnabled() && backgroundHandler != null) {
                        backgroundHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                bluetoothAdapter.startDiscovery();
                            }
                        });
                    } else if (BluetoothUseCase.this.callback.get() != null) {
                        BluetoothUseCase.this.callback.get().onBluetoothCallback(Response.BT_DISABLED);
                    }
                }
            }
        };
    }

    @Override
    public void start() {
        HandlerThread handlerThread = new HandlerThread("bluetooth_use_case", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                App.getContext().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.startDiscovery();
                        if (callback.get() != null)
                            callback.get().onBluetoothCallback(Response.BT_WORKING);
                    }
                });
            } else if (callback.get() != null) {
                callback.get().onBluetoothCallback(Response.BT_NEED_START);
            }
        } else if (callback.get() != null) {
            callback.get().onBluetoothCallback(Response.BT_NOT_SUPPORTED);
        }
    }

    @Override
    public void stop() {
        try {
            App.getContext().unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException exception) {
            /** Not implemented*/
        }

        if (backgroundHandler != null) {
            backgroundHandler.removeCallbacksAndMessages(null);
            backgroundHandler.getLooper().quitSafely();
            backgroundHandler = null;
        }

        if (bluetoothAdapter != null)
            bluetoothAdapter.cancelDiscovery();
    }
}
