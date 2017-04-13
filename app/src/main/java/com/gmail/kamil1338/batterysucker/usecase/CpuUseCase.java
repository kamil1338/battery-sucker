package com.gmail.kamil1338.batterysucker.usecase;

import android.support.annotation.NonNull;

import com.gmail.kamil1338.batterysucker.model.Response;

import java.lang.ref.WeakReference;

/**
 * Created by kamilpierudzki on 30/03/2017.
 */

public class CpuUseCase implements BaseUseCase {

    private WeakReference<UseCaseCallback<Response>> callback;

    private volatile boolean run = false;

    private final int DATA_SIZE = 64;

    private double[] data;

    public CpuUseCase(@NonNull UseCaseCallback<Response> callback) {
        this.callback = new WeakReference<>(callback);

        data = new double[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; i++)
            data[i] = 1f;
    }

    @Override
    public void start() {
        run = true;

        int THREAD_NUMBER = 4;
        for (int i = 0; i < THREAD_NUMBER; i++)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (run)
                        for (int i = 0; i < DATA_SIZE && run; i++)
                            data[i] = data[i] * -1f;//false sharing
                }
            }).start();

        if (callback.get() != null)
            callback.get().onCpuCallback(Response.CPU_WORKING);
    }

    @Override
    public void stop() {
        if (run && callback.get() != null) {
            callback.get().onCpuCallback(Response.CPU_NOT_WORKING);
        }
        run = false;
    }

}
