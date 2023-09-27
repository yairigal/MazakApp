/*
package project.android.com.httpstest.Database;

import android.content.Context;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import project.android.com.httpstest.Model.Entities.Delegate;
import project.android.com.httpstest.Model.Entities.Grade;
import project.android.com.httpstest.Model.Entities.gradeIngredients;
import project.android.com.httpstest.Model.HtmlParser;
import project.android.com.httpstest.Model.Web.ConnectionData;
import project.android.com.httpstest.Model.getOptions;

*/
/**
 * Created by Yair on 2017-02-19.
 *//*


public class WebViewDatabase implements Database {
    WebViewConnection connection;
    String username;
    String password;
   GradesList grades;
    Context ctx;

    public WebViewDatabase(String us,String pass,Context ctx){
        connection = new WebViewConnection(new WebView(ctx),us,pass);
        this.username = us;
        this.password = pass;
        this.ctx = ctx;
    }
    @Override
    public void saveGrades(ArrayList grades) {

    }

    void setGradesIngrid(ArrayList<Grade> grds){
        ArrayList<gradeIngredients> arr;
        for (Grade g:grds) {
            String html;
            html = connection.getWindow(g.subDetailsID);
            arr = HtmlParser.ParseToingerdiants(html);
            g.ingerdiantses = arr;
        }
    }

    @Override
    public ArrayList getGrades(getOptions fromWeb) throws Exception {
        final boolean[] wait = {true};
        if(fromWeb.equals(getOptions.fromWeb)){
            connection.connect(ConnectionData.GradesURL, new Delegate() {
                @Override
                public void function(Object obj) {
                    grades = (ArrayList<Grade>) obj;
                    setGradesIngrid(grades);
                    wait[0] = false;
                }
            });
            while(wait[0]);
        }
        return grades;
    }

    @Override
    public HashMap<Integer,GradesList> getGrades(getOptions options, int year) throws Exception {
        return null;
    }

    @Override
    public void clearDatabase() {

    }

    @Override
    publicGradesList FilterByName(String name) {
        return null;
    }

    @Override
    public void changeUsernameAndPassword(String username, String password) throws UnsupportedEncodingException {

    }

}
*/
