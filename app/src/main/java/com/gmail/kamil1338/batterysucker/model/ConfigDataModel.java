package com.gmail.kamil1338.batterysucker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kamilpierudzki on 30/03/2017.
 */

public class ConfigDataModel implements Parcelable {

    private boolean cpu;
    private boolean bluetooth;
    private boolean gps;
    private boolean sensors;
    private boolean backCamera;
    private boolean frontCamera;

    public ConfigDataModel() {
        this.cpu = false;
        this.bluetooth = false;
        this.gps = false;
        this.sensors = false;
        this.backCamera = false;
        this.frontCamera = false;
    }

    public boolean isCpu() {
        return cpu;
    }

    public void setCpu(boolean cpu) {
        this.cpu = cpu;
    }

    public boolean isBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public boolean isSensors() {
        return sensors;
    }

    public void setSensors(boolean sensors) {
        this.sensors = sensors;
    }

    public boolean isBackCamera() {
        return backCamera;
    }

    public void setBackCamera(boolean backCamera) {
        this.backCamera = backCamera;
    }

    public boolean isFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(boolean frontCamera) {
        this.frontCamera = frontCamera;
    }

    @Override
    public String toString() {
        return "ConfigDataModel{" +
                "cpu=" + cpu +
                ", bluetooth=" + bluetooth +
                ", gps=" + gps +
                ", sensors=" + sensors +
                ", backCamera=" + backCamera +
                ", frontCamera=" + frontCamera +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.cpu ? (byte) 1 : (byte) 0);
        dest.writeByte(this.bluetooth ? (byte) 1 : (byte) 0);
        dest.writeByte(this.gps ? (byte) 1 : (byte) 0);
        dest.writeByte(this.sensors ? (byte) 1 : (byte) 0);
        dest.writeByte(this.backCamera ? (byte) 1 : (byte) 0);
        dest.writeByte(this.frontCamera ? (byte) 1 : (byte) 0);
    }

    protected ConfigDataModel(Parcel in) {
        this.cpu = in.readByte() != 0;
        this.bluetooth = in.readByte() != 0;
        this.gps = in.readByte() != 0;
        this.sensors = in.readByte() != 0;
        this.backCamera = in.readByte() != 0;
        this.frontCamera = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ConfigDataModel> CREATOR = new Parcelable.Creator<ConfigDataModel>() {
        @Override
        public ConfigDataModel createFromParcel(Parcel source) {
            return new ConfigDataModel(source);
        }

        @Override
        public ConfigDataModel[] newArray(int size) {
            return new ConfigDataModel[size];
        }
    };
}
