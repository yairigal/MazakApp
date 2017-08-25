package project.android.com.mazak.Model.Entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

    public Grade(String code , String name, String sem, String points, String min, String finalGrade) {
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

    public Grade(String code , String name, String sem, String points, String min, String finalGrade,ArrayList<gradeIngerdiants> ing) {
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

    public Grade(String code , String name, String sem, String points, String min, String finalGrade, ArrayList<gradeIngerdiants> ing, ArrayList<Notebook> notebookLink) {
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

    public Grade(){}

    public void getGradeDetails(MazakConnection connection) throws Exception {
        String params = connection.getQuery(ConnectionData.getGradeDetailsPostArguments(subDetailsID));
        String html = connection.connectPost(ConnectionData.GradesURL,params);
        ArrayList<gradeIngerdiants> newing = ParseGradeDetails(html);
        ingerdiantses.clear();
        ingerdiantses.addAll(newing);
    }

    public ArrayList<gradeIngerdiants> getGradeDetailsExplcit(MazakConnection connection) throws Exception {
        String params = connection.getQuery(ConnectionData.getGradeDetailsPostArguments(subDetailsID));
        String html = connection.connectPost(ConnectionData.GradesURL,params);
        ArrayList<gradeIngerdiants> newing = ParseGradeDetails(html);
        return newing;
    }




    public static ArrayList<gradeIngerdiants> ParseGradeDetails(String html){
        ArrayList<gradeIngerdiants> ing = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.GradeDetailsTableID);
        Elements el = elem.children().get(0).children();
        for(int i=1;i<el.size();i++) {
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


    public static Grade ParseToGrade(Element root){
        Elements el = root.getAllElements();
        Grade grade = new Grade();
        grade.subDetailsID = el.get(2).attr("name");
        grade.code =  el.get(3).text();
        grade.name = el.get(4).child(0).text();
        grade.semester =  el.get(6).text();
        grade.points = el.get(7).text();
        grade.minGrade =  el.get(8).text();
        grade.finalGrade =  el.get(9).text();
        grade.StatLink = "https://mazak.jct.ac.il/Student/"+el.get(16).attr("href");
        return grade;
    }
}
