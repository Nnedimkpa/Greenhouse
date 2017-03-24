package com.nnedimkpa.greenhouse.control.manual;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nnedimkpa.greenhouse.R;
import com.nnedimkpa.greenhouse.databinding.FragmentManualBinding;
import com.nnedimkpa.greenhouse.model.ParseJSON;
import com.nnedimkpa.greenhouse.model.Reading;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualFragment extends Fragment implements View.OnClickListener,Response.ErrorListener, Response.Listener<String>, Runnable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private DatabaseReference reference;
    private final int MODE_AUTOMATIC = 1;
    private final int MODE_MANUAL = 0;
    private static final String THING_SPEAK_URL="https://api.thingspeak.com/channels/220794/feeds/last.json?api_key=MO4W3RQUZKP7B1OO";
    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private int plantData;
    private FragmentManualBinding binding;


    public ManualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param plantData Parameter 1.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManualFragment newInstance(int plantData) {
        ManualFragment fragment = new ManualFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, plantData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plantData = getArguments().getInt(ARG_PARAM1);
        }
        reference = FirebaseDatabase.getInstance().getReference("commands");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manual, container, false);
showProgressDialog();
        sendRequest();
        setMode(MODE_AUTOMATIC);

        binding.bulbOff.setOnClickListener(this);
        binding.bulbOn.setOnClickListener(this);
        binding.coolingFanOff.setOnClickListener(this);
        binding.coolingFanOn.setOnClickListener(this);
        binding.exhaustFanOff.setOnClickListener(this);
        binding.exhaustFanOn.setOnClickListener(this);
        binding.lightOff.setOnClickListener(this);
        binding.lightOn.setOnClickListener(this);
        binding.pumpOff.setOnClickListener(this);
        binding.pumpOn.setOnClickListener(this);
        binding.modeSwitch.setOnClickListener(this);
        return binding.getRoot();
    }

    private void setMode(int mode) {

        reference.child("automatic").setValue(mode);
        if (mode == MODE_AUTOMATIC)doAutoStuff();
        if (mode == MODE_MANUAL)doManualStuff();
    }

    @SuppressLint("SetTextI18n")
    private void doManualStuff() {
        binding.modeText.setText("Automatic Mode Off");
        binding.buttonsLayout.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void doAutoStuff() {
        binding.modeText.setText("Automatic Mode On");
        binding.buttonsLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bulbOff:
                setBulbState(0);
                break;
            case R.id.bulbOn:
                setBulbState(1);
                break;
            case R.id.coolingFanOff:
                setCoolingFanState(0);
                break;
            case R.id.coolingFanOn:
                setCoolingFanState(1);
                break;
            case R.id.exhaustFanOff:
                setExhaustFanState(0);
                break;
            case R.id.exhaustFanOn:
                setExhaustFanState(1);
                break;
            case R.id.lightOff:
                setLightState(0);
                break;
            case R.id.lightOn:
                setLightState(1);
                break;
            case R.id.pumpOff:
                setPumpState(0);
                break;
            case R.id.pumpOn:
                setPumpState(1);
                break;
            case R.id.modeSwitch:
                doStuffWithSwitch();
                break;
        }
    }

    private void doStuffWithSwitch() {
        if (binding.modeSwitch.isChecked()) {
            setMode(MODE_AUTOMATIC);
            return;
        }
        setMode(MODE_MANUAL);
    }

    private void setBulbState(int state) {
        reference.child("bulb").setValue(state);
    }

    private void setCoolingFanState(int state) {
        reference.child("coolingFan").setValue(state);
    }

    private void setExhaustFanState(int state) {
        reference.child("exhaustState").setValue(state);

    }

    private void setLightState(int state) {
        reference.child("light").setValue(state);
    }

    private void setPumpState(int state) {
        reference.child("pump").setValue(state);
    }

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, THING_SPEAK_URL, this, this);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void resendRequest(){
        final Handler handler = new Handler();
        handler.postDelayed(this, 15000);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
dismissProgressDialog();
        resendRequest();
    }

    @Override
    public void onResponse(String response) {
        dismissProgressDialog();
        ParseJSON parseJson = new ParseJSON();
        try {
            Reading reading = parseJson.readSingleObject(response);
            binding.lightText.setText("Light Level: " + reading.getLight() + "%");
            binding.waterText.setText("Water Level: " + reading.getWaterLevel() + "%");
            binding.humidityText.setText("Humidity: " + reading.getHumidity() + "%");
            binding.tempText.setText("Outer Temperature: " + reading.getInnerTemperature() + "C");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resendRequest();
    }

    @Override
    public void run() {
        StringRequest request = new StringRequest(Request.Method.GET, THING_SPEAK_URL, this, this);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading data");
        progressDialog.setMessage("Please wait, loading data from ThingSpeak");
        progressDialog.show();
    }

    private void dismissProgressDialog(){
        progressDialog.dismiss();
    }
}
