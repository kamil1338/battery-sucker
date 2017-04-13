package com.gmail.kamil1338.batterysucker.model;

/**
 * Created by kamilpierudzki on 05/04/2017.
 */

public enum Response {
    CPU_WORKING,
    CPU_NOT_WORKING,

    BT_DISABLED,
    BT_NOT_SUPPORTED,
    BT_NEED_START,
    BT_WORKING,

    GPS_DISABLED,
    GPS_NOT_SUPPORTED,
    GPS_NEED_START,
    GPS_WORKING,

    SENSORS_DISABLED,
    SENSORS_ENABLED,
    SENSORS_NOT_SUPPORTED
}
