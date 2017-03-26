package com.nnedimkpa.greenhouse.control.manual;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nnedimkpa.greenhouse.R;
import com.nnedimkpa.greenhouse.databinding.FragmentManualBinding;
import com.nnedimkpa.greenhouse.model.GreenhouseSettings;
import com.nnedimkpa.greenhouse.model.ParseJSON;
import com.nnedimkpa.greenhouse.model.Reading;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<String>, Runnable, ValueEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final int MODE_AUTOMATIC = 1;
    private static final int MODE_MANUAL = 0;
    private static final int MODE_ON = 1;
    private static final int MODE_OFF = 0;
    private static final String THING_SPEAK_URL = "https://api.thingspeak.com/channels/220794/feeds/last.json?api_key=MO4W3RQUZKP7B1OO";
    final Handler handler = new Handler();
    private DatabaseReference reference;
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
        reference.child("plantData").setValue(plantData);
        reference.addValueEventListener(this);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(this);
    }

    private void setMode(int mode) {

        reference.child("automatic").setValue(mode);
        if (mode == MODE_AUTOMATIC) doAutoStuff();
        if (mode == MODE_MANUAL) doManualStuff();
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
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setBulbState(MODE_OFF);
                break;
            case R.id.bulbOn:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setBulbState(MODE_ON);
                break;
            case R.id.coolingFanOff:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setCoolingFanState(MODE_OFF);
                break;
            case R.id.coolingFanOn:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setCoolingFanState(MODE_ON);
                break;
            case R.id.exhaustFanOff:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setExhaustFanState(MODE_OFF);
                break;
            case R.id.exhaustFanOn:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setExhaustFanState(MODE_ON);
                break;
            case R.id.lightOff:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setLightState(MODE_OFF);
                break;
            case R.id.lightOn:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setLightState(MODE_ON);
                break;
            case R.id.pumpOff:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setPumpState(MODE_OFF);
                break;
            case R.id.pumpOn:
                Toast.makeText(getActivity(), "Working on it...", Toast.LENGTH_SHORT).show();
                setPumpState(MODE_ON);
                break;
            case R.id.modeSwitch:
                doStuffWithSwitch();
                break;
        }
    }

    private void updateUI(GreenhouseSettings settings) {
        binding.modeSwitch.setChecked(settings.getAutomatic());
        if (settings.getPump()) {
            binding.pumpOn.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.pumpOff.setBackgroundColor(Color.parseColor("#eeeeee"));
        } else {
            binding.pumpOff.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.pumpOn.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        if (settings.getBulb()) {
            binding.bulbOn.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.bulbOff.setBackgroundColor(Color.parseColor("#eeeeee"));

        } else {
            binding.bulbOff.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.bulbOn.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        if (settings.getCoolingFan()) {
            binding.coolingFanOn.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.coolingFanOff.setBackgroundColor(Color.parseColor("#eeeeee"));

        } else {
            binding.coolingFanOff.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.coolingFanOn.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        if (settings.getExhaustFan()) {
            binding.exhaustFanOn.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.exhaustFanOff.setBackgroundColor(Color.parseColor("#eeeeee"));
        } else {
            binding.exhaustFanOff.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.exhaustFanOn.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        if (settings.getLight()) {
            binding.lightOn.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.lightOff.setBackgroundColor(Color.parseColor("#eeeeee"));

        } else {
            binding.lightOff.setBackgroundColor(Color.parseColor("#8bc34a"));
            binding.lightOn.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        if (settings.getAutomatic()) {
            doAutoStuff();

        } else doManualStuff();
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
        reference.child("exhaustFan").setValue(state);

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

    private void resendRequest() {
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
        } catch (JSONException | NullPointerException e) {
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

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading data");
        progressDialog.setMessage("Please wait, loading data from ThingSpeak");
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        GreenhouseSettings settings = dataSnapshot.getValue(GreenhouseSettings.class);

        updateUI(settings);
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
