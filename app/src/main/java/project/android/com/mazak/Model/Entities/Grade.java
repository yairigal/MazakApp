package project.android.com.mazak.Model.Entities;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;


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
    public String actualCourseID;
    public String droppedOut;

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

    public static Grade ParseToGrade(Object root) throws JSONException {
        JSONObject object = (JSONObject) root;
        Grade grade = new Grade();
        grade.actualCourseID = object.getString("actualCourseID");
        grade.subDetailsID = "https://mazak.jct.ac.il/Student/Modals/StudentCoursePartGradePage.aspx?actualCourseID="+grade.actualCourseID;
        grade.code = object.getString("actualCourseFullNumber");
        grade.name = object.getString("courseName");
        grade.semester = object.getString("semesterName");
        grade.points = object.getString("effectiveCredits");
        grade.minGrade = object.getString("effectiveMinGrade");
        grade.finalGrade = object.getString("finalGradeName");
        grade.StatLink = "https://mazak.jct.ac.il/Student/GradesCharts.aspx?ActualCourseID="+grade.actualCourseID;
        return grade;
    }
}
