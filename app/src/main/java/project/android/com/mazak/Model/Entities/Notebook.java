package project.android.com.mazak.Model.Entities;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

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
        Node el = rootElement.childNode(1);
        Node el2 = el.childNode(1);
        String at = el2.attr("href");
        String link = at.substring(25,at.length()-5);
        String moed = ((TextNode)rootElement.childNode(13).childNode(0)).text().trim();
        String code = ((TextNode)rootElement.childNode(5).childNode(0)).text().trim();
        String time = ((TextNode)rootElement.childNode(15).childNode(0)).text().trim();
        Notebook n = new Notebook();
        n.link = link;
        n.moed = moed;
        n.code = code;
        n.id = String.valueOf(id);
        n.time = time.replace(" ","_").replace(":","").replace("/","");
        return n;
    }
}
