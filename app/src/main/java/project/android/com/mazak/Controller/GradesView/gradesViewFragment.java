package project.android.com.mazak.Controller.GradesView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;

import project.android.com.mazak.Controller.Statistics.CourseStatisticsActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Adapters.MyAdapter;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.gradeIngredients;
import project.android.com.mazak.Model.Filter;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.R;

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
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataAndGradesFromBundle();
        animationStates = new boolean[grades.size()];
        try {
            database = Factory.getInstance();
        } catch (Exception e) {
            Log.e("GradesViewFragment", "onCreate: ", e);
            Toast.makeText(getContext(), getString(R.string.error_getting_grades), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_grades_view_test, container, false);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gradesRecycleView);
            mScrollView = (ScrollView) view.findViewById(R.id.scrollojt);


            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);

            sem0List = GradesModel.sortBySemester(grades).get(0);
            sem1List = GradesModel.sortBySemester(grades).get(1);
            sem2List = GradesModel.sortBySemester(grades).get(2);

            MyAdapter mAdapter = new MyAdapter(sem0List, sem1List, sem2List, getContext(), mScrollView);
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
        }

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

    private ArrayList<String> convertToStringArray(ArrayList<gradeIngredients> ingerdiantses) {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.type));
        list.add(getString(R.string.weight));
        list.add(getString(R.string.moed_a));
        list.add(getString(R.string.moed_b));
        list.add(getString(R.string.special_test));
        list.add(getString(R.string.moed_c));
        list.add(getString(R.string.min_grade));
        for (gradeIngredients i : ingerdiantses) {
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

}
