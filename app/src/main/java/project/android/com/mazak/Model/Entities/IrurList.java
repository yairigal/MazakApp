package project.android.com.mazak.Model.Entities;

import java.util.ArrayList;

/**
 * Created by Yair on 2017-03-11.
 */

public class IrurList extends ObjectList<Irur> {

    public IrurList(ArrayList<Irur> objects, boolean b) {
        super(objects,b);
    }

    public IrurList() {
        super();
    }

    @Override
    public ArrayList<Irur> getList(){
        return (ArrayList<Irur>) list;
    }

    @Override
    public IrurList clone() {
        return new IrurList((ArrayList<Irur>)((ArrayList<Irur>)list).clone(),reversed);
    }

    public boolean equal(IrurList lst2) {
        if (list.size() != lst2.getList().size())
            return false;
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            found = false;
            for (int j = 0; j < lst2.getList().size(); j++) {
                if (list.get(i).equals(lst2.get(j)))
                    found = true;
            }
            if (!found)
                return false;
        }
        return true;
    }
}
