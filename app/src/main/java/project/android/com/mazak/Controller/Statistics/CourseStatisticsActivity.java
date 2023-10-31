package project.android.com.mazak.Controller.Statistics;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import project.android.com.mazak.Controller.Constants;
import project.android.com.mazak.Controller.NavDrawerActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

public class CourseStatisticsActivity extends AppCompatActivity{

    public static Integer[] colors = new Integer[]{
            ColorTemplate.rgb("F56D5D"), // red
            ColorTemplate.rgb("FB896E"), // red
            ColorTemplate.rgb("FAA97A"), // orange
            ColorTemplate.rgb("FDC97D"), // orange
            ColorTemplate.rgb("FEEB84"), // yellow
            ColorTemplate.rgb("D9E081"), // bright blue
            ColorTemplate.rgb("AFD77F"), // blue
            ColorTemplate.rgb("8ACA7E"), // bright green
            ColorTemplate.rgb("63BD7D")}; // green
//    String noDataText = "No Data Found";
    IRefresh currentFragmet;
    CourseStatistics current;
    Grade currentGrade;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_statistics);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        getGradeFromIntent();
        getFactoryDatabase();
        getStatisticsAsync(new Delegate() {
            @Override
            public void function(Object obj) {
                onFinishedLoading();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,NavDrawerActivity.class));
        finish();
    }

    /**
     * gets the statistics asynchronously from the server.
     * @param onFinishLoading
     */
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
        }.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR );
    }

    /**
     * this method is executed when the statistics are finished loading.
     */
    private void onFinishedLoading(){

        setupTabs();

        findViewById(R.id.StatsProgressBar).setVisibility(View.GONE);
        findViewById(R.id.layoutChart).setVisibility(View.VISIBLE);

        setupDetails();

    }

    /**
     * sets up all the global details
     */
    private void setupDetails() {
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
    }

    /**
     * gets the grade from the sender activity
     */
    private void getGradeFromIntent() {
        currentGrade = (Grade) getIntent().getSerializableExtra("grade");
    }

    /**
     * gets the instance of the database.
     */
    private void getFactoryDatabase() {
        try {
            db = Factory.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * sets up the charts tabs.
     */
    private void setupTabs( ) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.Stats_TabLayout);
        int numOfTabs = 2;

        for (int i = 0; i < numOfTabs; i++)
            tabLayout.addTab(tabLayout.newTab());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.Stats_ViewPager);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new SamplePageAdapter(getSupportFragmentManager()));

        tabLayout.getTabAt(0).setIcon(R.drawable.bar_chart_down);
        tabLayout.getTabAt(1).setIcon(R.drawable.pieicon);
        //if(checkHebrew())
        tabLayout.getTabAt(0).select();
    }

    private class SamplePageAdapter extends FragmentStatePagerAdapter {
        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        public SamplePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            b.putSerializable("grade", currentGrade);
            b.putSerializable("stats", current);
            Fragment fragment;

            if(position == 0)
                fragment = new BarChartFragment();
            else
                fragment = new PieChartFragment();

            fragment.setArguments(b);
            currentFragmet = (IRefresh) fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Constants.getChartType(position, getApplicationContext());
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
