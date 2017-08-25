package project.android.com.mazak.Model.Entities;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;

import project.android.com.mazak.Model.GradesModel;

/**
 * Created by Yair on 2017-08-03.
 */

public class Notebook implements Serializable {
    public String link;
    public String moed;
    public String code;
    public String id;
    public String time;



    public static Notebook setupNotebookLink(Element rootElement,int id){
        String link = rootElement.child(0).child(0).attr("id").replace("_","%");
        String moed = rootElement.child(6).text();
        String code = rootElement.child(2).text();
        String time = rootElement.child(8).text();
        Notebook n = new Notebook();
        n.link = link;
        n.moed = moed;
        n.code = code;
        n.id = String.valueOf(id);
        n.time = time.replace(" ","_").replace(":","").replace("/","");
        return n;
    }
}
