package project.android.com.mazak.Database;

import java.util.HashMap;

import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.getOptions;

/**
 * Created by Yair on 2017-03-11.
 */

public interface IGrades {
    void saveGrades(GradesList grades);
    GradesList getGrades(getOptions options) throws Exception;
    HashMap<Integer,GradesList> getGrades(getOptions options, int year) throws Exception;
    GradesList FilterByName(String name);
    void deleteGrades();
}
