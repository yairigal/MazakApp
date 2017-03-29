package project.android.com.mazak.Controller;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.R;

public class CourseStatisticsActivity extends AppCompatActivity implements OnChartValueSelectedListener{

    Integer[] colors = new Integer[]{
            ColorTemplate.rgb("D32F2F"), // red
            ColorTemplate.rgb("FF5722"), // red
            ColorTemplate.rgb("F57C00"), // orange
            ColorTemplate.rgb("FF9800"), // orange
            ColorTemplate.rgb("FFC107"), // yellow
            ColorTemplate.rgb("00BCD4"), // bright blue
            ColorTemplate.rgb("1976D2"), // blue
            ColorTemplate.rgb("689F38"), // bright green
            ColorTemplate.rgb("4CAF50")}; // green
    String noDataText = "No Data Found";
    CourseStatistics current;
    Grade currentGrade;
    Database db;
    RadioButton PieBtn;
    RadioButton BarBtn;
    PieChart pie;
    BarChart bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_statistics);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        pie = (PieChart) findViewById(R.id.PieChart);
        bar = (BarChart) findViewById(R.id.BarChart);

        getGradeFromIntent();
        getFactoryDatabase();
        getStatisticsAsync(new Delegate() {
            @Override
            public void function(Object obj) {
                onFinishedLoading();
            }
        });

        PieBtn = (RadioButton) findViewById(R.id.PieChartRadioBtn);
        BarBtn = (RadioButton) findViewById(R.id.BarChartRadioBtn);

        BarBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PieBtn.setChecked(false);
                    bar.setVisibility(View.VISIBLE);
                    pie.setVisibility(View.GONE);
                    setUpBarchart(bar,current.getCourseName(),current.getFreqs());
                }
            }
        });

        PieBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    BarBtn.setChecked(false);
                    pie.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                    setUpPiechart(pie,current.getCourseName(),current.getFreqs());
                }
            }
        });

    }

    private void getStatisticsAsync(final Delegate onFinishLoading) {
        new AsyncTask<Void, Void, Void>() {
            public boolean error = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                findViewById(R.id.StatsProgressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.layoutChart).setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    current = db.getStatsFromWeb(currentGrade.StatLink);
                    current.setCourseName(currentGrade.name);
                } catch (Exception e) {
                    error = true;
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (error) {
                    Toast.makeText(getApplicationContext(), "Error reaching statistics", Toast.LENGTH_LONG).show();
                    return;
                }
                onFinishLoading.function(null);
                //setUpBarchart(bar, current.getCourseName(), current.getFreqs());
            }
        }.execute();
    }

    private void onFinishedLoading(){
        findViewById(R.id.StatsProgressBar).setVisibility(View.GONE);
        findViewById(R.id.layoutChart).setVisibility(View.VISIBLE);

        TextView num = (TextView)findViewById(R.id.numOfStud);
        TextView avg = (TextView)findViewById(R.id.mean);
        TextView med = (TextView)findViewById(R.id.Median);

        TextView grade = (TextView)findViewById(R.id.GradeTV);
        TextView year = (TextView)findViewById(R.id.YearTV);
        TextView sem = (TextView)findViewById(R.id.SemesterTV);

        num.setText(num.getText().toString() + " "+ current.getNumOfStudentsWithGrade());
        avg.setText(avg.getText().toString() + " "+ current.getMean());
        med.setText(med.getText().toString() + " "+ (int)current.getMedian());

        grade.setText(grade.getText()+" "+currentGrade.finalGrade);
        year.setText(year.getText()+" "+currentGrade.code.substring(currentGrade.code.length()-4,currentGrade.code.length()));
        sem.setText(sem.getText()+" "+currentGrade.semester);

        BarBtn.setChecked(true);
    }

    private void getGradeFromIntent() {
        currentGrade = (Grade) getIntent().getSerializableExtra("grade");
    }

    private void getFactoryDatabase() {
        try {
            db = Factory.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpPiechart(PieChart mChart,String coursename,ArrayList<Integer> freqs){

        //mChart.setUsePercentValues(true);
        //mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

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
        mChart.setOnChartValueSelectedListener(this);

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

    private void setUpBarchart(BarChart mChart,String coursename,ArrayList<Integer> freqs){
        mChart.setOnChartValueSelectedListener(this);

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

        IAxisValueFormatter xAxisFormatter = new gradesAxisFormatter(mChart);

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

        XYMarkerView mv = new XYMarkerView(this, new DefaultAxisValueFormatter(3));
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        mChart.setData(getData(freqs,coursename));
        mChart.animateXY(1000,1000);
        //setData(12, 50);
    }

    private BarData getData(ArrayList<Integer> freqs,String coursename) {
        BarData data = new BarData();
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> currentColors = new ArrayList<>();
        int end = 59;
        int start = 0;
        for (int i = 0; i < 9; i++) {
                currentColors.add(colors[i]);
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

    private PieData getDataPie(ArrayList<Integer> freqs,String coursename) {
        PieData data = new PieData();
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> currentColors = new ArrayList<>();
        int end = 59;
        int start = 0;
        for (int i = 0; i < 9; i++) {
            if(freqs.get(i) != 0) {
                currentColors.add(colors[i]);
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

        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });

        //data.setValueTypeface(Typeface.SANS_SERIF);
        data.addDataSet(set);

        return data;
    }

    private ArrayList<Integer> getColors(){
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c:ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c:ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c:ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        return colors;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

/*        if (e == null)
            return;*/

/*        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);*/
    }


    @Override
    public void onNothingSelected() {
        
    }

    public class XYMarkerView extends MarkerView {

        private TextView tvContent;
        private IAxisValueFormatter xAxisValueFormatter;

        private DecimalFormat format;

        public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
            super(context, R.layout.custom_marker_view);

            this.xAxisValueFormatter = xAxisValueFormatter;
            tvContent = (TextView) findViewById(R.id.tvContent);
            format = new DecimalFormat("###.0");
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            String val = String.valueOf((int)e.getY());
            tvContent.setText(val + " Studs");
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    private class gradesAxisFormatter implements IAxisValueFormatter {
        private final BarChart mChart;

        public gradesAxisFormatter(BarChart mChart) {
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
}
