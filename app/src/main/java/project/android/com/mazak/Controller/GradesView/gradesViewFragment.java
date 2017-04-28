package project.android.com.mazak.Controller.GradesView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.android.com.mazak.Controller.Statistics.CourseStatisticsActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.Filter;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.Model.Utility;
import project.android.com.mazak.R;

public class gradesViewFragment extends Fragment implements ISearch {

    GradesList grades;
    GradesList adapterList;
    ArrayAdapter adapter;
    Database database;
    int year;
    private boolean[] animationStates;

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

        View view = inflater.inflate(R.layout.fragment_grades_view, container, false);
        ListView sem0 = (ListView) view.findViewById(R.id.listSem0);
        ListView sem1 = (ListView) view.findViewById(R.id.listSem1);
        ListView sem2 = (ListView) view.findViewById(R.id.listSem2);
        //get avg
        //HashMap<String, Float> avg = GradesModel.getAvg(grades);
        //addAverageToList(avg);
        GradesList sem0List = GradesModel.sortBySemester(grades).get(0);
        GradesList sem1List = GradesModel.sortBySemester(grades).get(1);
        GradesList sem2List = GradesModel.sortBySemester(grades).get(2);
        sem0.setAdapter(new GradeAdapter(getActivity(),R.layout.fragment_grades_view,sem0List.getList()));
        sem1.setAdapter(new GradeAdapter(getActivity(),R.layout.fragment_grades_view,sem1List.getList()));
        sem2.setAdapter(new GradeAdapter(getActivity(),R.layout.fragment_grades_view,sem2List.getList()));

        Utility.setListViewHeightBasedOnChildren(sem0);
        Utility.setListViewHeightBasedOnChildren(sem1);
        Utility.setListViewHeightBasedOnChildren(sem2);

        return view;
    }



    private void addAverageToList(HashMap<String, Float> avg) {
        float avgNum = avg.get("avg");
        try {
            addGrade(new Grade("ממוצע קודש: " + Float.toString(avg.get("kodesh")), "ממוצע אקדמי: " + Float.toString(avgNum).substring(0, 4), "", Float.toString(avg.get("nz")), "", ""));
        } catch (Exception ex) {
            try {
                addGrade(new Grade("ממוצע קודש: " + Float.toString(avg.get("kodesh")), "ממוצע אקדמי: " + Float.toString(avgNum).substring(0, 3), "", Float.toString(avg.get("nz")), "", ""));
            }catch (Exception exp){
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
        ((TextView)dialog.findViewById(R.id.dialog1)).setText(gd.name);
        ((TextView)dialog.findViewById(R.id.dialog2)).setText(gd.code);
        ((TextView)dialog.findViewById(R.id.dialog3)).setText("נ''ז: " + gd.points);
        ((TextView)dialog.findViewById(R.id.dialog4)).setText("סמסטר: " + gd.semester);
        ((TextView)dialog.findViewById(R.id.dialog5)).setText("ציון סופי: " + gd.finalGrade);
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

    private void openStatisticsActivity(Grade gd, Dialog dialog) {
        Intent intent = new Intent(getActivity(), CourseStatisticsActivity.class);
        intent.putExtra("grade",gd);
        dialog.dismiss();
        startActivity(intent);
    }

    void refreshAdapter(ArrayAdapter adp) {
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

        public GradeAdapter(Context context, int resource, List<Grade> objects) {
            super(context, resource, objects);
            this.list = (ArrayList<Grade>) objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade, parent, false);

/*                    if (!animationStates[position]) {
                        Log.e("TAG", "Animating item no: " + position);
                        animationStates[position] = true;
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                        animation.setStartOffset(position*80);
                        convertView.startAnimation(animation);
                    }*/
                Grade current = list.get(position);

                setNz(convertView, current);
                setStatsButton(current, convertView);
                setName(convertView, R.id.courseTv, current.name);
                setOnClick(convertView,current);
                setBackgroundColor(position, convertView);
                return convertView;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return convertView;
        }

        private void setNz(View convertView, Grade current) {
            TextView Nz = (TextView) convertView.findViewById(R.id.NZTV);
            float num = Float.parseFloat(current.points);
            try {
                if(num == (int)num)
                    Nz.setText((int)num + " נ''ז");
                else
                    Nz.setText(num + " נ''ז");
            }catch (Exception e){
                Nz.setText(num + " נ''ז");
            }
        }

        private void setName(View convertView, int courseTv, String name2) {
            TextView name = (TextView) convertView.findViewById(courseTv);
            name.setText(name2);
        }

        private void setOnClick(View convertView, final Grade grade) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(grade);
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
                    intent.putExtra("grade",position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getCount() {
            if (list != null)
                return list.size();
            return 0;
        }
    }
}
