package project.android.com.mazak.Model.Entities;

import org.jsoup.nodes.Element;
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
    public ArrayList<gradeIngerdiants> ingerdiantses;
    public String StatLink;

    public Grade(String code , String name, String sem, String points, String min, String finalGrade) {
        this.code = code;
        this.name = name;
        this.semester = sem;
        this.points = points;
        this.minGrade = min;
        this.finalGrade = finalGrade;
        this.subDetailsID = "";
        this.StatLink = "";
    }

    public Grade(){}

    public static Grade ParseToGrade(Element root){
        Elements el = root.getAllElements();
        Grade grade = new Grade();
        grade.subDetailsID = el.get(2).attr("id");
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
