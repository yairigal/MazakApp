package project.android.com.mazak.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Model.Entities.ClassEvent;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.Irur;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.Entities.Notebook;
import project.android.com.mazak.Model.Entities.NotebookList;
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
        Element el1 = doc.getElementById("dvGrades");
        Element el2 = el1.child(2);
        Element el3 = el2.child(0);
        Element el4 = el3.child(1);
        Elements el = el4.children();
        for(int i=0;i<el.size();i++)
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
        //right now appeals
        Elements tables = doc.select("table.table");
        for (Element tab:tables)
            addIrurs(tab,irurs);
        return irurs;
    }

    /**
     * adds the irurs from the document to the list.
     * @param root
     * @param lst
     */
    private static void addIrurs(Element root,IrurList lst){
        if(root != null) {
            Elements el = root.children().get(1).children();
            for (int i = 0; i < el.size(); i++)
                //checking its not a not sent irur
                if(el.get(i).children().size()>10)
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
        Elements elem = doc.select("table.table");
        Elements el = elem.get(0).child(1).children();
        for(int i=0;i<9;i++) {
            String freq = ((Element)el.get(i).childNode(3).childNode(1)).text();
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
        Element elem = doc.select("table.table").get(0);
        List<Node> el = elem.childNode(3).childNodes();
        for(int i=0;i<el.size();i+=2)
            //sched.add(ClassEvent.ParseToClassEvent(el.get(i).getElementsByAttribute("title")));
            sched.add(ClassEvent.ParseToClassEvent((Element) el.get(i)));
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
        Element elem = doc.select("table.table").get(0);
        Elements el = elem.children().get(1).children();
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
            case InternalDatabase.NotebookKey:
                return ParseToNotebooks(html);
            default:
                return null;
        }
    }

    private static NotebookList ParseToNotebooks(String html) {
        HashMap<String,ArrayList<Notebook>> map = new HashMap<>();
        int id = 0;
        Document doc = Jsoup.parse(html);
        Elements elem = doc.getAllElements();
        Elements currents = null;
        for (Element e : elem) {
            if (e.className().equals("table-responsive")) {
                currents = e.child(0).child(1).children();
                break;
            }
        }

        Elements listOfNotebooks = currents;
        for(int i=0;i<listOfNotebooks.size();i++){
            String code = ((TextNode)(listOfNotebooks.get(i).childNode(5).childNode(0))).text().trim();
            Notebook newN = Notebook.setupNotebookLink(listOfNotebooks.get(i),id++);
            if(map.containsKey(code)){
                map.get(code).add(newN);
            }else {
                ArrayList<Notebook> list = new ArrayList<>();
                list.add(newN);
                map.put(code,list);
            }
        }
        return new NotebookList(map);
    }

/*    public static void setupAllGradesNotebookLinks(GradesList grades,String html){
        Map m = grades.toHashtable();
        Document doc = Jsoup.parse(html);
        Element root = doc.getElementById(ConnectionData.NotebookTableID);
        Elements listOfNotebooks = root.child(0).children();
        for(int i=1;i<listOfNotebooks.size();i++){
            String code = listOfNotebooks.get(i).child(2).text();
            Grade g = (Grade) m.get(code);
            g.setupNotebookLink(listOfNotebooks.get(i));
        }
    }*/
}
