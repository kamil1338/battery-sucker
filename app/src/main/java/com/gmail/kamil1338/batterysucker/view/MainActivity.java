package com.gmail.kamil1338.batterysucker.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;

import com.gmail.kamil1338.batterysucker.Contract;
import com.gmail.kamil1338.batterysucker.R;
import com.gmail.kamil1338.batterysucker.model.ConfigDataModel;
import com.gmail.kamil1338.batterysucker.model.Response;
import com.gmail.kamil1338.batterysucker.presenter.BatterySuckerPresenter;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        Contract.View<Response>, DialogInterface.OnDismissListener {

    private Contract.Presenter presenter;

    private final int BLUETOOTH_REQUEST = 123;
    private final int ACCESS_FINE_LOCATION_REQUEST = 125;

    private final String CONFIG_EXTRA = "config_extra";
    private ConfigDataModel config;

    private SwitchCompat cpuSwitch, bluetoothSwitch, gpsSwitch, sensorsSwitch, backCameraSwitch, frontCameraSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cpuSwitch = (SwitchCompat) findViewById(R.id.switch_cpu);
        cpuSwitch.setOnCheckedChangeListener(this);

        bluetoothSwitch = (SwitchCompat) findViewById(R.id.switch_bluetooth);
        bluetoothSwitch.setOnCheckedChangeListener(this);

        gpsSwitch = (SwitchCompat) findViewById(R.id.switch_gps);
        gpsSwitch.setOnCheckedChangeListener(this);

        sensorsSwitch = (SwitchCompat) findViewById(R.id.switch_sensors);
        sensorsSwitch.setOnCheckedChangeListener(this);

        if (savedInstanceState != null) {
            config = savedInstanceState.getParcelable(CONFIG_EXTRA);
        } else {
            config = new ConfigDataModel();
        }

        new BatterySuckerPresenter(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_cpu:
                config.setCpu(isChecked);
                presenter.cpu(isChecked);
                break;
            case R.id.switch_bluetooth:
                config.setBluetooth(isChecked);
                presenter.bluetooth(isChecked);
                break;
            case R.id.switch_gps:
                config.setGps(isChecked);
                presenter.gps(isChecked);
                break;
            case R.id.switch_sensors:
                config.setSensors(isChecked);
                presenter.sensors(isChecked);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public ConfigDataModel prepareConfig() {
        return config;
    }

    @Override
    public void onCpuResult(Response response) {
        switch (response) {
            case CPU_WORKING:
                cpuSwitch.setOnCheckedChangeListener(null);
                cpuSwitch.setChecked(true);
                cpuSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_cpu_positive, Snackbar.LENGTH_SHORT).show();
                break;
            case CPU_NOT_WORKING:
                cpuSwitch.setOnCheckedChangeListener(null);
                cpuSwitch.setChecked(false);
                cpuSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_cpu_negative, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBluetoothResult(Response response) {
        switch (response) {
            case BT_NEED_START:
                bluetoothSwitch.setOnCheckedChangeListener(null);
                bluetoothSwitch.setChecked(false);
                bluetoothSwitch.setOnCheckedChangeListener(this);

                switch (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    case PackageManager.PERMISSION_GRANTED:
                        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BLUETOOTH_REQUEST);
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BLUETOOTH_REQUEST);
                        break;
                }
                break;
            case BT_DISABLED:
                bluetoothSwitch.setOnCheckedChangeListener(null);
                bluetoothSwitch.setChecked(false);
                bluetoothSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_bluetooth_disabled, Snackbar.LENGTH_SHORT).show();
                break;
            case BT_WORKING:
                bluetoothSwitch.setOnCheckedChangeListener(null);
                bluetoothSwitch.setChecked(true);
                bluetoothSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_bluetooth_enabled, Snackbar.LENGTH_SHORT).show();
                break;
            case BT_NOT_SUPPORTED:
                bluetoothSwitch.setOnCheckedChangeListener(null);
                bluetoothSwitch.setChecked(false);
                bluetoothSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_bluetooth_not_supported, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onGpsResult(Response response) {
        switch (response) {
            case GPS_WORKING:
                gpsSwitch.setOnCheckedChangeListener(null);
                gpsSwitch.setChecked(true);
                gpsSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_gps_enabled, Snackbar.LENGTH_SHORT).show();
                break;
            case GPS_DISABLED:
                gpsSwitch.setOnCheckedChangeListener(null);
                gpsSwitch.setChecked(false);
                gpsSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_gps_disabled, Snackbar.LENGTH_SHORT).show();
                break;
            case GPS_NOT_SUPPORTED:
                gpsSwitch.setOnCheckedChangeListener(null);
                gpsSwitch.setChecked(false);
                gpsSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_gps_not_supported, Snackbar.LENGTH_SHORT).show();
                break;
            case GPS_NEED_START:
                gpsSwitch.setOnCheckedChangeListener(null);
                gpsSwitch.setChecked(false);
                gpsSwitch.setOnCheckedChangeListener(this);

                switch (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    case PackageManager.PERMISSION_GRANTED:
                        new GpsDialog().setListener(this).show(getFragmentManager(), "");
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST);
                        break;
                }
                break;
        }
    }

    @Override
    public void onSensorsResult(Response response) {
        switch (response) {
            case SENSORS_DISABLED:
                sensorsSwitch.setOnCheckedChangeListener(null);
                sensorsSwitch.setChecked(false);
                sensorsSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_sensors_disabled, Snackbar.LENGTH_SHORT).show();
                break;
            case SENSORS_ENABLED:
                sensorsSwitch.setOnCheckedChangeListener(null);
                sensorsSwitch.setChecked(true);
                sensorsSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_sensors_enabled, Snackbar.LENGTH_SHORT).show();
                break;
            case SENSORS_NOT_SUPPORTED:
                sensorsSwitch.setOnCheckedChangeListener(null);
                sensorsSwitch.setChecked(false);
                sensorsSwitch.setOnCheckedChangeListener(this);
                Snackbar.make(findViewById(android.R.id.content), R.string.result_sensors_not_supported, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void setPresenter(@NonNull Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CONFIG_EXTRA, config);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_REQUEST) {
            if (resultCode == RESULT_OK) {
                presenter.bluetooth(true);
            } else {
                config.setBluetooth(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST) {
            presenter.gps(true);
        } else if (requestCode == BLUETOOTH_REQUEST) {
            presenter.bluetooth(true);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        config.setGps(false);
    }
}
