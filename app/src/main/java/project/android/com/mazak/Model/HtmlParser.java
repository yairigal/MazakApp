package project.android.com.mazak.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
import project.android.com.mazak.Model.Entities.gradeIngredients;
import project.android.com.mazak.Model.Web.ConnectionData;

import static project.android.com.mazak.Model.Entities.Grade.ParseToGrade;

/**
 * Created by Yair on 2017-02-14.
 */

public class HtmlParser {
    /**
     * Parsing the html page and returning the grades from it
     *
     * @param html
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static GradesList ParseToGrades(String html) throws ExecutionException, InterruptedException, JSONException {
       /* AsyncTask<Void,Void,ArrayList<Grade>> as  = getAsyncTask(html);
        as.execute();
        return as.get();*/
        GradesList grades = new GradesList();
/*        Document doc = null;
        doc = Jsoup.parse(html);
        Element el1 = doc.getElementById("dvGrades");
        Element el2 = el1.child(2);
        Element el3 = el2.child(0);
        Element el4 = el3.child(1);
        Elements el = el4.children();*/
        final JSONObject obj = new JSONObject(html);
        JSONArray items = obj.getJSONArray("items");
        for (int i = 0; i < items.length(); i++)
            grades.add(ParseToGrade(items.get(i)));
        return grades;
    }

    /**
     * arsing the html page and returning the irurs from it
     *
     * @param html
     * @return
     */
    public static IrurList ParseToIrurs(String html) {
        IrurList irurs = new IrurList();
        Document doc = null;
        doc = Jsoup.parse(html);
        //right now appeals
        Elements tables = doc.select("table.table");
        for (Element tab : tables)
            addIrurs(tab, irurs);
        return irurs;
    }

    /**
     * adds the irurs from the document to the list.
     *
     * @param root
     * @param lst
     */
    private static void addIrurs(Element root, IrurList lst) {
        if (root != null) {
            Elements el = root.children().get(1).children();
            for (int i = 0; i < el.size(); i++)
                //checking its not a not sent irur
                if (el.get(i).children().size() > 10)
                    lst.add(Irur.ParseToIrur(el.get(i)));
        }
    }

    /**
     * @param html
     * @return
     */
    public static ArrayList<gradeIngredients> ParseToingerdiants(String html) {
        Document doc = null;
        doc = Jsoup.parse(html);
        Element elem = doc.getElementById(ConnectionData.IngredientsTableId);
        Elements el = elem.children().get(0).children();
        return null;
    }

    /**
     * arsing the html page and returning the statistics from it
     *
     * @param html
     * @return
     */
    public static CourseStatistics ParseToStats(String html) {
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
        for (int i = 0; i < 9; i++) {
            String freq = ((Element) el.get(i).childNode(3).childNode(1)).text();
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
        for (int i = 1; i < el.size(); i++)
            sched.add(ClassEvent.ParseToClassEvent1(el.get(i).getElementsByAttribute("title")));
        return sched;
    }

    /**
     * arsing the html page and returning the classes  from it
     *
     * @param html
     * @return
     */
    public static ScheduleList ParseToClassEvents(String html) throws JSONException {
        ScheduleList sched = new ScheduleList();
/*        Document doc = Jsoup.parse(html);
        Element elem = doc.select("table.table").get(0);
        List<Node> el = elem.childNode(3).childNodes();*/
        final JSONObject obj = new JSONObject(html);
        JSONArray items = obj.getJSONArray("groupsWithMeetings");
        for (int i = 0; i < items.length(); i++)
            //sched.add(ClassEvent.ParseToClassEvent(el.get(i).getElementsByAttribute("title")));
            sched.add(ClassEvent.ParseToClassEvent(items.get(i)));
        return sched;
    }

    /**
     * parses the html page to find the url to the schedule page that is in words and not pictures
     *
     * @param html
     * @return
     */
    public static String getListScheudleURL(String html) {
        String year = null, sem = null;
        Document doc = Jsoup.parse(html);
        Element root = doc.getElementById(ConnectionData.ScheduleYearCBID);
        Elements childs = root.children();
        for (Element e : childs) {
            String Selected = "";
            Selected = e.getElementsByAttribute("selected").val();
            if (!Selected.equals("")) {
                year = Selected;
                break;
            }
        }
        root = doc.getElementById(ConnectionData.ScheduleSemesterCBID);
        childs = root.children();
        for (Element e : childs) {
            String Selected = "";
            Selected = e.getElementsByAttribute("selected").val();
            if (!Selected.equals("")) {
                sem = Selected;
                break;
            }
        }
        return String.format("https://levnet.jct.ac.il/Student/ScheduleList.aspx?AcademicYearID=%s&SemesterID=%s", year, sem);
    }

    /**
     * arsing the html page and returning the Tfila times from it
     *
     * @param html
     * @return
     */
    public static ArrayList<TfilaTime> ParseToTfilaTime(String html) throws ParseException {
        ArrayList<TfilaTime> times = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element elem = doc.getElementsByTag("dl").get(0);
        Elements el = elem.children();
        boolean closeTimeSelected = false;
        for (int i = 0; i < el.size(); i += 2) {
            boolean isSelected = false;
            String TfilaTitle = el.get(i).text();
            String TfilaTime = el.get(i + 1).text();
            if (!closeTimeSelected) {
                Calendar calendar = Calendar.getInstance();
                int tHr = Integer.parseInt(TfilaTime.split(":")[0]);
                int tMin = Integer.parseInt(TfilaTime.split(":")[1]);
                int nHr = calendar.get( Calendar.HOUR_OF_DAY );
                int nMin = calendar.get( Calendar.MINUTE );
                if (tHr > nHr || (tHr == nHr && tMin > nMin)) {
                    isSelected = true;
                    closeTimeSelected = true;
                }
            }
            times.add(new TfilaTime(TfilaTitle, TfilaTime, isSelected));
        }
        return times;
    }

    /**
     * arsing the html page and returning the tests from it
     *
     * @param html
     * @return
     */
    public static TestList ParseToTests(String html) {
        TestList times = new TestList();
        Document doc = Jsoup.parse(html);
        Element elem = doc.select("table.table").get(0);
        Elements el = elem.children().get(1).children();
        for (int i = 1; i < el.size(); i++) {
            times.add(Test.ParseToTest(el.get(i)));
        }
        return times;
    }

    /**
     * a general function for all the parses by their keys.
     *
     * @param html
     * @param key
     * @return
     * @throws Exception
     */
    public static Object Parse(String html, String key) throws Exception {
        switch (key) {
            case InternalDatabase.gradesKey:
                return ParseToGrades(html);
            case InternalDatabase.IrursKey:
                return ParseToIrurs(html);
            case InternalDatabase.ScheduleKey:
                return ParseToClassEvents(html);
            case InternalDatabase.TestKey:
                return ParseToTests(html);
            case InternalDatabase.NotebookKey:
                return null;
            default:
                return null;
        }
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
