package com.gmail.kamil1338.batterysucker.usecase;

import com.gmail.kamil1338.batterysucker.BasePresenter;

/**
 * Created by kamilpierudzki on 30/03/2017.
 */

public interface BaseUseCase extends BasePresenter {

    interface UseCaseCallback<T> {
        void onCpuCallback(T response);
        void onBluetoothCallback(T response);
        void onGpsCallback(T response);
        void onSensorsCallback(T response);
    }
}
