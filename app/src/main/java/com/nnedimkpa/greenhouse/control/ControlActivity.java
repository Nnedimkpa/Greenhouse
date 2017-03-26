package com.nnedimkpa.greenhouse.control;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.nnedimkpa.greenhouse.MainActivity;
import com.nnedimkpa.greenhouse.R;
import com.nnedimkpa.greenhouse.control.chart.ChartFragment;
import com.nnedimkpa.greenhouse.control.manual.ManualFragment;
import com.nnedimkpa.greenhouse.databinding.ActivityControlBinding;
import com.nnedimkpa.greenhouse.model.ParseJSON;
import com.nnedimkpa.greenhouse.model.Reading;

import org.json.JSONException;

import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Response.ErrorListener, Response.Listener<String>{
    private ActivityControlBinding binding;
    private ArrayList<Reading> readings;

    private Fragment chartFragment, controlFragment;
    private FragmentManager fragmentManager;
    private int plantData;
    private static final String THING_SPEAK_URL = "https://api.thingspeak.com/channels/220794/feeds.json?results=10&api_key=MO4W3RQUZKP7B1OO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_control);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously();
         plantData = getIntent().getIntExtra("plantData", 99);
        fragmentManager = getSupportFragmentManager();
        switchFragment(getControlFragment(plantData),  "control");
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private void switchFragment(Fragment fragment, String name) {
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();
    }

    private Fragment getChartFragment(int plantData) {
        if (chartFragment == null) chartFragment = ChartFragment.newInstance(plantData);
        return chartFragment;
    }

    private Fragment getControlFragment(int plantData) {
        if (controlFragment == null) controlFragment = ManualFragment.newInstance(plantData);
        return controlFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_charts:

                switchFragment(getChartFragment(plantData), "chart");

                break;
            case R.id.nav_control:
                switchFragment(getControlFragment(plantData), "control");

                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void sendRequest() {
        if (readings != null) {
            ChartFragment chartFragment = (ChartFragment)this.chartFragment;
            chartFragment.dismissProgressDialog();
            chartFragment.convertReadingToEntry(readings);
            chartFragment.plotGraphs();

            return;
        }
        StringRequest request = new StringRequest(Request.Method.GET, THING_SPEAK_URL, this, this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void refreshCharts(){
        readings = null;
        sendRequest();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("NnediGreen", error.getMessage());
        ChartFragment chartFragment = (ChartFragment)this.chartFragment;
        chartFragment.dismissProgressDialog();
        chartFragment.showError();

    }

    @Override
    public void onResponse(String response) {
        Log.d("NnediGreen", response);
        ParseJSON parseJSON = new ParseJSON();
        ChartFragment chartFragment = (ChartFragment)this.chartFragment;
        chartFragment.dismissProgressDialog();

        try {
            readings = parseJSON.readJson(response);

            chartFragment.convertReadingToEntry(readings);
            chartFragment.plotGraphs();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
