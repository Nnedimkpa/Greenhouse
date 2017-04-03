package com.nnedimkpa.greenhouse.control.commands;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nnedimkpa.greenhouse.model.GreenhouseSettings;

/**
 * Created by Digz on 03/04/2017.
 */

public class FirebaseControl implements CommandsContract, ValueEventListener {

    private DatabaseReference reference;
    private ValueListener listener;


    public FirebaseControl() {
        reference = FirebaseDatabase.getInstance().getReference("commands");
    }

    @Override
    public void setListener(ValueListener listener) {
        this.listener = listener;
        listener.onConnected();
        reference.addValueEventListener(this);
    }

    @Override
    public void setMode(int mode) {
        reference.child("automatic").setValue(mode);

    }

    @Override
    public void setPlant(int plant) {
        reference.child("plantData").setValue(plant);
    }

    @Override
    public void setCoolingFanState(int state) {
        reference.child("coolingFan").setValue(state);

    }

    @Override
    public void setBulbState(int state) {
        reference.child("bulb").setValue(state);

    }

    @Override
    public void setExhaustFanState(int state) {
        reference.child("exhaustFan").setValue(state);

    }

    @Override
    public void setLightState(int state) {
        reference.child("light").setValue(state);

    }

    @Override
    public void setPumpState(int state) {
        reference.child("pump").setValue(state);

    }

    @Override
    public void endConnection() {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        GreenhouseSettings settings = dataSnapshot.getValue(GreenhouseSettings.class);
        listener.onSettingsChanged(settings);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
