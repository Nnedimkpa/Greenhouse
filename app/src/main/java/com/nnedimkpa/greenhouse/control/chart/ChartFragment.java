package com.nnedimkpa.greenhouse.control.chart;


import android.os.Bundle;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.nnedimkpa.greenhouse.R;
import com.nnedimkpa.greenhouse.model.ParseJSON;
import com.nnedimkpa.greenhouse.model.Reading;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int plantData;
    private ArrayList<Reading> readings;
    private List<BarEntry> tempEntry = new ArrayList<>(), humidityEntry= new ArrayList<>(), lightEntry= new ArrayList<>(), waterEntry= new ArrayList<>();
    private BarChart tempChart, humidityChart, lightChart, waterChart;

    public ChartFragment() {
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
    public static ChartFragment newInstance(int plantData) {
        ChartFragment fragment = new ChartFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    private void SendRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, this, this);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        ParseJSON parseJSON = new ParseJSON();
        try {
            readings = parseJSON.readJson(response);
            convertReadingToEntry();
            plotGraphs();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void plotGraphs() {
        plotTempGraph();
        plotLightGraph();
        plotWaterGraph();
        plotHumidityGraph();
    }

    private void plotTempGraph() {
        BarDataSet dataSet = new BarDataSet(tempEntry, "");
        BarData barData = new BarData(dataSet);
        tempChart.setData(barData);
        tempChart.invalidate();
    }

    private void plotHumidityGraph() {
        BarDataSet dataSet = new BarDataSet(humidityEntry, "");
        BarData barData = new BarData(dataSet);
        humidityChart.setData(barData);
        humidityChart.invalidate();
    }

    private void plotLightGraph() {
        BarDataSet dataSet = new BarDataSet(lightEntry, "");
        BarData barData = new BarData(dataSet);
        lightChart.setData(barData);
        lightChart.invalidate();
    }

    private void plotWaterGraph() {
        BarDataSet dataSet = new BarDataSet(waterEntry, "");
        BarData barData = new BarData(dataSet);
        waterChart.setData(barData);
        waterChart.invalidate();
    }

    private void convertReadingToEntry() {
        for (Reading reading: readings){
            tempEntry.add(new BarEntry(reading.getDate(), reading.getInnerTemperature()));
            humidityEntry.add(new BarEntry(reading.getDate(), reading.getHumidity()));
            lightEntry.add(new BarEntry(reading.getDate(), reading.getLight()));
            waterEntry.add(new BarEntry(reading.getDate(), reading.getWaterLevel()));
        }


    }
}
