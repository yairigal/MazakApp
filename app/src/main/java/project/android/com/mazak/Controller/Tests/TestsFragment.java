package project.android.com.mazak.Controller.Tests;


import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import project.android.com.mazak.Controller.Constants;
import project.android.com.mazak.Controller.GradesView.FatherTab;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Model.Entities.Test;
import project.android.com.mazak.Model.Entities.TestList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

import static project.android.com.mazak.Controller.GradesView.FatherTab.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestsFragment extends Fragment implements IRefresh {


    TestList grades;
    ArrayList<Test> adapterList = new ArrayList<>();
    Database database;
    GradeAdapter adp;
    private View view;
    ListView sem0;
    private LinearLayout mainLayout;
    private ProgressBar spinner;


    public TestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            database = Factory.getInstance();
        } catch (Exception e) {
            Toast.makeText(getContext(), getString(R.string.no_tests_error), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_tests, container, false);
            mainLayout = (LinearLayout) view.findViewById(R.id.TestsMainLayout);
            spinner = (ProgressBar) view.findViewById(R.id.TestsProgressBar);
            sem0 = (ListView) view.findViewById(R.id.listTests);
            sem0.setAdapter(adp = new GradeAdapter(getActivity(), R.layout.fragment_tests, adapterList));
            getTestsAsync(null);
        } else { //IF ALREADY INSTANTIATED USE SAME OLD View
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    /**
     * Initializing the TestList @grades Asynchronously.
     *
     * @param options get the Tests from the web or from the internal database.
     */
    private void getTestsAsync(final getOptions options) {
        new AsyncTask<Void, Void, Void>() {
            public String errorMsg;
            public boolean error;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                FatherTab.toggleSpinner(true, mainLayout, spinner);
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

                //gradesSorted = GradesModel.sortByYears(grades);
                //numOfYears = gradesSorted.size();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (error) {
                    grades = null;
                    //gradesSorted = null;
                    //numOfYears = 0;
                    //showSnackbar(errorMsg);
                    FatherTab.toggleSpinner(false, mainLayout, spinner);
                } else {
                    FatherTab.toggleSpinner(false, mainLayout, spinner);
                    adp.changeList((ArrayList<Test>) grades.clone().getList());
                    adp.notifyDataSetChanged();
                    String cal1 = database.getUpdateTime(InternalDatabase.TestKey);
                    try {
                        if (view != null)
                            Snackbar.make(view, "Last Update  " + cal1, Snackbar.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                    }
                    //setupTabs(view);
                }
            }

            private void getGradesFromAnywhere() {
                try {
                    grades = database.getTests(getOptions.fromMemory).clone();
                } catch (Exception e) {
                    try {
                        if (isNetworkAvailable(getContext()))
                            grades = database.getTests(getOptions.fromWeb).clone();
                        else
                            throw new NetworkErrorException();
                    } catch (Exception e1) {
                        errorMsg = Constants.checkErrorTypeAndMessage(e1, getContext());
                        error = true;
                    }
                }
            }

            private void getGradesFromWebOnly() {
                try {
                    grades = database.getTests(getOptions.fromWeb).clone();
                } catch (Exception e) {
                    errorMsg = Constants.checkErrorTypeAndMessage(e, getContext());
                    error = true;
                }
            }
        }.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR );
    }

    void refreshAdapter(ArrayAdapter adp) {
        adp.notifyDataSetChanged();
    }

    void reLoadGrades(TestList org, ArrayAdapter adp) {
        TestList temp = org.clone();
        grades.clear();
        grades.addAll(temp);
        adp.notifyDataSetChanged();
    }

    /**
     * Refresh The tests from the web Asynchronously
     */
    @Override
    public void Refresh() {
        getTestsAsync(getOptions.fromWeb);
    }


    /**
     * Adapter for the ListView
     */
    private class GradeAdapter extends ArrayAdapter<Test> {

        ArrayList<Test> list;

        public GradeAdapter(Context context, int resource, ArrayList<Test> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        /**
         * changing the list in the adapter.
         *
         * @param list
         */
        public void changeList(ArrayList<Test> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.test, parent, false);
            }

            final Test current = list.get(position);
            //do stuff
            TextView name = (TextView) convertView.findViewById(R.id.TestName);
            TextView moed = (TextView) convertView.findViewById(R.id.TestMoed);
            TextView time = (TextView) convertView.findViewById(R.id.TestTime);
            name.setText(current.getName());
            moed.setText(current.getMoed());
            time.setText(current.getTime() + "\n" + current.getDate());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(current);
                }
            });


            return convertView;
        }


        /**
         * Showing the dialog when clicking an item on the list.
         *
         * @param gd The Test to show his details.
         */
        void showDialog(final Test gd) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_list);
            ((TextView) dialog.findViewById(R.id.dialog1)).setText(gd.getName());
            ((TextView) dialog.findViewById(R.id.dialog2)).setText(gd.getMoed());
            ((TextView) dialog.findViewById(R.id.dialog3)).setText("מועד המבחן:\n" + gd.getDate() + "\n" + gd.getTime());
            ((TextView) dialog.findViewById(R.id.dialog4)).setText("תאריך אחרון להרשמה:" + gd.getLastRegtime());
            ((TextView) dialog.findViewById(R.id.dialog5)).setText("תאריך אחרון לביטול:" + gd.getLastCancelTime());
/*        ListView listView = (ListView) dialog.findViewById(R.id.DialogLV);
        final ArrayList<String> lst = new ArrayList<String>() {{
            add(gd.name);
            add(gd.code);
            add("נ''ז: " + gd.points);
            add("סמסטר: " + gd.semester);
            add("ציון סופי: " + gd.finalGrade);
        }};
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.dialog_list, lst) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.detail, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.detail);
                tv.setText(lst.get(position));
*//*                switch (position){
                    case 0:
                        tv.setText(gd.name);
                        break;
                    case 1:
                        tv.setText(gd.code);
                        break;
                    case 2:
                        tv.setText(gd.points);
                        break;
                    case 3:
                        tv.setText(gd.semester);
                        break;
                    case 4:
                        tv.setText(gd.finalGrade);
                        break;
                }*//*
                return convertView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 5)
                    openStatisticsActivity(gd, dialog);
            }
        });*/
            dialog.show();
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }
    }

}
