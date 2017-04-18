package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ScheduleList implements Iterable<ClassEvent>, Serializable {
    List<ClassEvent> list;

    public ScheduleList(List<ClassEvent> lst) {
        this.list = lst;
    }

    public ScheduleList() {
        list = new ArrayList<>();
    }



    @Override
    public Iterator<ClassEvent> iterator() {
        return list.iterator();
    }

    public void reverse() {
        if(list != null && list.size() != 0) {
            Collections.reverse(list);
        }
    }

    public ClassEvent get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public void addAll(ScheduleList grades) {
        if(grades != null)
            this.list.addAll(grades.list);
    }

    public List<ClassEvent> getList() {
        return list;
    }

    public void add(ClassEvent g){
        this.list.add(g);
    }

    public ScheduleList clone(){
        return new ScheduleList(new ArrayList<>(list));
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
}
