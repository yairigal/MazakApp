package project.android.com.mazak.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.Entities.TestList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.HtmlParser;
import project.android.com.mazak.Model.Web.ConnectionData;
import project.android.com.mazak.Model.Web.MazakConnection;
import project.android.com.mazak.R;

/**
 * Created by Yair on 2017-02-17.
 */

public class InternalDatabase implements Database {
    private static final String filename = "Database.dat";
    public static final String gradesKey = "grades";
    public static final String IrursKey = "irurs";
    public static final String ScheduleKey = "schedules";
    public static final String TestKey = "tests";
    private MazakConnection connection;
    private String currentUsername = "",currentPassword="";
    private GradesList grades;
    private Context activity;
    private IrurList irurs;
    private ScheduleList schedule;

    private TestList tests;


    public InternalDatabase(Context ctx) {
        activity = ctx;
    }

    private void loadGradesFromInternalMemory() {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String list = sharedPref.getString(gradesKey, null);
        //got nothing from database.
        if (list == null)
            grades = null;
        else {
            GradesList lst;
            lst = (new Gson()).fromJson(list, GradesList.class);
            grades = lst;
        }
    }

    private GradesList castToArray(GradesList arr) {
        GradesList lst = new GradesList();
        for (Grade map : arr) {
            Grade gr = new Grade();
/*            gr.code = map.get("code").toString();
            gr.finalGrade = map.get("finalGrade").toString();
            gr.minGrade = map.get("minGrade").toString();
            gr.name = map.get("name").toString();
            gr.points = map.get("points").toString();
            gr.semester = map.get("semester").toString();
            gr.subDetailsID = map.get("subDetailsID").toString();*/
            lst.add(gr);
        }
        return lst;
    }

    public void save(Object toSave,String key){
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(toSave);
        editor.putString(key, json);
        editor.commit();
    }

    public void clear(String key,Object listOfItems){
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
        listOfItems = null;
    }

    public void loadFromWeb(String key,String URL,Object list) throws Exception {
        String html;
        clear(key,list);
        refreshConnection();
        html = connection.connect(URL);
        list = HtmlParser.Parse(html,key);
        save(list,key);
    }

    public void loadFromMemory(String key,Object listOfObj,Type typeOfList){
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String list = sharedPref.getString(key, null);
        //got nothing from database.
        if (list == null)
            listOfObj = null;
        else {
            Object lst;
            lst = (new Gson()).fromJson(list,typeOfList);
            listOfObj = lst;
        }
    }

    public Object get(getOptions option,String key,Object listOfObjects,Type typeOfList,String URL) throws Exception {
        switch (option) {
            case fromMemory:
                loadFromMemory(key,listOfObjects,typeOfList);
            case fromWeb:
                loadFromWeb(key,URL,listOfObjects);
                break;
        }
        if (listOfObjects == null)
            throw new Exception(activity.getString(R.string.web_error));
        return listOfObjects;
    }

    @Override
    public void saveGrades(GradesList grades) {
        save(grades,gradesKey);
    }

    @Override
    public GradesList getGrades(getOptions option) throws Exception {
        switch (option) {
            case fromMemory:
                loadGradesFromInternalMemory();
                break;
            case fromWeb:
                getGradesFromWeb();
                break;
        }
        if (grades == null)
            throw new Exception(activity.getString(R.string.no_grades_error));
        return grades;
    }

    @Override
    public HashMap<Integer, GradesList> getGrades(getOptions options, int year) throws Exception {
        getGrades(options);
        return GradesModel.sortBySemesterAndYear(grades).get(year);
    }

    /**
     * Getting grades from the web, taking the username and password from the database.
     * @throws Exception
     */
    private void getGradesFromWeb() throws Exception {
        String html;
        deleteGrades();
        refreshConnection();
        html = connection.connect(ConnectionData.GradesURL);
        //String res = WebViewConnection.RhinoTest(html);
        grades = HtmlParser.ParseToGrades(html);
        saveGrades(grades);
    }

    private void refreshConnection() throws IOException {
        String username = LoginDatabase.getInstance(activity).getLoginDataFromMemory().get("username");
        String passw = LoginDatabase.getInstance(activity).getLoginDataFromMemory().get("password");
        if (connection == null || !username.equals(currentUsername) || !passw.equals(currentPassword)) {
            currentUsername = username;
            currentPassword = passw;
            connection = new MazakConnection(currentUsername, currentPassword);
        }
    }

    @Override
    public void clearDatabase() {
        deleteGrades();
        deleteIrurs();
        clearScheudle();
        clearTests();
    }

    @Override
    public GradesList FilterByName(String name) {
        GradesList toRet = new GradesList();
        for (Grade g : grades)
            if (g.name.contains(name))
                toRet.add(g);
        return toRet;
    }

    @Override
    public void deleteGrades() {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(gradesKey);
        editor.commit();
        grades = null;
    }

    @Override
    public void changeUsernameAndPassword(String username, String password) throws Exception {
        if (username == null || password == null)
            throw new Exception("Argument is null");
        connection = new MazakConnection(username, password);
    }

    @Override
    public IrurList getIrurs(getOptions options) throws Exception {
        switch (options) {
            case fromMemory:
                getIrursFromMemory();
                break;
            case fromWeb:
                getIrursFromWeb();
                break;
        }
        if (irurs == null)
            throw new Exception(activity.getString(R.string.no_appeals_found));
        return (IrurList) irurs.clone();
    }

    @Override
    public void deleteIrurs() {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(IrursKey);
        editor.commit();
        irurs = null;
    }

    @Override
    public void saveIrurs(IrurList irurs) {
        save(irurs,IrursKey);
    }

    @Override
    public boolean ifNewIrurArrived() throws Exception {
        IrurList currentList = null;
        try {
            //try get the list from the memory.
            currentList = getIrurs(getOptions.fromMemory);
        } catch (Exception ex){
            //if its the first time and there was no irurs saved in the memory
            //then for this time get them from the web and save them(in the getIrursFromWeb function)
            //and finish this call.
            getIrurs(getOptions.fromWeb);
            return false;
        }
        IrurList newList = getIrurs(getOptions.fromWeb);
        if(newList.equal(currentList))
            return false;
        saveIrurs(newList);
        return true;
    }

    private void getIrursFromWeb() throws Exception {
        String html;
        deleteIrurs();
        refreshConnection();
        html = connection.connect(ConnectionData.IrurURL);
        //String res = WebViewConnection.RhinoTest(html);
        irurs = HtmlParser.ParseToIrurs(html);
        saveIrurs(irurs);
    }

    private void getIrursFromMemory() {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String list = sharedPref.getString(IrursKey, null);
        //got nothing from database.
        if (list == null)
            irurs = null;
        else {
            IrurList lst;
            lst = (new Gson()).fromJson(list, IrurList.class);
            irurs = lst;
        }
    }

    @Override
    public CourseStatistics getStatsFromWeb(String link) throws Exception {
        refreshConnection();
        String res = connection.getStatisticsPage(link);
        return HtmlParser.ParseToStats(res);
    }

    @Override
    public ScheduleList getScheudle(getOptions options) throws Exception {
        switch (options) {
            case fromMemory:
                loadScheduleFromMemeory();
                break;
            case fromWeb:
                getScheudleFromWeb();
                break;
        }
        if (schedule == null)
            throw new Exception(activity.getString(R.string.no_schedule_error));
        return schedule;
    }

    private void getScheudleFromWeb() throws Exception {
        String html;
        clearScheudle();
        refreshConnection();
        html = connection.connect(ConnectionData.ScheduleURL);
        String newURL = HtmlParser.getListScheudleURL(html);
        html = connection.connect(newURL);
        schedule = HtmlParser.ParseToClassEvents(html);
        saveScheudle(schedule);
    }

    private void loadScheduleFromMemeory() {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String list = sharedPref.getString(ScheduleKey, null);
        //got nothing from database.
        if (list == null)
            schedule = null;
        else {
            ScheduleList lst;
            lst = (new Gson()).fromJson(list, ScheduleList.class);
            schedule = lst;
        }
    }

    @Override
    public void saveScheudle(ScheduleList schedule) {
        save(schedule,ScheduleKey);
    }

    @Override
    public void clearScheudle() {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(ScheduleKey);
        editor.commit();
        schedule = null;
    }

    @Override
    public TestList getTests(getOptions option) throws Exception {
        switch (option) {
            case fromMemory:
                loadFromMemory(TestKey,tests,TestList.class);
            case fromWeb:
                loadTestsFromWeb();
                break;
        }
        if (tests == null)
            throw new Exception(activity.getString(R.string.no_tests_error));
        return tests;
    }

    private void loadTestsFromWeb() throws Exception {
        loadFromWeb(TestKey,ConnectionData.TestsURL,tests);
    }

    private void saveTests(TestList tests) {
        save(tests,TestKey);
    }

    private void clearTests() {
        clear(TestKey,tests);
    }
}
