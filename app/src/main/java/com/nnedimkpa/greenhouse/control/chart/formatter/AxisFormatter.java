package com.nnedimkpa.greenhouse.control.chart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Nnedimkpa on 3/24/2017.
 */

public class AxisFormatter implements IAxisValueFormatter{
    private String[] values;

    public AxisFormatter(String[] values){
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values[(int) value];
    }
}
