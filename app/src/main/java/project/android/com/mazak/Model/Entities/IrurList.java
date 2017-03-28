package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Yair on 2017-03-11.
 */

public class IrurList implements Iterable<Irur>, Serializable {

    ArrayList<Irur> list;
    boolean reversed;


    public IrurList(ArrayList<Irur> lst, boolean isreversed) {
        this.list = lst;
        this.reversed = isreversed;
    }

    public IrurList() {
        list = new ArrayList<>();
        reversed = false;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void reverse() {
        Collections.reverse(list);
        reversed = !reversed;
    }

    public void add(Irur i){
        list.add(i);
    }

    public ArrayList<Irur> getList() {
        return list;
    }

    @Override
    public Iterator<Irur> iterator() {
        return list.iterator();
    }

    public Irur get(int index){
        return list.get(index);
    }

    public boolean equal(IrurList lst2){
        if(list.size() != lst2.getList().size())
            return false;
        boolean found = false;
        for (int i=0;i<list.size();i++){
            found = false;
            for (int j =0;j<lst2.getList().size();j++){
                if(list.get(i).equals(lst2.get(j)))
                    found = true;
            }
            if(!found)
                return false;
        }
        return true;
    }
    public int size(){
        return list.size();
    }

    @Override
    public IrurList clone(){
        return new IrurList((ArrayList<Irur>) list.clone(),reversed);
    }


}
