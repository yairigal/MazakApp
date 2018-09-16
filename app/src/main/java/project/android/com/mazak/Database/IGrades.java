package project.android.com.mazak.Database;

import java.util.ArrayList;
import java.util.HashMap;

import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.NotebookList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.Entities.gradeIngerdiants;
import project.android.com.mazak.Model.Web.MazakAPI;

/**
 * Created by Yair on 2017-03-11.
 */

public interface IGrades {
    void saveGrades(GradesList grades);
    GradesList getGrades(getOptions options) throws Exception;
    HashMap<Integer,GradesList> getGrades(getOptions options, int year) throws Exception;
    GradesList FilterByName(String name);
    void deleteGrades();

    ArrayList<gradeIngerdiants> getGradesParts(Grade currentGrade) throws Exception;

    MazakAPI.Tuple<ArrayList<gradeIngerdiants>,NotebookList> getGradesDetailsAndNotebooks(Grade course) throws Exception;
}
