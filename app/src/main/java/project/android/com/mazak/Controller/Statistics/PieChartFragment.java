package project.android.com.mazak.Controller.Statistics;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

public class PieChartFragment extends Fragment implements IRefresh {
    PieChart pie;
    Grade currentGrade;
    CourseStatistics current;
    View root;
    private String noDataText = "No Data Found";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.pie_chart_fragment, container, false);
        pie = (PieChart) root.findViewById(R.id.PieChart);
        currentGrade = (Grade) getArguments().getSerializable("grade");
        current = (CourseStatistics) getArguments().getSerializable("stats");
        setUpPiechart(pie,current.getCourseName(),current.getFreqs());
        return root;
    }

    /**
     * sets up all the pie chart details.
     * @param mChart
     * @param coursename
     * @param freqs
     */
    private void setUpPiechart(PieChart mChart,String coursename,ArrayList<Integer> freqs){

        //mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        //mChart.setDescription("");
        mChart.setNoDataText(noDataText);

        mChart.setCenterText(coursename);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);
        mChart.setCenterTextSize(20);
        mChart.setCenterTextColor(ColorTemplate.JOYFUL_COLORS[1]);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setData(getDataPie(freqs,coursename));
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
        mChart.animateXY(1000,1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        pie.animateXY(1000,1000);
    }

    /**
     * gets the data from the rawData.
     * @param freqs
     * @param coursename
     * @return
     */
    private PieData getDataPie(ArrayList<Integer> freqs, String coursename) {
        PieData data = new PieData();
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> currentColors = new ArrayList<>();
        int end = 59;
        int start = 0;
        for (int i = 0; i < 9; i++) {
            if(freqs.get(i) != 0) {
                currentColors.add(CourseStatisticsActivity.colors[i]);
                entries.add(new PieEntry(freqs.get(i), start + "-" + end));
                labels.add(start + "-" + end);
/*                entries.add(new BarEntry(end, freqs.get(i), start + "-" + end));
                labels.add(start + "-" + end);*/
            }
            start = end + 1;
            end = start + 4;
            if (i == 7)
                end++;
        }
        PieDataSet set = new PieDataSet(entries,coursename);
        set.setColors(currentColors);

        set.setSliceSpace(3f);
        set.setSelectionShift(5f);

        set.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return String.valueOf((int) value);
//            }

            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);

//                return super.getFormattedValue(value);
            }
        });

        //data.setValueTypeface(Typeface.SANS_SERIF);
        data.addDataSet(set);

        return data;
    }

    @Override
    public void Refresh() {

    }

}
