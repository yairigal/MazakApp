package project.android.com.mazak.Controller.GradesView;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
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

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;

import project.android.com.mazak.Controller.Login.LoginActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.R;

public class FatherTab extends Fragment implements ISearch {

    private static FatherTab instnace;
    private ISearch currentFragmet;
    View view;
    Database db;
    GradesList grades = null;
    HashMap<Integer, GradesList> gradesSorted = new HashMap<>();
    int numOfYears = 0;
    ProgressBar spinner;
    LinearLayout mainLayout;
    LoginDatabase loginDatabase;
    boolean isPreperation;

    public FatherTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_father_tab, container, false);
            spinner = (ProgressBar) view.findViewById(R.id.FatherSpinner);
            mainLayout = (LinearLayout) view.findViewById(R.id.TabsFatherLayout);

            try {
                db = Factory.getInstance(getActivity());
                getGradesAsync(view, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    /**
     * if options if null  - tries to get grades from database , if failed , gets grades from web.
     * if options is not null - gets the grades from that specific place.
     *
     * @param view
     * @param options
     */
    @SuppressLint("StaticFieldLeak")
    private void getGradesAsync(final View view, final getOptions options) {
        new AsyncTask<Void, Void, Void>() {
            public String errorMsg;
            public boolean error;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toggleSpinner(true, mainLayout, spinner);
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

                gradesSorted = GradesModel.sortByYears(grades);
                numOfYears = gradesSorted.size();
                try {
                    isPreperation = db.isPreperation();
                } catch (Exception e) {
                }

/*                long time1 = System.currentTimeMillis();
                try {
                    MazakConnection connection = db.getConnection();
                    for(int i=0;i<grades.size();i++)
                        grades.get(i).getGradeDetails(connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                time1 = System.currentTimeMillis() - time1;
                time1++;*/
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (error) {
                    grades = null;
                    gradesSorted = null;
                    numOfYears = 0;
                    //showSnackbar(errorMsg);
                    toggleSpinner(false, mainLayout, spinner);
                } else {
                    toggleSpinner(false, mainLayout, spinner);
                    setupTabs(view, isPreperation);
                    String cal1 = db.getUpdateTime(InternalDatabase.gradesKey);
                    try {
                        if (view != null)
                            Snackbar.make(view, "Last Update  " + cal1, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                    }
                }
            }

            private void getGradesFromAnywhere() {
                try {
                    grades = db.getGrades(getOptions.fromMemory);
                } catch (Exception e) {
                    try {
                        if (isNetworkAvailable(getContext()))
                            grades = db.getGrades(getOptions.fromWeb);
                        else
                            throw new NetworkErrorException();
                    } catch (Exception e1) {
                        errorMsg = checkErrorTypeAndMessage(e1);
                        error = true;
                    }
                }
            }

            private void getGradesFromWebOnly() {
                try {
                    grades = db.getGrades(getOptions.fromWeb);
                } catch (Exception e) {
                    errorMsg = checkErrorTypeAndMessage(e);
                    error = true;
                }
            }

            private void showSnackbar(String errorMsg) {
                if (errorMsg.contains("password"))
                    Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG).setAction("CHANGE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                //clearing the username and password for when moving to navActivity it will start the service(searching for username and password will fail)
                                loginDatabase.clearLoginInformation();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent Login = new Intent(getContext(), LoginActivity.class);
                            startActivity(Login);
                        }
                    }).show();
                else
                    Snackbar.make(getView(), errorMsg, Snackbar.LENGTH_LONG).show();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * checks which error it is and returns the error message for that type.
     *
     * @param e1
     * @return
     */
    public static String checkErrorTypeAndMessage(Exception e1) {
        String errorMsg;
        if (e1 instanceof UnknownHostException)
            errorMsg = "'levnet.jct.ac.il' might be down";
        else if (e1 instanceof NullPointerException)
            errorMsg = "An Error Occurred";
        else if (e1 instanceof NetworkErrorException)
            errorMsg = "Check your internet connection";
        else
            errorMsg = "Database Error";
        return errorMsg;
    }

    /**
     * set us the years tabs
     *
     * @param v
     */
    private void setupTabs(View v, boolean Preparation) {
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        if (numOfYears > 3)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < numOfYears; i++)
            tabLayout.addTab(tabLayout.newTab(), false);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager, true);
        SamplePageAdapter mAdapter = new SamplePageAdapter(getFragmentManager(), (HashMap<Integer, GradesList>) gradesSorted.clone(), Preparation);
        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
        //if(checkHebrew())
        tabLayout.getTabAt(numOfYears - 1).select();
    }

    @Override
    public void Filter(String query) {
        currentFragmet.Filter(query);
    }

    @Override
    public void clearFilter() {
        currentFragmet.clearFilter();
    }

    @Override
    public void Refresh() {
        getGradesAsync(getView(), getOptions.fromWeb);
    }

    public static Fragment getInstance() {
        return new FatherTab();
    }

    private class SamplePageAdapter extends FragmentStatePagerAdapter {
        private final HashMap<Integer, GradesList> grades;
        private final boolean preparation;

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        public SamplePageAdapter(FragmentManager fm, HashMap<Integer, GradesList> gradesSorted, boolean Preparation) {
            super(fm);
            this.grades = gradesSorted;
            this.preparation = Preparation;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            b.putInt("year", position + 1);
            GradesList currentYearList = grades.get(position + 1);
            b.putSerializable("list", currentYearList);
            Fragment fragment = new gradesViewFragment();
            fragment.setArguments(b);
            currentFragmet = (ISearch) fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (checkHebrew())
                return getYearTitle(position + 1, preparation);
            return getYearTitle(position + 1, preparation);
        }

        @Override
        public int getCount() {
            return grades.size();
        }
    }

    private GradesList getGradesByYear(HashMap<Integer, HashMap<Integer, GradesList>> gradesSorted, int i) {
        GradesList lst = new GradesList();
        for (int j = 0; j < gradesSorted.get(i).size(); j++)
            lst.addAll(gradesSorted.get(i).get(j));
        return lst;
    }

    /**
     * checks if the device is on hebrew.
     *
     * @return
     */
    public static boolean checkHebrew() {
        return Locale.getDefault().toString().equals("iw_IL") || Locale.getDefault().toString().equals("he_IL");
    }

    /**
     * gets the Title by the year number.
     *
     * @param year
     * @return
     */
    String getYearTitle(int year, boolean Preparation) {
        String title;
        if (!Preparation) {
            switch (year) {
                case 1:
                    title = getString(R.string.first_year);
                    break;
                case 2:
                    title = getString(R.string.second_year);
                    break;
                case 3:
                    title = getString(R.string.third_year);
                    break;
                case 4:
                    title = getString(R.string.fourth_year);
                    break;
                case 5:
                    title = getString(R.string.fifth_year);
                    break;
                default:
                    title = getString(R.string.other);
                    break;
            }
        } else {
            switch (year) {
                case 1:
                    title = getString(R.string.prep_year);
                    break;
                default:
                    title = getYearTitle(year - 1, false);
                    break;
            }
        }
        return title;
    }

    /**
     * toggles the spinner (loading animation) true or false
     *
     * @param toggle    toggle the spinner true or false
     * @param toDismiss the view to dismiss and hide
     * @param toShow    the view to show.
     */
    public static void toggleSpinner(boolean toggle, View toDismiss, ProgressBar toShow) {
        if (toggle) {
            toDismiss.setVisibility(View.GONE);
            toShow.setVisibility(View.VISIBLE);
            //swipeRefreshLayout.setRefreshing(true);
        } else {
            toDismiss.setVisibility(View.VISIBLE);
            toShow.setVisibility(View.GONE);
            //swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * checks if network is available in the device.
     *
     * @param ctx
     * @return
     */
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
