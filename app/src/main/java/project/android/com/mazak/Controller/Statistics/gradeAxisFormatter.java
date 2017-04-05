package project.android.com.mazak.Controller.Statistics;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Yair on 2017-04-04.
 */

public class gradeAxisFormatter implements IAxisValueFormatter {
    private final BarChart mChart;

    public gradeAxisFormatter(BarChart mChart) {
        this.mChart = mChart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int val = (int) value;
        if(val == 0)
            return "0-59";
        if(val == 1)
            return "60-64";
        if(val == 2)
            return "65-69";
        if(val == 3)
            return "70-74";
        if(val == 4)
            return "75-79";
        if(val == 5)
            return "80-84";
        if(val == 6)
            return "85-89";
        if(val == 7)
            return "90-94";
        else
            return "95-100";
    }
}
