package com.nnedimkpa.greenhouse.control.commands;

import android.content.Context;

import com.nnedimkpa.greenhouse.CONST;
import com.nnedimkpa.greenhouse.model.GreenhouseSettings;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;


public class BluetoothControl implements CommandsContract, BluetoothSPP.BluetoothConnectionListener {

    private BluetoothSPP bluetoothSPP;

    private ValueListener listener;
    private GreenhouseSettings settings;

    public BluetoothControl(Context context) {
        settings = GreenhouseSettings.init();
        bluetoothSPP = new BluetoothSPP(context);
        bluetoothSPP.setupService();
        bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
        bluetoothSPP.setBluetoothConnectionListener(this);
        bluetoothSPP.connect("");
    }

    @Override
    public void setMode(int mode) {
        switch (mode) {
            case 0:
                bluetoothSPP.send("AUOFF", true);

                break;
            case 1:
                bluetoothSPP.send("AUON", true);
                break;
        }
    }

    @Override
    public void setListener(ValueListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPlant(int plant) {
        switch (plant) {
            case CONST.PEPPER:
                bluetoothSPP.send("PE", true);
                break;
            case CONST.TOMATO:
                bluetoothSPP.send("TO", true);
                break;
            case CONST.BEANS:
                bluetoothSPP.send("BE", true);
                break;
        }
    }

    @Override
    public void setCoolingFanState(int state) {
        settings.setCoolingFan(state);
        switch (state) {
            case 0:
                bluetoothSPP.send("CFOFF", true);
                break;
            case 1:
                bluetoothSPP.send("CFON", true);
                break;
        }
        listener.onSettingsChanged(settings);
    }

    @Override
    public void setBulbState(int state) {
        settings.setBulb(state);
        switch (state) {
            case 0:
                bluetoothSPP.send("HBOFF", true);
                break;
            case 1:
                bluetoothSPP.send("HBON", true);
                break;
        }
        listener.onSettingsChanged(settings);
    }

    @Override
    public void setExhaustFanState(int state) {
        settings.setExhaustFan(state);
        switch (state) {
            case 0:
                bluetoothSPP.send("EFOFF", true);
                break;
            case 1:
                bluetoothSPP.send("EFON", true);
                break;
        }
        listener.onSettingsChanged(settings);
    }

    @Override
    public void setLightState(int state) {
        settings.setLight(state);
        switch (state) {
            case 0:
                bluetoothSPP.send("GLOFF", true);
                break;
            case 1:
                bluetoothSPP.send("GLON", true);
                break;
        }
        listener.onSettingsChanged(settings);
    }

    @Override
    public void setPumpState(int state) {
        settings.setPump(state);
        switch (state) {
            case 0:
                bluetoothSPP.send("WPOFF", true);
                break;
            case 1:
                bluetoothSPP.send("WPON", true);
                break;
        }
        listener.onSettingsChanged(settings);
    }

    @Override
    public void endConnection() {
        bluetoothSPP.disconnect();
        bluetoothSPP.stopService();
    }

    @Override
    public void onDeviceConnected(String name, String address) {
        listener.onConnected();
    }

    @Override
    public void onDeviceDisconnected() {
        listener.onDisconnected();
    }

    @Override
    public void onDeviceConnectionFailed() {
        listener.onConnectionFailed();
    }
}
