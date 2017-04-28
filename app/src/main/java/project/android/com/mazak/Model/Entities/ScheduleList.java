package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ScheduleList extends ObjectList<ClassEvent>{

    public ScheduleList(List<ClassEvent> clone, boolean reversed) {
        super(clone,reversed);
    }

    public ScheduleList() {
        super();
    }

    public ScheduleList getEventsByDay(int position) {
        ScheduleList lst = new ScheduleList();
        for (ClassEvent c:list)
            if(c.day == position)
                lst.add(c);
        lst.sort();
        return lst;
    }

    public void sort(){
        Collections.sort(list);
    }

    public int getNumOfDays() {
        HashMap<Integer,Integer> map = new HashMap<>();
        for (ClassEvent c:
             list) {
            map.put(c.day,0);
        }
        return map.size();
    }

    @Override
    public ScheduleList clone() {
        return new ScheduleList((List<ClassEvent>)((ArrayList<ClassEvent>)list).clone(),reversed);
    }
}
