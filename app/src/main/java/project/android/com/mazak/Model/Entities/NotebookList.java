package project.android.com.mazak.Model.Entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yair on 2017-08-03.
 */

public class NotebookList  {
    HashMap<String,ArrayList<Notebook>> map;

    public NotebookList(HashMap<String,ArrayList<Notebook>> map){
        this.map = map;
    }

    public NotebookList(){
        this.map = new HashMap<>();
    }

    public ArrayList<Notebook> get(String code){
        return map.get(code);
    }

    public int size(){
        return map.size();
    }

    public HashMap getMap(){
        return map;
    }
}
