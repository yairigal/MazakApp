package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Yair on 2017-02-22.
 */

public class GradesList implements Iterable<Grade>, Serializable {
   ArrayList<Grade> list;
    boolean reversed;

    public GradesList(ArrayList<Grade> lst, boolean rev) {
        this.list = lst;
        this.reversed = rev;
    }

    public GradesList() {
        list = new ArrayList<>();
        reversed = false;
    }

    @Override
    public Iterator<Grade> iterator() {
        return list.iterator();
    }

    public void reverse() {
        if(list != null && list.size() != 0) {
            Collections.reverse(list);
            reversed = !reversed;
        }
    }

    public boolean isReversed() {
        return reversed;
    }

    public Grade get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public void addAll(GradesList grades) {
        if(grades != null)
            this.list.addAll(grades.list);
    }

    public ArrayList<Grade> getList() {
        return list;
    }

    public void add(Grade g){
        this.list.add(g);
    }

    public GradesList clone(){
        return new GradesList(new ArrayList<Grade>(list),false);
    }
}
