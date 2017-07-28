package project.android.com.mazak.Controller.GradesView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import project.android.com.mazak.Controller.Statistics.CourseStatisticsActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Adapters.DetailAdapter;
import project.android.com.mazak.Model.Adapters.MyAdapter;
import project.android.com.mazak.Model.Entities.BackgroundTask;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.Entities.gradeIngerdiants;
import project.android.com.mazak.Model.Filter;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.Model.Utility;
import project.android.com.mazak.R;

import static project.android.com.mazak.Controller.GradesView.FatherTab.toggleSpinner;
import static project.android.com.mazak.R.id.courseTv;

public class gradesViewFragment extends Fragment implements ISearch {

    GradesList grades;
    GradesList adapterList;
    ArrayAdapter adapter;
    Database database;
    int year;
    ListView sem0, sem1, sem2;
    GradesList sem0List, sem1List, sem2List;
    private boolean[] animationStates;
    ExpandableLayout currentOpen = null;
    private ScrollView mScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataAndGradesFromBundle();
        animationStates = new boolean[grades.size()];
        try {
            database = Factory.getInstance();
        } catch (Exception e) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grades_view_test, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gradesRecycleView);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollojt);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        sem0List = GradesModel.sortBySemester(grades).get(0);
        sem1List = GradesModel.sortBySemester(grades).get(1);
        sem2List = GradesModel.sortBySemester(grades).get(2);

        MyAdapter mAdapter = new MyAdapter(sem0List, sem1List, sem2List,getContext(),mScrollView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));


/*        mScrollView = (ScrollView) view.findViewById(R.id.scrollojt);
        sem0 = (ListView) view.findViewById(R.id.listSem0);
        sem1 = (ListView) view.findViewById(R.id.listSem1);
        sem2 = (ListView) view.findViewById(R.id.listSem2);
        //get avg
        //HashMap<String, Float> avg = GradesModel.getAvg(grades);
        //addAverageToList(avg);
        sem0List = GradesModel.sortBySemester(grades).get(0);
        sem1List = GradesModel.sortBySemester(grades).get(1);
        sem2List = GradesModel.sortBySemester(grades).get(2);
        sem0.setAdapter(new GradeAdapter(getActivity(), R.layout.fragment_grades_view, sem0List.getList(), sem0));
        sem1.setAdapter(new GradeAdapter(getActivity(), R.layout.fragment_grades_view, sem1List.getList(), sem1));
        sem2.setAdapter(new GradeAdapter(getActivity(), R.layout.fragment_grades_view, sem2List.getList(), sem2));

        Utility.setListViewHeightBasedOnChildren(sem0);
        Utility.setListViewHeightBasedOnChildren(sem1);
        Utility.setListViewHeightBasedOnChildren(sem2);*/

        return view;
    }


    private void addAverageToList(HashMap<String, Float> avg) {
        float avgNum = avg.get("avg");
        try {
            addGrade(new Grade("ממוצע קודש: " + Float.toString(avg.get("kodesh")), "ממוצע אקדמי: " + Float.toString(avgNum).substring(0, 4), "", Float.toString(avg.get("nz")), "", ""));
        } catch (Exception ex) {
            try {
                addGrade(new Grade("ממוצע קודש: " + Float.toString(avg.get("kodesh")), "ממוצע אקדמי: " + Float.toString(avgNum).substring(0, 3), "", Float.toString(avg.get("nz")), "", ""));
            } catch (Exception exp) {
                addGrade(new Grade("ממוצע קודש: " + Float.toString(avg.get("kodesh")), "ממוצע אקדמי: " + Float.toString(avgNum).substring(0, 2), "", Float.toString(avg.get("nz")), "", ""));

            }
        }

    }


    private void getDataAndGradesFromBundle() {
        year = getArguments().getInt("year");
        grades = (GradesList) getArguments().getSerializable("list");
        adapterList = (GradesList) grades.clone();
    }

    private void getGradesList() throws Exception {
        HashMap<Integer, GradesList> list = database.getGrades(getOptions.fromMemory, year);
        grades = new GradesList();
        for (int i = 1; i <= list.size(); i++)
            grades.addAll(list.get(i));
        adapterList = new GradesList(grades.getList(), grades.isReversed());
    }

    void showDialog(final Grade gd) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_list);
        ((TextView) dialog.findViewById(R.id.dialog1)).setText(gd.name);
        ((TextView) dialog.findViewById(R.id.dialog2)).setText(gd.code);
        ((TextView) dialog.findViewById(R.id.dialog3)).setText("נ''ז: " + gd.points);
        ((TextView) dialog.findViewById(R.id.dialog4)).setText("סמסטר: " + gd.semester);
        ((TextView) dialog.findViewById(R.id.dialog5)).setText("ציון סופי: " + gd.finalGrade);

        final ProgressBar spinner = (ProgressBar) dialog.findViewById(R.id.pbGradeDetails);
        final ListView listView = (ListView) dialog.findViewById(R.id.detailsListView);


        new BackgroundTask(new Delegate() {
            @Override
            public void function(Object obj) {
                toggleSpinner(true, listView, spinner);
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                try {
                    gd.getGradeDetails(database.getConnection());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                toggleSpinner(false, listView, spinner);
                ArrayList<String> afterList = convertToStringArray(gd.ingerdiantses);
                final ArrayAdapter adp = new DetailAdapter(getActivity(), R.layout.grade_detail, gd.ingerdiantses);
                listView.setAdapter(adp);
            }
        }).start();
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

    private ArrayList<String> convertToStringArray(ArrayList<gradeIngerdiants> ingerdiantses) {
        ArrayList<String> list = new ArrayList<>();
        list.add("סוג");
        list.add("משקל");
        list.add("מועד א");
        list.add("מועד ב");
        list.add("מועד מיוחד");
        list.add("מועד ג");
        list.add("ציון מינימום");
        for (gradeIngerdiants i : ingerdiantses) {
            list.add(i.type);
            String w = i.weight;
            float we = Float.valueOf(w);
            int wei = (int) we;
            String last = String.valueOf(wei) + " %";
            list.add(last);
            list.add(i.moedA);
            list.add(i.moedB);
            list.add(i.moedSpecial);
            list.add(i.moedC);
            list.add(i.minGrade);
        }
        return list;
    }

    private void openStatisticsActivity(Grade gd, Dialog dialog) {
        Intent intent = new Intent(getActivity(), CourseStatisticsActivity.class);
        intent.putExtra("grade", gd);
        dialog.dismiss();
        startActivity(intent);
    }

    public static void refreshAdapter(ArrayAdapter adp) {
        adp.notifyDataSetChanged();
    }

    void reLoadGrades(GradesList org, ArrayAdapter adp) {
        adapterList = org;
        refreshAdapter(adp);
    }

    void addGrade(Grade g) {
        grades.add(g);
        adapterList.add(g);
    }

    @Override
    public void Filter(String query) {
        Filter filter = new Filter(grades);
        GradesList newGradesList;
        newGradesList = filter.filter(query);
        adapterList = newGradesList;
        refreshAdapter(adapter);
    }

    @Override
    public void clearFilter() {
        reLoadGrades(grades, adapter);
    }

    @Override
    public void Refresh() {

    }

    class GradeAdapter extends ArrayAdapter<Grade> {

        ArrayList<Grade> list;
        ListView currentListView;

        public GradeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Grade> objects, ListView listview) {
            super(context, resource, objects);
            this.list = (ArrayList<Grade>) objects;
            this.currentListView = listview;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade_view, parent, false);

/*                   if (!animationStates[position]) {
                        Log.e("TAG", "Animating item no: " + position);
                        animationStates[position] = true;
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                        animation.setStartOffset(position*80);
                        convertView.startAnimation(animation);
                    }*/
                Grade current = list.get(position);
                setNz(convertView, current);
                setStatsButton(current, convertView);
                setName(convertView, courseTv, current.name);
                setOnClick(convertView, current, position);
                setBackgroundColor(position, convertView);
                return convertView;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return convertView;
        }

        private void setDetails(final View convertView, final Grade gd) {
            //((TextView) convertView.findViewById(R.id.nameDetails)).setText(gd.name);
            ((TextView) convertView.findViewById(R.id.courseIdDetails)).setText(gd.code);
            ((TextView) convertView.findViewById(R.id.courseFinalGrade)).setText("ציון סופי " + gd.finalGrade);

            ((TextView) convertView.findViewById(R.id.Type_details)).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView.findViewById(R.id.Weight_details)).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView.findViewById(R.id.MinGrade_Details)).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView.findViewById(R.id.MoedA_detials)).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView.findViewById(R.id.MoedB_Details)).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView.findViewById(R.id.MoedC_Details)).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView.findViewById(R.id.MoedSpec_Details)).setTypeface(null, Typeface.BOLD);

            final ListView detailsListView = (ListView) convertView.findViewById(R.id.detailsListView);
            final ProgressBar spinner = (ProgressBar) convertView.findViewById(R.id.detailsProgressBar);
            final ArrayAdapter adp = new DetailAdapter(getActivity(), R.layout.grade_detail, gd.ingerdiantses);
            detailsListView.setAdapter(adp);

            new BackgroundTask(new Delegate() {
                @Override
                public void function(Object obj) {
                    toggleSpinner(true, detailsListView, spinner);
                }
            }, new Delegate() {
                @Override
                public void function(Object obj) {
                    try {
                        gd.getGradeDetails(database.getConnection());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Delegate() {
                @Override
                public void function(Object obj) {
                    toggleSpinner(false, detailsListView, spinner);
                    refreshAdapter(adp);
                    final ExpandableLayout layout = (ExpandableLayout) convertView.findViewById(R.id.ExpadAbleLayout_Details);
                    Utility.setListViewHeightBasedOnChildren(detailsListView);
                    Utility.setListViewHeightBasedOnChildrenWithDetailsListView(currentListView, detailsListView, layout, true);
                }
            }).start();

        }

        private void setNz(View convertView, Grade current) {
            TextView Nz = (TextView) convertView.findViewById(R.id.NZTV);
            float num = Float.parseFloat(current.points);
            try {
                if (num == (int) num)
                    Nz.setText((int) num + " נ''ז");
                else
                    Nz.setText(num + " נ''ז");
            } catch (Exception e) {
                Nz.setText(num + " נ''ז");
            }
        }

        private void setName(View convertView, int courseTv, String name2) {
            TextView name = (TextView) convertView.findViewById(courseTv);
            name.setText(name2);
        }

        private void setOnClick(final View convertView, final Grade grade, final int position) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showDialog(grade);
                    final ExpandableLayout layout = (ExpandableLayout) convertView.findViewById(R.id.ExpadAbleLayout_Details);
                    layout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                        @Override
                        public void onExpansionUpdate(float expansionFraction, int state) {
                            switch (state) {
                                case 0: // done collapsing
                                    Utility.setListViewHeightBasedOnChildrenWithExpandable(currentListView, layout, false);
                                    break;
                                case 3: // done expanding
                                    Utility.setListViewHeightBasedOnChildrenWithExpandable(currentListView, layout, true);
                                    //if its the last value , scroll to bottom.
                                    if (grade.equals(sem2List.get(sem2List.size() - 1))) // last value
                                        scollDown();
                                    break;
                            }
                        }
                    });

                    if (layout.isExpanded()) {
                        layout.collapse();
                        currentOpen = null;
                    } else { // if the layout is about to be expanded

                        //check when it is expanded or collapsed and then change the listview size

                        //if someone else is opened -> close it.
                        if (currentOpen != null)
                            currentOpen.collapse();
                        currentOpen = layout;


                        setDetails(convertView, grade);
                        layout.expand();

                    }
                }

                private void scollDown() {
                    mScrollView.post(new Runnable() {
                        public void run() {
                            mScrollView.fullScroll(mScrollView.FOCUS_DOWN);
                        }
                    });
                }
            });
        }

        private void setBackgroundColor(int position, View convertView) {
/*        if (position % 2 == 1)
            convertView.findViewById(R.id.gradeCard).setBackgroundColor(Color.rgb(194, 217, 237));
        else
            convertView.findViewById(R.id.gradeCard).setBackgroundColor(Color.WHITE);*/
        }

        private void setStatsButton(final Grade position, View convertView) {
            (convertView.findViewById(R.id.statsBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CourseStatisticsActivity.class);
                    intent.putExtra("grade", position);
                    startActivity(intent);
                }
            });
        }

    }




}
