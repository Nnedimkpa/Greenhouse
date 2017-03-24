package com.nnedimkpa.greenhouse.control.chart;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.nnedimkpa.greenhouse.R;
import com.nnedimkpa.greenhouse.control.ControlActivity;
import com.nnedimkpa.greenhouse.control.chart.formatter.AxisFormatter;
import com.nnedimkpa.greenhouse.model.Reading;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private int plantData;
    private List<BarEntry> tempEntry = new ArrayList<>(), humidityEntry = new ArrayList<>(), lightEntry = new ArrayList<>(), waterEntry = new ArrayList<>();
    private BarChart tempChart, humidityChart, lightChart, waterChart;
    private String[] xAxisVals;
    private ControlActivity activity;

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
        activity = (ControlActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        tempChart = (BarChart) view.findViewById(R.id.tempChart);
        lightChart = (BarChart) view.findViewById(R.id.lightChart);
        humidityChart = (BarChart) view.findViewById(R.id.humidityChart);
        waterChart = (BarChart) view.findViewById(R.id.waterChart);
        showProgressDialog();
        activity.sendRequest();
        return view;
    }


    public void plotGraphs() {
        plotTempGraph();
        plotLightGraph();
        plotWaterGraph();
        plotHumidityGraph();
    }

    private void plotTempGraph() {
        BarDataSet dataSet = new BarDataSet(tempEntry, "");
        BarData barData = new BarData(dataSet);
        tempChart.setData(barData);
        XAxis xAxis = tempChart.getXAxis();
        xAxis.setValueFormatter(new AxisFormatter(xAxisVals));
        tempChart.invalidate();

    }

    private void plotHumidityGraph() {
        BarDataSet dataSet = new BarDataSet(humidityEntry, "");
        BarData barData = new BarData(dataSet);
        humidityChart.setData(barData);
        XAxis xAxis = humidityChart.getXAxis();
        xAxis.setValueFormatter(new AxisFormatter(xAxisVals));
        humidityChart.invalidate();

    }

    private void plotLightGraph() {
        BarDataSet dataSet = new BarDataSet(lightEntry, "");
        BarData barData = new BarData(dataSet);
        lightChart.setData(barData);
        XAxis xAxis = lightChart.getXAxis();
        xAxis.setValueFormatter(new AxisFormatter(xAxisVals));
        lightChart.invalidate();
    }

    private void plotWaterGraph() {
        BarDataSet dataSet = new BarDataSet(waterEntry, "");
        BarData barData = new BarData(dataSet);
        waterChart.setData(barData);
        XAxis xAxis = waterChart.getXAxis();
        xAxis.setValueFormatter(new AxisFormatter(xAxisVals));
        waterChart.invalidate();
    }

    public void convertReadingToEntry(ArrayList<Reading> readings) {
        xAxisVals = new String[readings.size()];
        int i = 0;
        for (Reading reading : readings) {
            tempEntry.add(new BarEntry(reading.getId(), reading.getInnerTemperature()));
            humidityEntry.add(new BarEntry(reading.getId(), reading.getHumidity()));
            lightEntry.add(new BarEntry(reading.getId(), reading.getLight()));
            waterEntry.add(new BarEntry(reading.getId(), reading.getWaterLevel()));
            xAxisVals[i] = reading.getDate();
            i++;
        }


    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading data");
        progressDialog.setMessage("Please wait, loading data from ThingSpeak");
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void showError(){
        Toast.makeText(getActivity(), "There was error loading data", Toast.LENGTH_SHORT).show();
    }
}
