package project.android.com.mazak.Model.Web;

import android.content.Context;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.Model.Adapters.NotebookAdapter;
import project.android.com.mazak.Model.Entities.ClassEvent;
import project.android.com.mazak.Model.Entities.CourseStatistics;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.Irur;
import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.Entities.LoginException;
import project.android.com.mazak.Model.Entities.Notebook;
import project.android.com.mazak.Model.Entities.NotebookList;
import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.Entities.Test;
import project.android.com.mazak.Model.Entities.TestList;
import project.android.com.mazak.Model.Entities.gradeIngredients;

/**
 * Created by Yair Yigal on 2018-02-20.
 */

public class MazakAPI {
    private static final int BUFFER_SIZE = 4096;
    private static String Mechina = "מכינה קדם אקדמית";

    /**
     * Reads username and password from LoginDatabase and tries to login to mazak.
     *
     * @return Cookies for next connection.
     * @throws Exception if login wasn't successful
     */
    public static String login(Context ctx) throws Exception {
        HashMap<String, String> data = LoginDatabase.getInstance(ctx).getLoginDataFromMemory();
        String loginData = getLoginData(data.get("username"), data.get("password"));
        Tuple<String, String> res = POST("https://levnet.jct.ac.il/api/home/login.ashx?action=TryLogin", loginData, "", "");
        String cookies = res.y;
        String JSONresposne = res.x;
        JSONObject response = new JSONObject(JSONresposne);
        if (response.getString("success").equals("false"))
            throw new LoginException("The username or password is incorrect");
        return cookies;
    }

    private static String getLoginData(String username, String password) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("username", username);
        dataMap.put("password", password);
        dataMap.put("defaultLanguage", null);
        JSONObject data = new JSONObject(dataMap);
        return data.toString();
    }

    /**
     * HTTP POST
     *
     * @param url
     * @param postParams
     * @return
     * @throws Exception
     */
    private static Tuple<String, String> POST(String url, String postParams) throws Exception {
        return POST(url, postParams, "", "");
    }

    private static Tuple<String, String> POST(String url, String postParams, String referer, String cookies) throws Exception {
        URL obj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

        // new connection trying to login and get a cookie
        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        //conn.setRequestProperty("Host", "accounts.google.com");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,**/*//*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        if (cookies != null && !cookies.equals(""))
            conn.setRequestProperty("Cookie", cookies);
        if (referer != null && !referer.equals(""))
            conn.setRequestProperty("Referer", referer);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        if (postParams.length() > 0) {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
        }

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);

        Charset set = Charset.forName("UTF-8");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), set));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            //response.append("\n");
        }
        in.close();

        return new Tuple<String, String>(response.toString(), getConnectionCookies(conn));

    }

    @NonNull
    private static String getConnectionCookies(HttpsURLConnection conn) {
        // temporary to build request cookie header
        StringBuilder sb = new StringBuilder();
        if (conn != null) {

            // find the cookies in the response header from the first request
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            if (cookies != null) {
                for (String cookie : cookies) {
                    if (sb.length() > 0) {
                        sb.append("; ");
                    }

                    // only want the first part of the cookie header that has the value
                    String value = cookie.split(";")[0];
                    sb.append(value);
                }
            }
        }

        // build request cookie header to send on all subsequent requests
        return sb.toString();
    }

    /**
     * HTTP GET
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String GET(String url) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
/*
        if (cookies != null) {
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
*/


        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


/*        // Get the response cookies
        Map<String, List<String>> coo1 = conn.getHeaderFields();
        List<String> coo = coo1.get("Set-Cookie");
        if (coo != null)
            setCookies(coo);*/

        return response.toString();

    }

    public static GradesList getGrades(Context ctx) throws Exception {
        String cookies = login(ctx);
        return tryGetGrades(cookies);
    }

    @NonNull
    private static GradesList tryGetGrades(String cookies) throws Exception {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("selectedAcademicYear", null);
        dataMap.put("selectedSemester", null);
        dataMap.put("pageSize", "999");
        JSONObject data = new JSONObject(dataMap);
        String sData = data.toString();
        Tuple<String, String> response = POST("https://levnet.jct.ac.il/api/student/grades.ashx?action=LoadGrades", sData, "", cookies);
        JSONObject r = new JSONObject(response.x);
        JSONArray items = r.getJSONArray("items");
        GradesList grades = new GradesList();
        for (int i = 0; i < items.length(); i++) {
            Grade grade = new Grade();
            grade.code = ((JSONObject) items.get(i)).get("actualCourseFullNumber").toString();
            grade.finalGrade = ((JSONObject) items.get(i)).get("finalGradeName").toString();
            grade.minGrade = ((JSONObject) items.get(i)).get("effectiveMinGrade").toString();
            grade.name = ((JSONObject) items.get(i)).get("courseName").toString();
            grade.points = ((JSONObject) items.get(i)).get("effectiveCredits").toString();
            grade.semester = ((JSONObject) items.get(i)).get("semesterName").toString();
            grade.actualCourseID = ((JSONObject) items.get(i)).get("actualCourseID").toString();
            grade.droppedOut = ((JSONObject) items.get(i)).getString("isDroppedOut");
            grades.add(grade);
        }
        return grades;
    }

    public static IrurList getAppeals(Context ctx) throws Exception {
        String cookies = login(ctx);
        return tryGetAppeals(cookies);
    }

    private static IrurList tryGetAppeals(String cookies) throws Exception {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("academicYearId", null);
        dataMap.put("semesterId", null);
        dataMap.put("pageSize", "9999999");
        JSONObject data = new JSONObject(dataMap);
        JSONObject r = new JSONObject(POST("https://levnet.jct.ac.il/api/Student/appeals.ashx?action=GetStudentClosedAppeals", data.toString(), "", cookies).x);
        JSONArray items = r.getJSONArray("items");
        IrurList irurs = new IrurList();
        for (int i = 0; i < items.length(); i++) {
            Irur irur = new Irur();
            irur.setCourseName(((JSONObject) items.get(i)).get("courseName").toString());
            irur.setCourseNum(((JSONObject) items.get(i)).get("courseFullNumber").toString());
            irur.setDate(((JSONObject) items.get(i)).get("answeredOn").toString());
            irur.setGradeDetail(((JSONObject) items.get(i)).get("gradePartTypeName").toString());
            irur.setInChargeName(((JSONObject) items.get(i)).get("ownerName").toString());
            irur.setIrurType(((JSONObject) items.get(i)).get("appealTypeName").toString());
            irur.setLecturer(((JSONObject) items.get(i)).get("lecturerName").toString());
            irur.setMoed(((JSONObject) items.get(i)).get("testTimeTypeName").toString());
            irur.setStatus(((JSONObject) items.get(i)).get("appealStateTypeName").toString());
            irurs.add(irur);
        }
        return irurs;
    }

    public static CourseStatistics getStatistics(String actualCourseID, Context ctx) throws Exception {
        String cookies = login(ctx);
        return tryGetStatistics(actualCourseID, cookies);
    }

    @NonNull
    private static CourseStatistics tryGetStatistics(String actualCourseID, String cookies) throws Exception {
        JSONObject res = new JSONObject(POST("https://levnet.jct.ac.il/api/student/GradesCharts.ashx?action=LoadData&ActualCourseID=" + actualCourseID, "", "", cookies).x);
        JSONObject r = (JSONObject) res.get("courseAverage");
        CourseStatistics stats = new CourseStatistics();
        stats.setCourseName(res.get("courseName").toString());
        stats.setMean(Float.parseFloat(r.getString("totalCourseAverage")));
        stats.setMedian(Float.parseFloat(r.getString("passedCourseMedian")));
        stats.setNumOfStudentsWithGrade((Integer) r.get("sumOfStudents"));
        stats.setNumOfPassedStudentsWithGrade((Integer) r.get("sumOfPassedStudents"));
        stats.setPassedMean(Float.parseFloat(r.getString("passedCourseAverage")));
        ArrayList<Integer> frqs = new ArrayList<>();
        JSONObject gds = (JSONObject) res.get("grades");
        frqs.add(gds.getInt("_0to59"));
        frqs.add(gds.getInt("_60to64"));
        frqs.add(gds.getInt("_65to69"));
        frqs.add(gds.getInt("_70to74"));
        frqs.add(gds.getInt("_75to79"));
        frqs.add(gds.getInt("_80to84"));
        frqs.add(gds.getInt("_85to89"));
        frqs.add(gds.getInt("_90to94"));
        frqs.add(gds.getInt("_95to100"));
        stats.setFreqs(frqs);
        return stats;
    }

    public static ScheduleList getSchedule(Context ctx) throws Exception {
        String cookies = login(ctx);
        return tryGetSchedule(cookies);
    }

    @NonNull
    private static ScheduleList tryGetSchedule(String cookies) throws Exception {
        JSONObject res = new JSONObject(POST("https://levnet.jct.ac.il/api/student/schedule.ashx?action=LoadFilters", "{}", "https://levnet.jct.ac.il/Student/WeeklySchedule.aspx", cookies).x);
        String year = res.getString("selectedAcademicYear");
        String semester = res.getString("selectedSemester");
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("selectedAcademicYear", year);
        dataMap.put("selectedSemester", semester);
        JSONObject data = new JSONObject(dataMap);
        res = new JSONObject(POST("https://levnet.jct.ac.il/api/student/schedule.ashx?action=LoadWeeklySchedule", data.toString(), "", cookies).x);
        JSONArray getLecturer = new JSONObject(POST("https://levnet.jct.ac.il/api/student/schedule.ashx?action=LoadScheduleList", data.toString(), "", cookies).x).getJSONArray("groupsWithMeetings");
        //courseGroupLecturers
        JSONArray meetings = res.getJSONArray("meetings");
        ScheduleList list = new ScheduleList();
        for (int i = 0; i < meetings.length(); ++i) {
            JSONObject current = (JSONObject) meetings.get(i);
            ClassEvent event = new ClassEvent();
            event.setclass(current.getJSONArray("fullView").get(3).toString());
            event.setDay(Integer.parseInt(current.getString("dayId")) - 1);
            event.setEndTime(current.getString("endTime").replaceAll(":00$", ""));
            event.setName(current.getJSONArray("fullView").get(0).toString());
            event.setStartTime(current.getString("startTime").replaceAll(":00$", ""));
            event.setType(current.getString("groupType"));
            // find corresponding lecturer by courseID
            String courseID = current.getJSONArray("fullView").get(1).toString();
            for (int k = 0; k < getLecturer.length(); ++k) {
                JSONObject currentLecturer = (JSONObject) getLecturer.get(k);
                String currentLecturerID = currentLecturer.get("groupFullNumber").toString();
                if (currentLecturerID.equals(courseID)){
                    event.setLecturer(currentLecturer.getString("courseGroupLecturers"));
                    break;
                }
            }
            list.add(event);
        }
        return list;
    }

    private static String getAbsoluteCourseID(String originalCourseID){
        String[] segs = originalCourseID.split("\\.");
        StringBuilder newCourseID = new StringBuilder();
        newCourseID.append(segs[0]);
        for (int i=1;i<segs.length-1;++i){
            newCourseID.append(".");
            newCourseID.append(segs[i]);
        }
        return newCourseID.toString();
    }

    public static TestList getTests(Context ctx) throws Exception {
        String cookies = login(ctx);
        return tryGetTests(cookies);
    }

    private static TestList tryGetTests(String cookies) throws Exception {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("selectedAcademicYear", null);
        dataMap.put("selectedSemester", null);
        dataMap.put("pageSize", "9999999");
        JSONObject data = new JSONObject(dataMap);
        JSONObject r = new JSONObject(POST("https://levnet.jct.ac.il/api/student/Tests.ashx?action=LoadTests", data.toString(), "", cookies).x);
        JSONArray items = r.getJSONArray("items");
        TestList tests = new TestList();
        for (int i = 0; i < items.length(); i++) {
            Test test = new Test();
            JSONObject current = items.getJSONObject(i);
            test.setCode(current.getString("courseFullNumber"));
            String date = current.getString("startDate").split("T")[0];
            test.setDate(date);
            test.setMoed(current.getString("testTimeTypeName"));
            test.setName(current.getString("courseName"));
            String time = current.getString("startDate").split("T")[1].split("\\+")[0];
            test.setTime(time);
            test.setLastRegtime("חסר");
            test.setLastCancelTime("חסר");
            tests.add(test);
        }
        return tests;
    }

    private static Tuple<ArrayList<gradeIngredients>, NotebookList> TryGetGradesDetails(Grade course, String cookies) throws Exception {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("actualCourseId", course.actualCourseID);
        JSONObject data = new JSONObject(dataMap);
        JSONObject r = new JSONObject(POST("https://levnet.jct.ac.il/api/student/coursePartGrades.ashx?action=GetStudentCoursePartGrades", data.toString(), "", cookies).x);
        ArrayList<gradeIngredients> parts = new ArrayList<>();
        JSONArray items = r.getJSONArray("partGrades");
        for (int i = 0; i < items.length(); ++i) {
            JSONObject current = items.getJSONObject(i);
            gradeIngredients currentPart = new gradeIngredients();
            currentPart.minGrade = current.getString("minGrade").equals("null") ? "" : current.getString("minGrade");
            currentPart.moedA = current.getString("gradeAName").equals("null") ? "" : current.getString("gradeAName");
            currentPart.moedB = current.getString("gradeBName").equals("null") ? "" : current.getString("gradeBName");
            currentPart.moedC = current.getString("gradeCName").equals("null") ? "" : current.getString("gradeCName");
            currentPart.moedSpecial = current.getString("gradeSpecialName").equals("null") ? "" : current.getString("gradeSpecialName");
            currentPart.type = current.getString("gradePartTypeName");
            currentPart.weight = current.getString("weight");
            parts.add(currentPart);
        }
        NotebookList notebooks = new NotebookList();
        JSONArray notebooksJSON = r.getJSONArray("testNotebooks");
        for (int i = 0; i < notebooksJSON.length(); ++i) {
            Notebook currentNote = new Notebook();
            JSONObject current = notebooksJSON.getJSONObject(i);
            currentNote.code = course.code;
            currentNote.id = current.getString("id");
            currentNote.link = "https://levnet.jct.ac.il/api/student/testNotebooks.ashx?action=DownloadNotebook&notebookId=" + currentNote.id;
            currentNote.moed = current.getString("testTimeTypeName");
            currentNote.time = current.getString("createdOn");
            notebooks.add(currentNote);
        }
        return new Tuple<>(parts, notebooks);
    }

    public static Tuple<ArrayList<gradeIngredients>, NotebookList> getGradesDetailsAndNotebooks(Grade course, Context ctx) throws Exception {
        String cookies = login(ctx);
        return TryGetGradesDetails(course, cookies);
    }

    public static File downloadPDF(String sUrl, Notebook currentNotebook, Context ctx) throws Exception {
        String cookies = login(ctx);
        String saveOutputName = NotebookAdapter.getFileName(currentNotebook);
        URL obj = new URL(sUrl);
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

        // Acts like a browser
        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Cookie", cookies);

        int responseCode = conn.getResponseCode();


        if ("gzip".equals(conn.getContentEncoding())) {
            GZIPInputStream reader = new GZIPInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(saveOutputName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            reader.close();
        } else {
            InputStream inputStream = conn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveOutputName);
            int SumBytes = 0;

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                SumBytes += bytesRead;
            }
            inputStream.close();
            outputStream.close();
        }


        return new File(saveOutputName);
    }

    public static boolean checkForPreperation(Context ctx) throws Exception {
        String cookies = login(ctx);
        return tryCheckForPrepegation(cookies);
    }

    private static boolean tryCheckForPrepegation(String cookies) throws Exception {
        Tuple<String, String> response = POST("https://levnet.jct.ac.il/api/student/Averages.ashx?action=LoadData", "", "", cookies);
        JSONObject r = new JSONObject(response.x);
        JSONArray items = r.getJSONArray("academicAverages");
        for(int i =0;i<items.length();++i){
            JSONObject current = items.getJSONObject(i);
            if(current.getString("parentDepartmentName").equals(Mechina))
                return true;
        }
        return false;
    }

    public static class Tuple<X, Y> {
        public final X x;
        public final Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }
}

