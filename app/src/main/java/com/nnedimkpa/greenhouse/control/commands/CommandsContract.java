package com.nnedimkpa.greenhouse.control.commands;

import com.nnedimkpa.greenhouse.model.GreenhouseSettings;

/**
 * Created by Digz on 03/04/2017.
 */

public interface CommandsContract {

    void setMode(int mode);
    void setListener(ValueListener listener);
    void setPlant(int plant);
    void setCoolingFanState(int state);
    void setBulbState(int state);
    void setExhaustFanState(int state);
    void setLightState(int state);
    void setPumpState(int state);
    void endConnection();


    interface ValueListener {
        void onConnected();
        void onDisconnected();
        void onConnectionFailed();
        void onSettingsChanged(GreenhouseSettings settings);
    }
}
