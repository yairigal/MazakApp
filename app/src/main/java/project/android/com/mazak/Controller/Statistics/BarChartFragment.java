package project.android.com.mazak.Controller.Statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;


public class BarChartFragment extends Fragment implements IRefresh {
    View root;
    BarChart bar;
    //Grade currentGrade;
    CourseStatistics current;
    private String noDataText = "No Data Found";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.bar_chart_fragment, container, false);
        bar = (BarChart) root.findViewById(R.id.BarChart);
        //currentGrade = (Grade) getArguments().getSerializable("grade");
        current = (CourseStatistics) getArguments().getSerializable("stats");
        setUpBarchart(bar,current.getCourseName(),current.getFreqs());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        bar.animateXY(1000,1000);
    }

    /**
     * sets up the barchart details
     * @param mChart
     * @param coursename
     * @param freqs
     */
    private void setUpBarchart(BarChart mChart, String coursename, ArrayList<Integer> freqs){
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setNoDataText(noDataText);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new gradeAxisFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        //IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(getContext(), new DefaultAxisValueFormatter(3));
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        mChart.setData(getData(freqs,coursename));
        mChart.animateXY(1000,1000);
        //setData(12, 50);
    }

    /**
     * gets the data from the raw data.
     * @param freqs
     * @param coursename
     * @return
     */
    private BarData getData(ArrayList<Integer> freqs, String coursename) {
        BarData data = new BarData();
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> currentColors = new ArrayList<>();
        int end = 59;
        int start = 0;
        for (int i = 0; i < 9; i++) {
            currentColors.add(CourseStatisticsActivity.colors[i]);
            entries.add(new BarEntry(i, freqs.get(i), start + "-" + end));
            labels.add(start + "-" + end);
/*                entries.add(new BarEntry(end, freqs.get(i), start + "-" + end));
                labels.add(start + "-" + end);*/
            start = end + 1;
            end = start + 4;
            if (i == 7)
                end++;
        }
        BarDataSet set = new BarDataSet(entries,coursename);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                float percentage = value/current.getNumOfStudentsWithGrade() * 100;
                DecimalFormat format = new DecimalFormat("##.#");
                return String.valueOf(format.format(percentage)) + "%";
            }
        });
        set.setColors(currentColors);
        data.addDataSet(set);
        return data;
    }

    @Override
    public void Refresh() {

    }

}
