package com.gmail.kamil1338.batterysucker;

import com.gmail.kamil1338.batterysucker.model.ConfigDataModel;

/**
 * Created by kamilpierudzki on 30/03/2017.
 */

public interface Contract {

    interface View<T> extends BaseView<Presenter> {
        ConfigDataModel prepareConfig();

        void onCpuResult(T response);

        void onBluetoothResult(T response);

        void onGpsResult(T response);

        void onSensorsResult(T response);
    }

    interface Presenter extends BasePresenter {
        void cpu(boolean start);

        void bluetooth(boolean start);

        void gps(boolean start);

        void sensors(boolean start);
    }
}
