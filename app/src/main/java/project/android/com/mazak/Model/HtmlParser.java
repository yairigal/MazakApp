package project.android.com.mazak.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import project.android.com.mazak.Model.Entities.ClassEvent;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.Irur;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.Entities.gradeIngerdiants;
import project.android.com.mazak.Model.Web.ConnectionData;

import static project.android.com.mazak.Model.Entities.Grade.ParseToGrade;

/**
 * Created by Yair on 2017-02-14.
 */

public class HtmlParser {

    public static  GradesList ParseToGrades(String html) throws ExecutionException, InterruptedException {
       /* AsyncTask<Void,Void,ArrayList<Grade>> as  = getAsyncTask(html);
        as.execute();
        return as.get();*/
        GradesList grades = new GradesList();
        Document doc = null;
        doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.GradesTableID);
        Elements el = elem.children().get(0).children();
        for(int i=1;i<el.size();i++)
            grades.add(ParseToGrade(el.get(i)));
        return grades;
    }

    public static IrurList ParseToIrurs(String html){
        IrurList irurs = new IrurList();
        Document doc = null;
        doc = Jsoup.parse(html);
        //normal irurs.
        addIrurs(ConnectionData.LastAppealsTableID,irurs,doc);
        //to the Rosh mahlaka
        addIrurs(ConnectionData.IrurTableHeadID,irurs,doc);
        //to rosh mahlaka 2
        addIrurs(ConnectionData.IrurTableHeadID2,irurs,doc);
        return irurs;
    }

    private static void addIrurs(String TableId,IrurList lst,Document doc){
        Element elem = doc.getElementById(TableId);
        if(elem != null) {
            Elements el = elem.children().get(0).children();
            for (int i = 1; i < el.size(); i++)
                lst.add(Irur.ParseToIrur(el.get(i)));
        }
    }

    public static ArrayList<gradeIngerdiants> ParseToingerdiants(String html){
        Document doc = null;
        doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.IngredientsTableId);
        Elements el = elem.children().get(0).children();
        return null;
    }

    public static CourseStatistics ParseToStats(String html){
        CourseStatistics statistics = new CourseStatistics();
        Document doc = Jsoup.parse(html);
        Element avg = doc.getElementById(ConnectionData.StatsMeanID);
        Element median = doc.getElementById(ConnectionData.StatsMedianID);
        Element num = doc.getElementById(ConnectionData.StatsNumOfStudntsID);
        statistics.setMean(Float.parseFloat(avg.text()));
        statistics.setMedian(Float.parseFloat(median.text()));
        statistics.setNumOfStudentsWithGrade(Integer.parseInt(num.text()));
        Element elem = doc.getElementById(ConnectionData.StatisticsTableID);
        Elements el = elem.children().get(0).children();
        for(int i=1;i<10;i++) {
            String freq = el.get(i).child(1).text();
            Integer frq = Integer.parseInt(freq);
            statistics.getFreqs().add(frq);
        }
        return statistics;
    }

    public static ScheduleList ParseToClassEvents(String html) {
        ScheduleList sched = new ScheduleList();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.ScheduleTableID);
        Elements el = elem.children().get(2).children();
        for(int i=1;i<el.size();i++)
            sched.add(ClassEvent.ParseToClassEvent(el.get(i).getElementsByAttribute("title")));
        return sched;
    }
}
