package project.android.com.mazak.Model.Entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.Model.Web.ConnectionData;
import project.android.com.mazak.Model.Web.MazakConnection;

/**
 * Created by Yair on 2017-02-14.
 */

public class Grade implements Serializable {
    public String code;
    public String name;
    public String semester;
    public String points;
    public String minGrade;
    public String finalGrade;
    public String subDetailsID;
    public ArrayList<gradeIngerdiants> ingerdiantses = new ArrayList<>();
    public String StatLink;
    public ArrayList<Notebook> Notebook = new ArrayList<>();

    public Grade(String code, String name, String sem, String points, String min, String finalGrade) {
        this.code = code;
        this.name = name;
        this.semester = sem;
        this.points = points;
        this.minGrade = min;
        this.finalGrade = finalGrade;
        this.subDetailsID = "";
        this.StatLink = "";
        this.Notebook = null;
    }

    public Grade(String code, String name, String sem, String points, String min, String finalGrade, ArrayList<gradeIngerdiants> ing) {
        this.code = code;
        this.name = name;
        this.semester = sem;
        this.points = points;
        this.minGrade = min;
        this.finalGrade = finalGrade;
        this.subDetailsID = "";
        this.StatLink = "";
        this.ingerdiantses = ing;
        this.Notebook = null;
    }

    public Grade(String code, String name, String sem, String points, String min, String finalGrade, ArrayList<gradeIngerdiants> ing, ArrayList<Notebook> notebookLink) {
        this.code = code;
        this.name = name;
        this.semester = sem;
        this.points = points;
        this.minGrade = min;
        this.finalGrade = finalGrade;
        this.subDetailsID = "";
        this.StatLink = "";
        this.ingerdiantses = ing;
        this.Notebook = (ArrayList<project.android.com.mazak.Model.Entities.Notebook>) notebookLink.clone();
    }

    public Grade() {
    }

    public void getGradeDetails(MazakConnection connection) throws Exception {
        //String params = connection.getQuery(ConnectionData.getGradeDetailsPostArguments(subDetailsID));
        String html = connection.connect(subDetailsID);
        ArrayList<gradeIngerdiants> newing = ParseGradeDetails(html);
        ingerdiantses.clear();
        ingerdiantses.addAll(newing);
    }

    public ArrayList<gradeIngerdiants> getGradeDetailsAndNotebooksExplicit(MazakConnection connection) throws Exception {
        //String params = connection.getQuery(ConnectionData.getGradeDetailsPostArguments(subDetailsID));
        String html = connection.connect(subDetailsID);
        ArrayList<gradeIngerdiants> newing = ParseGradeDetails(html);
        try {
            this.getNotebooksLinks(html);
        }catch (Exception ex){}
        return newing;
    }

    private void getNotebooksLinks(String html) {
        Document doc = Jsoup.parse(html);
        Elements elem = doc.getAllElements();
        Elements currents = null;
        boolean found = false;
        //searching for the corresponding table
        for (Element e : elem) {
            if (e.className().equals("table-responsive")) {
                currents = e.child(0).child(1).children();
                if (found)
                    break;
                found = true;
            }
        }

        for(Element e:currents){
            project.android.com.mazak.Model.Entities.Notebook n = new Notebook();
            n.code = this.code;
            n.moed = e.child(2).text().trim();
            n.time = e.child(3).text().trim();
            n.time = n.time.replace(" ","_").replace(":","").replace("/","");
            n.link = "https://mazak.jct.ac.il"+e.child(0).child(0).attr("href");
            this.Notebook.add(n);
        }

    }

    public static ArrayList<gradeIngerdiants> ParseGradeDetails(String html) {
        ArrayList<gradeIngerdiants> ing = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elem = doc.getAllElements();
        Elements currents = null;
        for (Element e : elem) {
            if (e.className().equals("table-responsive")) {
                currents = e.child(0).child(1).children();
                break;
            }
        }
        Elements el = currents;
        for (int i = 0; i < el.size(); i++) {
            Elements current = el.get(i).getAllElements();
            gradeIngerdiants toAdd = new gradeIngerdiants();
            toAdd.type = current.get(1).text();
            toAdd.minGrade = current.get(2).text();
            toAdd.weight = current.get(3).text();
            toAdd.moedA = current.get(4).text();
            toAdd.moedB = current.get(5).text();
            toAdd.moedSpecial = current.get(6).text();
            toAdd.moedC = current.get(7).text();
            ing.add(toAdd);
        }

        return ing;
    }

    public static Grade ParseToGrade(Element root) {
        Elements el = root.children();
        Grade grade = new Grade();
        Element a = el.get(0);
        Node b = a.childNode(1);
        Attributes c = b.attributes();
        grade.subDetailsID = "https://mazak.jct.ac.il" + c.get("onclick").substring(11, c.get("onclick").length() - 2);
        grade.code = el.get(1).text();
        grade.name = el.get(2).child(0).text();
        grade.semester = el.get(3).text();
        grade.points = el.get(4).text();
        grade.minGrade = el.get(5).text();
        grade.finalGrade = el.get(6).text();
        grade.StatLink = "https://mazak.jct.ac.il" + el.get(9).childNode(1).attr("href").substring(2);
        return grade;
    }
}
