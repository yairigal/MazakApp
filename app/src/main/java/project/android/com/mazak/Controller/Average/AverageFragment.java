package project.android.com.mazak.Controller.Average;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.android.com.mazak.Controller.Statistics.BarChartFragment;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

import static project.android.com.mazak.Controller.GradesView.FatherTab.toggleSpinner;
import static project.android.com.mazak.Controller.GradesView.singleGradeView.measureViewChildren;


public class AverageFragment extends Fragment implements IRefresh {

    private static final int MAX_SEM = 3;
    private static AverageFragment instance;
    private View root;
    Database db;
    GradesList grades;
    private LayoutInflater mainInflaytor;
    BarChart mBarChart;
    LinearLayout masterLayout;
    ScrollView mScrollView;


    public AverageFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (instance == null)
            instance = new AverageFragment();
        return instance;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null) {
            mainInflaytor = inflater;
            root = inflater.inflate(R.layout.fragment_average, container, false);
            mBarChart = (BarChart) root.findViewById(R.id.LineChart2);
            masterLayout = (LinearLayout) root.findViewById(R.id.masterLayout);
            mScrollView = (ScrollView) root.findViewById(R.id.masterScroll);
            if (getDatabaseFactory()) {
                getGradesAsync(new Delegate() {
                    @Override
                    public void function(Object obj) {
                        //initSpinners();
                        onFinished();
                    }
                });
            }
        }
        return root;
    }

   /* private void initSpinners() {
        sem.setEnabled(false);
        final HashMap<Integer, HashMap<Integer, GradesList>> map = GradesModel.sortBySemesterAndYear(grades);
        final ArrayList<String> yearArray = new ArrayList<>();
        for (int i=1;i<=map.size();i++)
            yearArray.add(String.valueOf(i));
        year.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,yearArray));
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sem.setEnabled(true);
                ArrayList<String> semArray = new ArrayList<>();
                for (int i=0;i<map.get(position+1).size();i++)
                    semArray.add(String.valueOf(GradesModel.intToSemster(i)));
                sem.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,semArray));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sem.setEnabled(false);
            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradesList grades = map.get(year.getSelectedItemPosition()+1).get(sem.getSelectedItemPosition());
                HashMap<String, Float> avgs = GradesModel.getAvg(grades);
                calc.setText("ממוצע: "+ roundTo2OrLessAfterPoint(avgs.get("avg")));
            }
        });
    }*/


    private void getGradesAsync(final Delegate delegate) {
        final ProgressBar pb = (ProgressBar) root.findViewById(R.id.averageProgressBar);
        new AsyncTask<Void, Void, Void>() {
            public boolean error;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toggleSpinner(true,mBarChart,pb);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    grades = db.getGrades(getOptions.fromMemory);
                } catch (Exception e) {
                    try {
                        grades = db.getGrades(getOptions.fromWeb);
                    } catch (Exception e1) {
                        error = true;
                        Snackbar.make(root, "Error getting grades", Snackbar.LENGTH_LONG).show();
                        e1.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (error) {
                    root.findViewById(R.id.notfoundIDAvg).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.mainLayoutAvg).setVisibility(View.GONE);
                    return;
                }
                grades = GradesModel.removeDuplicatesOfGrades(grades);
                toggleSpinner(false,mBarChart,pb);
                delegate.function(null);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * This function gets called when loading the grades is done.
     */
    private void onFinished() {
        calculateAverageAndRename();
        addYearsCards();
        setupChart();
    }

    private void setData(GradesList grades, BarChart mChart) {


        ArrayList<ArrayList<BarEntry>> years = getBarEntryGrades(grades);


        ArrayList<BarEntry> sem0 = years.get(0);
        ArrayList<BarEntry> sem1 = years.get(1);
        ArrayList<BarEntry> sem2 = years.get(2);


        float groupSpace = 0.325f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        int groupCount = sem0.size();
        int startYear = 0;
        int endYear = startYear + groupCount;

        BarDataSet set0 = new BarDataSet(sem0, "Semester Alul");
        set0.setColor(getColor(0));
        BarDataSet set1 = new BarDataSet(sem1, "Semester A");
        set1.setColor(getColor(1));
        BarDataSet set2 = new BarDataSet(sem2, "Semester B");
        set2.setColor(getColor(2));


        mChart.getAxisLeft().setAxisMinimum(0);
        mChart.getAxisLeft().setAxisMaximum(100);


        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        mChart.getDescription().setEnabled(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        mChart.setClickable(false);
        mChart.setDoubleTapToZoomEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "Year" + " #" + String.valueOf((int) value + 1);
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        // create a data object with the datasets
        BarData data = new BarData(set0, set1, set2);

        // set data
        mChart.setData(data);
        mChart.animateXY(300, 300);
        // specify the width each bar should have
        mChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mChart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.getXAxis().setAxisMaximum(endYear);
        mChart.groupBars(startYear, groupSpace, barSpace);
        mChart.invalidate();
    }

    @NonNull
    private ArrayList<ArrayList<BarEntry>> getBarEntryGrades(GradesList grades) {
        HashMap<Integer, HashMap<Integer, GradesList>> map = GradesModel.sortBySemesterAndYear(grades);
        ArrayList<BarEntry> sem0 = new ArrayList<>();
        ArrayList<BarEntry> sem1 = new ArrayList<>();
        ArrayList<BarEntry> sem2 = new ArrayList<>();
        ArrayList<ArrayList<BarEntry>> years = new ArrayList<>();
        for (int i = 1; i <= map.size(); i++) {
            Float avg = GradesModel.getAvg(map.get(i).get(0)).get("avg");
            sem0.add(new BarEntry(i, avg));
            avg = GradesModel.getAvg(map.get(i).get(1)).get("avg");
            sem1.add(new BarEntry(i, avg));
            avg = GradesModel.getAvg(map.get(i).get(2)).get("avg");
            sem2.add(new BarEntry(i, avg));
        }
        years.add(sem0);
        years.add(sem1);
        years.add(sem2);
        return years;
    }

    private int getColor(int i) {
        switch (i) {
            case 0:
                return ColorTemplate.rgb("D32F2F"); // red
            case 1:
                return ColorTemplate.rgb("FFC107"); // yellow
            case 2:
                return ColorTemplate.rgb("00BCD4"); // bright blue
            case 3:
                return ColorTemplate.rgb("689F38"); // bright green
            case 4:
                return ColorTemplate.rgb("FF9800"); // orange
            case 5:
                return ColorTemplate.rgb("F57C00"); // orange
            case 6:
                return ColorTemplate.rgb("4CAF50");
            case 7:
                return ColorTemplate.rgb("1976D2"); // blue
            case 8:
                return ColorTemplate.rgb("FF5722"); // red
            default:
                return Color.BLACK;
        }
    }


    /**
     * setting up the Bar chart for the grades.
     */
    private void setupChart() {
        try {
            ArrayList<Integer> freqs = GradesModel.sortGradesByValue(grades);
            CourseStatistics stats = new CourseStatistics("Grades", freqs);
            setData(grades, mBarChart);

/*            ViewGroup.LayoutParams par = mBarChart.getLayoutParams();
            par.height = mBarChart.getHeight()-100;
            mBarChart.setLayoutParams(par);
            mBarChart.requestLayout();*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }

/*        int x = mLineChart.getHeight() + masterLayout.getHeight() + mScrollView.getHeight() + 50;


        masterLayout.getLayoutParams().height += ViewGroup.LayoutParams.MATCH_PARENT;
        masterLayout.requestLayout();
        mScrollView.getLayoutParams().height += ViewGroup.LayoutParams.MATCH_PARENT;
        mScrollView.requestLayout();
        mLineChart.getLayoutParams().height += ViewGroup.LayoutParams.MATCH_PARENT;
        mLineChart.requestLayout();*/


    }


    /**
     * Calculates all the averages
     * renames the label to their proper names.
     */
    private void calculateAverageAndRename() {
        HashMap<String, Float> map = GradesModel.getAvg(grades);
        String avg = roundTo2OrLessAfterPoint(map.get("avg")),
                nza = roundTo2OrLessAfterPoint(map.get("nz")),
                kod = roundTo2OrLessAfterPoint(map.get("kodesh"));
        TextView total = (TextView) root.findViewById(R.id.averageTextview);
        total.setText(avg);
        TextView nz = (TextView) root.findViewById(R.id.NZ_AVGTextview);
        nz.setText(nza);
        TextView kodesh = (TextView) root.findViewById(R.id.averageKodeshTextview);
        kodesh.setText(kod);
    }

    /**
     * rounding the num for 2 digits after the point
     *
     * @param num
     * @return
     */
    private String roundTo2OrLessAfterPoint(float num) {
        if (num == ((int) num)) // no digits after point
            return String.valueOf((int) num);
        return String.valueOf(((float) Math.round(num * 100) / 100));
    }

    /**
     * gets the database instance
     *
     * @return
     */
    private boolean getDatabaseFactory() {
        try {
            db = Factory.getInstance();
            return true;
        } catch (Exception e) {
            Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * adds the years cards to the main view.
     */
    private void addYearsCards() {
        HashMap<Integer, GradesList> map = GradesModel.sortByYears(grades);
        ((ViewGroup) (root.findViewById(R.id.mainLayoutAverage))).removeAllViews();
        for (int i = 1; i <= map.size(); i++) {
            View Mainview = mainInflaytor.inflate(R.layout.average_year, null);
            ((TextView) Mainview.findViewById(R.id.CardTitle_YearAverage)).setText("שנה " + i);
            ((TextView) Mainview.findViewById(R.id.NZInput_averageYear)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(i)).get("nz")));
            ((TextView) Mainview.findViewById(R.id.averageInput_averageYear)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(i)).get("avg")));
            setupSemesters(Mainview, map.get(i));
            ((LinearLayout) root.findViewById(R.id.mainLayoutAverage)).addView(Mainview);
        }
    }

    /**
     * adds the semesters to each card.
     *
     * @param mainview
     * @param grades
     */
    private void setupSemesters(View mainview, GradesList grades) {
        HashMap<Integer, GradesList> map = GradesModel.sortBySemester(GradesModel.removeDuplicatesOfGrades(grades));
        ((TextView) mainview.findViewById(R.id.CardTitle_Sem1Layout)).setText("סמסטר אלול");
        ((TextView) mainview.findViewById(R.id.averageInput_averageSem1)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(0)).get("avg")));
        ((TextView) mainview.findViewById(R.id.NZInput_averageSem1)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(0)).get("nz")));

        ((TextView) mainview.findViewById(R.id.CardTitle_Sem2Layout)).setText("סמסטר א'");
        ((TextView) mainview.findViewById(R.id.averageInput_averageSem2)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(1)).get("avg")));
        ((TextView) mainview.findViewById(R.id.NZInput_averageSem2)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(1)).get("nz")));

        ((TextView) mainview.findViewById(R.id.CardTitle_Sem3Layout)).setText("סמסטר ב'");
        ((TextView) mainview.findViewById(R.id.averageInput_averageSem3)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(2)).get("avg")));
        ((TextView) mainview.findViewById(R.id.NZInput_averageSem3)).setText(roundTo2OrLessAfterPoint(GradesModel.getAvg(map.get(2)).get("nz")));
    }

    @Override
    public void Refresh() {
        onFinished();
    }
}
