package project.android.com.mazak.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.Model.HtmlParser;
import project.android.com.mazak.Model.Web.ConnectionData;
import project.android.com.mazak.Model.Web.MazakConnection;
import project.android.com.mazak.Model.getOptions;
import project.android.com.mazak.R;

/**
 * Created by Yair on 2017-02-17.
 */

public class InternalDatabase implements Database {
    private static final String filename = "Database.dat";
    private static final String gradesKey = "grades";
    private static final String IrursKey = "irurs";
    private MazakConnection connection;
    private GradesList grades;
    private Context activity;
    private IrurList irurs;

    public InternalDatabase(String username, String passw, Context ctx) throws UnsupportedEncodingException {
        activity = ctx;
        connection = new MazakConnection(username, passw);
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

    @Override
    public void saveGrades(GradesList grades) {
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(grades);
        editor.putString(gradesKey, json);
        editor.commit();
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

    private void getGradesFromWeb() throws Exception {
        String html;
        deleteGrades();
        html = connection.connect(ConnectionData.GradesURL);
        //String res = WebViewConnection.RhinoTest(html);
        grades = HtmlParser.ParseToGrades(html);
        saveGrades(grades);
    }

    @Override
    public void clearDatabase() {
        deleteGrades();
        deleteIrurs();
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
        String newUN = username, newPass = password;
        if (username == null || password == null)
            throw new Exception("Argument is null");
        connection = new MazakConnection(newUN, newPass);
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
        return irurs.clone();
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
        SharedPreferences sharedPref = activity.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(irurs);
        editor.putString(IrursKey, json);
        editor.commit();
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
        String res = connection.getStatisticsPage(link);
        CourseStatistics statistics = HtmlParser.ParseToStats(res);
        return statistics;
    }
}
