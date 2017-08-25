package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yair on 2017-04-28.
 */

public abstract class ObjectList<T> implements Iterable<T>,Serializable {
    List<T> list;
    boolean reversed;

    public ObjectList(List<T> lst, boolean rev) {
        this.list = lst;
        this.reversed = rev;
    }

    public ObjectList() {
        list = new ArrayList<>();
        reversed = false;
    }

    @Override
    public Iterator<T> iterator() {
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

    public T get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public void addAll(ObjectList<T> grades) {
        if(grades != null)
            this.list.addAll(grades.list);
    }

    public List<T> getList() {
        return list;
    }

    public void add(T g){
        this.list.add(g);
    }

    public abstract Object clone();
}
