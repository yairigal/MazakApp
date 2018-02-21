package project.android.com.mazak.Model.Entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yair on 2017-08-03.
 */

public class NotebookList extends ObjectList<Notebook>  {


    public NotebookList(){super();}

    public NotebookList(NotebookList notebooks) {
        this.list = new ArrayList<>();
        for (Notebook n:notebooks) {
            list.add(n);
        }
    }

    @Override
    public NotebookList clone() {
        return new NotebookList(this);
    }
}
