package project.android.com.mazak.Controller.Appeals;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import project.android.com.mazak.Controller.GradesView.FatherTab;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Model.Entities.Irur;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.Model.Utility;
import project.android.com.mazak.R;


public class IrurFragment extends Fragment implements ISearch {
    private static IrurFragment instance;
    IrurList irurs;
    IrurList acceptedList,failedList,halfList,progList;
    Database db;
    ProgressBar spinner;
    View view;
    LinearLayout mainLayout;
    myAdapter acceptedad,failedad,halfad,progad;
    String ACCEPTED = "התקבל",HALF = "התקבל חלקית",PROG = "בטיפול";
    String[] FAILED = {"נדחה","ציון הורד"};
    ListView inProg;
    ListView accpeted;
    ListView half;
    ListView failed;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IrurFragment() {
    }

    /**
     * Singleton functions.
     * @return
     */
    public static Fragment getInstance() {
        if(instance == null)
            instance = new IrurFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        irurs = new IrurList();
    }

    /**
     * Gets The irurs from the web Asynchronously
     */
    private void getIrursAsync() {
        try {
            db = Factory.getInstance();
        } catch (Exception e) {
            showSnackException("Database error");
        }
        AsyncTask<Void, Void, IrurList> task = new AsyncTask<Void, Void, IrurList>() {
            IrurList newList = new IrurList();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                FatherTab.toggleSpinner(true, mainLayout, spinner);
            }

            @Override
            protected IrurList doInBackground(Void... params) {
                try {
                    newList = db.getIrurs(getOptions.fromMemory);
                } catch (Exception e) {
                    try {
                        newList = db.getIrurs(getOptions.fromWeb);
                    } catch (Exception e1) {
                        //showSnackException("Error getting Irurs");
                        e1.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(IrurList x) {
                super.onPostExecute(x);
                FatherTab.toggleSpinner(false, mainLayout, spinner);
                refreshAdapter(acceptedad, acceptedList.getList(), getIrurWithStatus(newList,ACCEPTED).getList());
                refreshAdapter(failedad, failedList.getList(), getFailedIrurs(newList,FAILED).getList());
                refreshAdapter(halfad, halfList.getList(), getIrurWithStatus(newList,HALF).getList());
                refreshAdapter(progad, progList.getList(), getIrurWithStatus(newList,PROG).getList());

                Utility.setListViewHeightBasedOnChildren(inProg);
                Utility.setListViewHeightBasedOnChildren(accpeted);
                Utility.setListViewHeightBasedOnChildren(half);
                Utility.setListViewHeightBasedOnChildren(failed);

                if (newList.size() == 0)
                    showEmptyMessage();
                else
                    hideEmptyMessage();
                //progressBar.setVisibility(VISIBLE);
                String cal1 = db.getUpdateTime(InternalDatabase.IrursKey);
                Snackbar.make(view,"Last Update  "+cal1, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    /**
     * Returns the irurs with that status
     * @param irurs scans this irurs
     * @param status searches for that status.
     * @return the irurs with that status.
     */
    private IrurList getFailedIrurs(IrurList irurs, String[] status){
        IrurList toret = new IrurList();
        for(int i=0;i<status.length;i++){
            toret.addAll(getIrurWithStatus(irurs,status[i]));
        }
        return toret;
    }

    /**
     * Gets the irrus asynchronously by the option specified
     * @param op fromWeb or fromDatabase.
     */
    private void getIrursAsync(final getOptions op) {
        try {
            db = Factory.getInstance();
        } catch (Exception e) {
            showSnackException("Database error");
        }
        AsyncTask<Void, Void, IrurList> task = new AsyncTask<Void, Void, IrurList>() {
            IrurList newList = new IrurList();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                FatherTab.toggleSpinner(true, mainLayout, spinner);
            }

            @Override
            protected IrurList doInBackground(Void... params) {
                try {
                    newList = db.getIrurs(op);
                } catch (Exception e) {
                    //showSnackException("Error getting Irurs");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(IrurList x) {
                super.onPostExecute(x);
                FatherTab.toggleSpinner(false, mainLayout, spinner);
                refreshAdapter(acceptedad, acceptedList.getList(), getIrurWithStatus(newList,ACCEPTED).getList());
                refreshAdapter(failedad, failedList.getList(), getFailedIrurs(newList,FAILED).getList());
                refreshAdapter(halfad, halfList.getList(), getIrurWithStatus(newList,HALF).getList());
                refreshAdapter(progad, progList.getList(), getIrurWithStatus(newList,PROG).getList());


                if (newList.size() == 0)
                    showEmptyMessage();
                else
                    hideEmptyMessage();
                //progressBar.setVisibility(VISIBLE);
                String cal1 = db.getUpdateTime(InternalDatabase.IrursKey);
                try {
                    if (view != null)
                        Snackbar.make(view, "Last Update  " + cal1, Toast.LENGTH_SHORT).show();
                }catch (Exception ex){};
            }
        };
        task.execute();
    }

    /**
     * hides the emptyIrurs messages
     */
    private void hideEmptyMessage() {
        (view.findViewById(R.id.emptyIrurs)).setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    /**
     * shows the emotyIrurs label
     */
    private void showEmptyMessage() {
        (view.findViewById(R.id.emptyIrurs)).setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

    }

    /**
     * Show a snackbar with the msg specified
     * @param msg the message to be shown.
     */
    private void showSnackException(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_irur_parent, container, false);
        spinner = (ProgressBar) view.findViewById(R.id.irurSpinner);
        mainLayout = (LinearLayout) view.findViewById(R.id.irursMainLayout);
        inProg = (ListView) view.findViewById(R.id.listAppealsInProgress);
        accpeted = (ListView) view.findViewById(R.id.listAppealsAccepted);
        half = (ListView) view.findViewById(R.id.listAppealsHalfAccepted);
        failed = (ListView) view.findViewById(R.id.listAppealsNotAccepted);

        getIrursAsync();

        progList = getIrurWithStatus(irurs,PROG);
        inProg.setAdapter(progad = new myAdapter(getContext(),R.layout.fragment_irur_parent,progList.getList()));
        acceptedList = getIrurWithStatus(irurs,ACCEPTED);
        accpeted.setAdapter(acceptedad = new myAdapter(getContext(),R.layout.fragment_irur_parent,acceptedList.getList()));
        halfList = getIrurWithStatus(irurs,HALF);
        half.setAdapter(halfad = new myAdapter(getContext(),R.layout.fragment_irur_parent,halfList.getList()));
        failedList = getFailedIrurs(irurs,FAILED);
        failed.setAdapter(failedad = new myAdapter(getContext(),R.layout.fragment_irur_parent,failedList.getList()));

        return view;
    }

    /**
     * Returns the irurs with that status
     * @param irurs scans this irurs
     * @param status searches for that status.
     * @return the irurs with that status.
     */
    private IrurList getIrurWithStatus(IrurList irurs,String status){
        IrurList toReturn = new IrurList();
        for (Irur r:irurs.getList())
            if(r.getStatus().equals(status))
                toReturn.add(r);
        return toReturn;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void Filter(String query) {

    }

    @Override
    public void clearFilter() {

    }

    @Override
    public void Refresh() {
        getIrursAsync(getOptions.fromWeb);
    }

    /**
     * refresh the listView adapter.
     * @param ad the adapter to be refreshed
     * @param originList the original list
     * @param newList the new list to be shown.
     */
    public static void refreshAdapter(ArrayAdapter ad, ArrayList originList, ArrayList newList) {
        originList.clear();
        originList.addAll(newList);
        ad.notifyDataSetChanged();
    }

    /**
     * Adapter class for the ListView adapter.
     */
    class myAdapter extends ArrayAdapter<Irur>{

        private final IrurList irurs;

        public myAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Irur> objects) {
            super(context, resource, objects);
            this.irurs = new IrurList((ArrayList<Irur>) objects,false);
        }

        private void setAppealColor(View view, Irur current) {
            TextView name = (TextView) view;
            String status = current.getStatus();
            if(status.equals("התקבל")){
                name.setTextColor(ColorTemplate.rgb("388E3C"));
            } else if(status.equals("התקבל חלקית")){
                name.setTextColor(ColorTemplate.rgb("FFA000"));
            } else if(status.equals("נדחה")){
                name.setTextColor(ColorTemplate.rgb("D32F2F"));
            }else if(status.equals("בטיפול")){
                name.setTextColor(ColorTemplate.rgb("9E9E9E"));
            }
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.irur_parent, parent, false);
                }
                final Irur current = irurs.get(position);
                //set the irur name;
                ((TextView) convertView.findViewById(R.id.IrurName)).setText(current.getCourseName());
                setAppealColor(convertView.findViewById(R.id.IrurName),current);
                setOnClick(convertView,current);
                return convertView;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return convertView;
        }




        private void setOnClick(View convertView, final Irur irur) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(irur);
                }
            });
        }


        @Override
        public int getCount() {
            if (irurs != null)
                return irurs.size();
            return 0;
        }
    }

    /**
     * Shows the dialog with the irur details.
     * @param gd
     */
    void showDialog(final Irur gd) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.irur_child);
        ((TextView)dialog.findViewById(R.id.AppealsChild_Name)).setText(gd.getCourseName());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Code)).setText(gd.getCourseNum());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Detail)).setText(gd.getGradeDetail());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Moed)).setText(gd.getMoed());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Instructor)).setText(gd.getInChargeName());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Lecturer)).setText(gd.getLecturer());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Date)).setText(gd.getDate());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Type)).setText(gd.getIrurType());
        ((TextView)dialog.findViewById(R.id.AppealsChild_Status)).setText(gd.getStatus());

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
}
