package project.android.com.mazak.Controller.GradesView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import project.android.com.mazak.Controller.NavDrawerActivity;
import project.android.com.mazak.Controller.Statistics.BarChartFragment;
import project.android.com.mazak.Controller.Statistics.PieChartFragment;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Adapters.DetailAdapter;
import project.android.com.mazak.Model.Adapters.NotebookAdapter;
import project.android.com.mazak.Model.Entities.BackgroundTask;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.Notebook;
import project.android.com.mazak.Model.Entities.NotebookList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.Entities.gradeIngerdiants;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.Model.Utility;
import project.android.com.mazak.Model.Web.MazakAPI;
import project.android.com.mazak.R;

import static project.android.com.mazak.Controller.GradesView.FatherTab.toggleSpinner;
import static project.android.com.mazak.Controller.GradesView.gradesViewFragment.refreshAdapter;

public class singleGradeView extends AppCompatActivity {
    Grade currentGrade;
    CourseStatistics current;
    private Database db;
    IRefresh currentFragmet;
    NotebookList notebooks = new NotebookList();
    Activity activity;
    // the rercyclerview adapter
    NotebookAdapter NotebookAdapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_full_layout);

        activity = this;

        setupToolbar();

        getGradeClicked();

        getSupportActionBar().setTitle(currentGrade.name);

        setBackButtonColor();

        getDatabaseFactory();

        setAllViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotebookAdapter != null)
            NotebookAdapter.notifyDataSetChanged();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarDetails);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(singleGradeView.this, NavDrawerActivity.class));
            }
        });
    }

    private void setBackButtonColor() {
        final Drawable upArrow = this.getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(singleGradeView.this, NavDrawerActivity.class));
        finish();
    }

    private void getDatabaseFactory() {
        db = Factory.getInstance(this);
    }

    private void setAllViews() {
        setGradeDetailsAndNotebooks();
        setGradeStatistics();
        //setNotebooksRecycleView();
        setOnRefreshButtonClicked();
    }

    private void setOnRefreshButtonClicked() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.NotebooksRecycleView);
        Button ref = (Button) findViewById(R.id.NotebookRefreshButton);
        final View notebookLayout = findViewById(R.id.NotebookLayout);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.NotebooksProgressBar);
        final boolean[] error = {false};
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delegates
                Delegate before = new Delegate() {
                    @Override
                    public void function(Object obj) {
                        toggleSpinner(true, notebookLayout, spinner);
                    }
                };
                Delegate in = new Delegate() {
                    @Override
                    public void function(Object obj) {
                        try {
                            notebooks.clear();
                            notebooks.addAll(db.getNotebooks(currentGrade));
                        } catch (Exception e) {
                            error[0] = true;
                            e.printStackTrace();
                        }
                    }
                };
                Delegate after = new Delegate() {
                    @Override
                    public void function(Object obj) {
                        toggleSpinner(false, notebookLayout, spinner);
                        if (error[0]) {
                            Toast.makeText(getApplicationContext(), "Notebook error, Try refreshing", Toast.LENGTH_LONG).show();
                            error[0] = false;
                            return;
                        }
                        NotebookAdapter = new NotebookAdapter(activity, (ArrayList<Notebook>) notebooks.getList());
                        recyclerView.setAdapter(NotebookAdapter);
                        NotebookAdapter.notifyDataSetChanged();

                    }
                };
                new BackgroundTask(before, in, after).start();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NotebookAdapter.onRequestArrived(requestCode, grantResults, NotebookAdapter.currentHolder, NotebookAdapter.currentPressed);
    }

    private void setGradeStatistics() {
        getStatisticsAsync(new Delegate() {
            @Override
            public void function(Object obj) {
                onFinishedLoading();
            }
        });
    }

    /**
     * gets the statistics asynchronously from the server.
     *
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
                    current = db.getStatsFromWeb(currentGrade.actualCourseID);
                    current.setCourseName(currentGrade.name);
                } catch (Exception e) {
                    e.printStackTrace();
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
                    findViewById(R.id.StatsProgressBar).setVisibility(View.GONE);
                    findViewById(R.id.layoutChart).setVisibility(View.VISIBLE);
                    return;
                }
                onFinishLoading.function(null);
                //setUpBarchart(bar, current.getCourseName(), current.getFreqs());
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * this method is executed when the statistics are finished loading.
     */
    private void onFinishedLoading() {

        setupTabs();

        findViewById(R.id.StatsProgressBar).setVisibility(View.GONE);
        findViewById(R.id.layoutChart).setVisibility(View.VISIBLE);

        setupDetails();

        setupStatisticsLayoutHeight();

    }

    private void setupStatisticsLayoutHeight() {
        LinearLayout statsLayout = (LinearLayout) findViewById(R.id.StatisticsLayout);

        ViewGroup.LayoutParams par = statsLayout.getLayoutParams();
        par.height = measureViewChildren(statsLayout) - 800;
        statsLayout.setLayoutParams(par);
        statsLayout.requestLayout();
    }

    /**
     * Measuring The Children height of l.
     *
     * @param l Measuring the height of his children.
     * @return
     */
    public static int measureViewChildren(ViewGroup l) {
        int totalHeight = 0;
        for (int i = 0; i < l.getChildCount(); i++) {
            View listItem = l.getChildAt(i);
            if (listItem.getVisibility() == View.VISIBLE) {
                if (listItem instanceof ViewGroup)
                    totalHeight += measureViewChildren((ViewGroup) listItem);
                else {
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }
        return totalHeight;
    }

    /**
     * sets up all the global details
     */
    private void setupDetails() {
        TextView num = (TextView) findViewById(R.id.numOfStud);
        TextView avg = (TextView) findViewById(R.id.mean);
        TextView med = (TextView) findViewById(R.id.Median);

        TextView grade = (TextView) findViewById(R.id.GradeTV);
        TextView year = (TextView) findViewById(R.id.YearTV);
        TextView sem = (TextView) findViewById(R.id.SemesterTV);

        num.setText(num.getText().toString() + " " + current.getNumOfStudentsWithGrade());
        avg.setText(avg.getText().toString() + " " + current.getMean());
        med.setText(med.getText().toString() + " " + (int) current.getMedian());

        grade.setText(grade.getText() + " " + currentGrade.finalGrade);
        year.setText(year.getText() + " " + currentGrade.code.substring(currentGrade.code.length() - 4, currentGrade.code.length()));
        sem.setText(sem.getText() + " " + currentGrade.semester);
    }

    /**
     * sets up the charts tabs.
     */
    private void setupTabs() {
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

            if (position == 0)
                fragment = new BarChartFragment();
            else
                fragment = new PieChartFragment();

            fragment.setArguments(b);
            currentFragmet = (IRefresh) fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Bar Chart";
            else
                return "Cake Chart";
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    /**
     * Setting up all the Grade details in the details list.
     */
    private void setGradeDetailsAndNotebooks() {
        //((TextView) convertView.findViewById(R.id.nameDetails)).setText(gd.name);
        ((TextView) findViewById(R.id.courseIdDetails)).setText(currentGrade.code);
        ((TextView) findViewById(R.id.courseFinalGrade)).setText("ציון סופי " + currentGrade.finalGrade);

        ((TextView) findViewById(R.id.Type_details)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(R.id.Weight_details)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(R.id.MinGrade_Details)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(R.id.MoedA_detials)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(R.id.MoedB_Details)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(R.id.MoedC_Details)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(R.id.MoedSpec_Details)).setTypeface(null, Typeface.BOLD);

        final ListView detailsListView = (ListView) findViewById(R.id.detailsListView);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.detailsProgressBar);
        final ArrayList<gradeIngerdiants> ing = new ArrayList<>();
        final ArrayAdapter adp = new DetailAdapter(this, R.layout.grade_detail, ing);
        final View notebookLayout = findViewById(R.id.NotebookLayout);
        final ProgressBar spinnerNotebooks = (ProgressBar) findViewById(R.id.NotebooksProgressBar);
        detailsListView.setAdapter(adp);

        new BackgroundTask(new Delegate() {
            @Override
            public void function(Object obj) {
                toggleSpinner(true, detailsListView, spinner);
                toggleSpinner(true, notebookLayout, spinnerNotebooks);
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                try {
                    MazakAPI.Tuple<ArrayList<gradeIngerdiants>, NotebookList> data = db.getGradesDetailsAndNotebooks(currentGrade);
                    ing.clear();
                    ing.addAll(data.x);
                    currentGrade.Notebook.clear();
                    currentGrade.Notebook.addAll(data.y.getList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Delegate() {
            @Override
            public void function(Object obj) {
                //doing it after the details , because the details page has the notebooks link.
                //so instead of going twice to the page , we are doing it once.
                setupNotebooksUI();
                toggleSpinner(false, detailsListView, spinner);
                toggleSpinner(false, notebookLayout, spinnerNotebooks);
                refreshAdapter(adp);
                Utility.setListViewHeightBasedOnChildren(detailsListView);
            }
        }).start();

    }

    private void setupNotebooksUI() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.NotebooksRecycleView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        NotebookAdapter = new NotebookAdapter(this, currentGrade.Notebook);
        recyclerView.setAdapter(NotebookAdapter);
        NotebookAdapter.notifyDataSetChanged();
    }
    private void getGradeClicked() {
        currentGrade = (Grade) getIntent().getSerializableExtra("grade");
    }
}
