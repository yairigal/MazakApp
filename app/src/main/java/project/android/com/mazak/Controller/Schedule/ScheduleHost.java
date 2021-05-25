package project.android.com.mazak.Controller.Schedule;


import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;

import project.android.com.mazak.Controller.GradesView.FatherTab;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

import static project.android.com.mazak.Controller.GradesView.FatherTab.toggleSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleHost extends Fragment implements IRefresh {

    private static ScheduleHost instance;
    View root;
    int NumOfDays;
    Database db;
    ProgressBar pb;
    LinearLayout MainLayouit;
    public final static String[] daysOfWeek = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
    private Fragment currentFragmet;
    ScheduleList list;

    public ScheduleHost() {
        // Required empty public constructor
    }


    public static Fragment getInstance() {
        if(instance == null)
            instance = new ScheduleHost();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_schedule_host, container, false);
            pb = (ProgressBar) root.findViewById(R.id.ScheduleHost_ProgressBar);
            MainLayouit = (LinearLayout) root.findViewById(R.id.ScheduleHost_MainLayout);
            getDatabaseAsync();
            getScheduleAsync(root, null);
        }
        return root;
    }

    /**
     * gets the database instance.
     */
    private void getDatabaseAsync() {
        db = Factory.getInstance(getContext());
    }

    /**
     * sets up the days tabs.
     * @param v
     */
    private void setupTabs(View v) {
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.ScheduleHost_TabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < NumOfDays; i++)
            tabLayout.addTab(tabLayout.newTab());

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.ScheduleHost_viewPager);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new SamplePageAdapter(getFragmentManager()));

        int day = getDayOfWeek();

        //solution for crashing on fridays when your schedule is only till wednesday.
        try {
            tabLayout.getTabAt(day).select();
        }catch (Exception ex){
            tabLayout.getTabAt(0).select();
        }
    }

    /**
     * gets the schedule from the web or the database
     * @param view
     * @param options if options is null , this tries from the database then from the web.
     */
    @SuppressLint("StaticFieldLeak")
    private void getScheduleAsync(final View view, final getOptions options) {
        try {
            new AsyncTask<Void, Void, Void>() {
                public String errorMsg;
                public boolean error;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    toggleSpinner(true, MainLayouit, pb);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    if (options == getOptions.fromWeb) {
                        getGradesFromWebOnly();
                    } else {
                        getGradesFromAnywhere();
                    }
                    if (error)
                        return null;
                    NumOfDays = list.getNumOfDays();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (error) {
                        list = null;
                        NumOfDays = 0;
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        toggleSpinner(false, MainLayouit, pb);
                    } else {
                        toggleSpinner(false, MainLayouit, pb);
                        setupTabs(view);
                        String cal1 = db.getUpdateTime(InternalDatabase.ScheduleKey);
                        try {
                            if (view != null)
                                Snackbar.make(view, "Last Update  " + cal1, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                        }
                    }
                }

                private void getGradesFromAnywhere(){
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                        error = true;
                        return;
                    }
                    try {
                        list = db.getScheudle(getOptions.fromMemory);
                    } catch (Exception e) {
                        try {
                            if (FatherTab.isNetworkAvailable(getContext()))
                                list = db.getScheudle(getOptions.fromWeb);
                            else
                                throw new NetworkErrorException();
                        } catch (Exception e1) {
                            errorMsg = FatherTab.checkErrorTypeAndMessage(e1);
                            error = true;
                        }
                    }
                }

                private void getGradesFromWebOnly() {
                    try {
                        list = db.getScheudle(getOptions.fromWeb);
                    } catch (Exception e) {
                        errorMsg = FatherTab.checkErrorTypeAndMessage(e);
                        error = true;
                    }
                }

            }.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR );
        }catch (Exception ex){
            Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Refresh() {
        getScheduleAsync(root,getOptions.fromWeb);
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
            b.putSerializable("events",list.getEventsByDay(position));
            Fragment fragment = new DayFragment();
            fragment.setArguments(b);
            currentFragmet = fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return daysOfWeek[position];
        }

        @Override
        public int getCount() {
            return NumOfDays;
        }
    }

    /**
     * returns the current dat of the week number.
     * @return
     */
    private int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.SUNDAY:day = 0;break;
            case Calendar.MONDAY:day = 1;break;
            case Calendar.TUESDAY:day = 2;break;
            case Calendar.WEDNESDAY:day = 3;break;
            case Calendar.THURSDAY:day = 4;break;
            case Calendar.FRIDAY:day = 5;break;
            default: day=0;break;
        }
        return day;
    }

}