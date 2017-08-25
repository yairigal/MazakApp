package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Yair on 2017-02-22.
 */

public class GradesList extends ObjectList<Grade> {
    public GradesList(List<Grade> list, boolean reversed) {
        super(list,reversed);
    }

    public GradesList() {
        super();
    }

    @Override
    public GradesList clone() {
        return new GradesList((List<Grade>)((ArrayList<Grade>)list).clone(),reversed);
    }

    public Map<String,Grade> toHashtable(){
        Map<String,Grade> map = new Hashtable<>();
        for (Grade g:list) {
            map.put(g.code,g);
        }
        return map;
    }
}
