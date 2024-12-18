package project.android.com.mazak.Controller.Statistics;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by Yair on 2017-04-04.
 * This class is for the X axis of the bar chart.
 */

public class gradeAxisFormatter extends ValueFormatter {
    private final BarChart mChart;

    public gradeAxisFormatter(BarChart mChart) {
        this.mChart = mChart;
    }

    //switch statement should be faster
    @Override
    public String getFormattedValue(float value) {
        int val = (int) value;
        switch (val) {
            case 0:
                return "0-59";
            case 1:
                return "60-64";
            case 2:
                return "65-69";
            case 3:
                return "70-74";
            case 4:
                return "75-79";
            case 5:
                return "80-84";
            case 6:
                return "85-89";
            case 7:
                return "90-94";
            default:
                return "95-100";
        }
    }
}
