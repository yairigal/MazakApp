package project.android.com.mazak.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.Irur;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.Model.getOptions;
import project.android.com.mazak.R;


public class IrurFragment extends Fragment implements ISearch {
    ExpandableListView listView;
    BaseExpandableListAdapter adp;
    IrurList irurs;
    Database db;
    ProgressBar spinner;
    View view;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IrurFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        irurs = new IrurList();
    }

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
                FatherTab.toggleSpinner(true, listView, spinner);
            }

            @Override
            protected IrurList doInBackground(Void... params) {
                try {
                    newList = db.getIrurs(getOptions.fromMemory);
                } catch (Exception e) {
                    try {
                        newList = db.getIrurs(getOptions.fromWeb);
                    } catch (Exception e1) {
                        showSnackException("Error getting Irurs");
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(IrurList x) {
                super.onPostExecute(x);
                FatherTab.toggleSpinner(false, listView, spinner);
                refreshAdapter(adp, irurs.getList(), newList.getList());

                if (newList.size() == 0)
                    showEmptyMessage();
                else
                    hideEmptyMessage();
                //progressBar.setVisibility(VISIBLE);
            }
        };
        task.execute();
    }

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
                FatherTab.toggleSpinner(true, listView, spinner);
            }

            @Override
            protected IrurList doInBackground(Void... params) {
                try {
                    newList = db.getIrurs(op);
                } catch (Exception e) {

                    showSnackException("Error getting Irurs");
                }
                return null;
            }

            @Override
            protected void onPostExecute(IrurList x) {
                super.onPostExecute(x);
                FatherTab.toggleSpinner(false, listView, spinner);
                refreshAdapter(adp, irurs.getList(), newList.getList());

                if (newList.size() == 0)
                    showEmptyMessage();
                else
                    hideEmptyMessage();
                //progressBar.setVisibility(VISIBLE);
            }
        };
        task.execute();
    }

    private void hideEmptyMessage() {
        (view.findViewById(R.id.emptyIrurs)).setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private void showEmptyMessage() {
        (view.findViewById(R.id.emptyIrurs)).setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

    }

    private void showSnackException(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_irur_parent, container, false);
        listView = (ExpandableListView) view.findViewById(R.id.expandListViewIrur);
        spinner = (ProgressBar) view.findViewById(R.id.irurSpinner);
        getIrursAsync();
        adp = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return irurs.getList().size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 9;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return irurs;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                Irur current = irurs.get(groupPosition);
                switch (childPosition) {
                    case 0:
                        return current.getCourseNum();
                    case 1:
                        return current.getCourseName();
                    case 2:
                        return current.getGradeDetail();
                    case 3:
                        return current.getIrurType();
                    case 4:
                        return current.getMoed();
                    case 5:
                        return current.getInChargeName();
                    case 6:
                        return current.getLecturer();
                    case 7:
                        return current.getDate();
                    default:
                        return current.getStatus();
                }
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            private String getTitle(int groupPosition, int childPosition) {
                Irur count = irurs.get(groupPosition);
                switch (childPosition) {
                    case 0:
                        return "Name: ";
                    case 1:
                        return "Country: ";
                    case 2:
                        return "Email: ";
                    case 3:
                        return "Website: ";
                    default:
                        return "Name: ";
                }
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.irur_parent, parent, false);
                }
                final Irur current = irurs.get(groupPosition);
                //set the irur name;
                ((TextView) convertView.findViewById(R.id.IrurName)).setText(current.getCourseName());
                setAppealColor(convertView.findViewById(R.id.IrurName),current);
                return convertView;
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

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflator.inflate(R.layout.irur_child, parent, false);
                }
                //ImageView btn = (ImageView) convertView.findViewById(R.id.imageButtonMap);
                ((TextView) convertView.findViewById(R.id.irurDetails)).setText(getChild(groupPosition, childPosition).toString());
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public int getChildTypeCount() {
                return 9;
            }
        };
        listView.setAdapter(adp);
        return view;
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

    public static void refreshAdapter(BaseExpandableListAdapter ad, ArrayList originList, ArrayList newList) {
        originList.clear();
        originList.addAll(newList);
        ad.notifyDataSetChanged();
    }
}
