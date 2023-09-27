package project.android.com.mazak.Model;

import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;

/**
 * Created by Yair on 2017-02-14.
 */

public class Filter {
    private GradesList rawData;

    public Filter(GradesList rawData){
        this.rawData = rawData;
    }
    public GradesList filter(String query){
       GradesList lst = new GradesList();
        for (Grade i:rawData) {
            if (i.name.contains(query)) {
                lst.add(i);
            }
        }
        return lst;
    }
}
