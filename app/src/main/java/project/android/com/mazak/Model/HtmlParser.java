package project.android.com.mazak.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Model.Entities.ClassEvent;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.Irur;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.Entities.Test;
import project.android.com.mazak.Model.Entities.TestList;
import project.android.com.mazak.Model.Entities.TfilaTime;
import project.android.com.mazak.Model.Entities.gradeIngerdiants;
import project.android.com.mazak.Model.Web.ConnectionData;

import static project.android.com.mazak.Model.Entities.Grade.ParseToGrade;

/**
 * Created by Yair on 2017-02-14.
 */

public class HtmlParser {
    /**
     * Parsing the html page and returning the grades from it
     * @param html
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * arsing the html page and returning the irurs from it
     * @param html
     * @return
     */
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

    /**
     * adds the irurs from the document to the list.
     * @param TableId
     * @param lst
     * @param doc
     */
    private static void addIrurs(String TableId,IrurList lst,Document doc){
        Element elem = doc.getElementById(TableId);
        if(elem != null) {
            Elements el = elem.children().get(0).children();
            for (int i = 1; i < el.size(); i++)
                lst.add(Irur.ParseToIrur(el.get(i)));
        }
    }

    /**
     *
     * @param html
     * @return
     */
    public static ArrayList<gradeIngerdiants> ParseToingerdiants(String html){
        Document doc = null;
        doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.IngredientsTableId);
        Elements el = elem.children().get(0).children();
        return null;
    }

    /**
     * arsing the html page and returning the statistics from it
     * @param html
     * @return
     */
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

    public static ScheduleList ParseToClassEvents1(String html) {
        ScheduleList sched = new ScheduleList();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.ScheduleTableID);
        Elements el = elem.children().get(2).children();
        for(int i=1;i<el.size();i++)
            sched.add(ClassEvent.ParseToClassEvent1(el.get(i).getElementsByAttribute("title")));
        return sched;
    }

    /**
     * arsing the html page and returning the classes  from it
     * @param html
     * @return
     */
    public static ScheduleList ParseToClassEvents(String html) {
        ScheduleList sched = new ScheduleList();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.ScheduleListTableID);
        Elements el = elem.children().get(0).children();
        for(int i=2;i<el.size();i++)
            //sched.add(ClassEvent.ParseToClassEvent(el.get(i).getElementsByAttribute("title")));
            sched.add(ClassEvent.ParseToClassEvent(el.get(i)));
        return sched;
    }

    /**
     * parses the html page to find the url to the schedule page that is in words and not pictures
     * @param html
     * @return
     */
    public static String getListScheudleURL(String html) {
        String year=null,sem=null;
        Document doc = Jsoup.parse(html);
        Element root = doc.getElementById(ConnectionData.ScheduleYearCBID);
        Elements childs = root.children();
        for (Element e:childs) {
            String Selected = "";
            Selected = e.getElementsByAttribute("selected").val();
            if(!Selected.equals("")){
                year = Selected;
                break;
            }
        }
        root = doc.getElementById(ConnectionData.ScheduleSemesterCBID);
        childs = root.children();
        for (Element e:childs) {
            String Selected = "";
            Selected = e.getElementsByAttribute("selected").val();
            if(!Selected.equals("")){
                sem = Selected;
                break;
            }
        }
        return String.format("https://mazak.jct.ac.il/Student/ScheduleList.aspx?AcademicYearID=%s&SemesterID=%s",year,sem);
    }

    /**
     * arsing the html page and returning the Tfila times from it
     * @param html
     * @return
     */
    public static ArrayList<TfilaTime> ParseToTfilaTime(String html){
        ArrayList<TfilaTime> times = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementById("times");
        Elements el = elem.children().get(0).children().get(0).children();
        for(int i=1;i<el.size()-1;i++) {
            Elements childs = el.get(i).children();
            //sched.add(ClassEvent.ParseToClassEvent(el.get(i).getElementsByAttribute("title")));
            boolean value = false;
            if(!childs.get(0).text().equals(""))
                value = true;
            times.add(new TfilaTime(childs.get(1).text(),childs.get(2).text(),value));
        }
        return times;
    }

    /**
     * arsing the html page and returning the tests from it
     * @param html
     * @return
     */
    public static TestList ParseToTests(String html) {
        TestList times = new TestList();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.TestsTableID);
        Elements el = elem.children().get(0).children();
        for(int i=1;i<el.size();i++) {
            times.add(Test.ParseToTest(el.get(i)));
        }
        return times;
    }

    /**
     * a general function for all the parses by their keys.
     * @param html
     * @param key
     * @return
     * @throws Exception
     */
    public static Object Parse(String html,String key) throws Exception {
        switch (key){
            case InternalDatabase.gradesKey:
                return ParseToGrades(html);
            case InternalDatabase.IrursKey:
                return ParseToIrurs(html);
            case InternalDatabase.ScheduleKey:
                return ParseToClassEvents(html);
            case InternalDatabase.TestKey:
                return ParseToTests(html);
            default:
                return null;
        }
    }
}
