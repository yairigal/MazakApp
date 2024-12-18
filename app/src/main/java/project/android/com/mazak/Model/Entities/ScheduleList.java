package project.android.com.mazak.Model.Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ScheduleList extends ObjectList<ClassEvent> {

    public ScheduleList(List<ClassEvent> clone, boolean reversed) {
        super(clone, reversed);
    }

    public ScheduleList() {
        super();
    }

    public ScheduleList getEventsByDay(int position) {
        ScheduleList lst = new ScheduleList();
        for (ClassEvent c : list) {
            if (c.day == position) {
                lst.add(c);
            }
        }
        lst.sort();
        return lst;
    }

    public void sort() {
        Collections.sort(list);
    }

    public int getNumOfDays() {
        int maxday = -1;
        for (ClassEvent c : list) {
            if (c.day >= maxday) maxday = c.day;
        }
        if (maxday > 5) {
            return maxday;
        }
        return 6;
    }

    @Override
    public ScheduleList clone() {
        return new ScheduleList((List<ClassEvent>) ((ArrayList<ClassEvent>) list).clone(), reversed);
    }
}
