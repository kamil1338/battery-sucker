package com.gmail.kamil1338.batterysucker;

import android.support.annotation.NonNull;

/**
 * Created by kamilpierudzki on 30/03/2017.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(@NonNull T presenter);
}
