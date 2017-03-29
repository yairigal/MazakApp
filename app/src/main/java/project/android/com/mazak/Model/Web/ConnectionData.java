package project.android.com.mazak.Model.Web;

import java.util.ArrayList;

import project.android.com.mazak.Model.Entities.NameValuePair;

/**
 * Created by Yair on 2017-02-14.
 */

public class ConnectionData {
    public static final String GradesURL = "https://mazak.jct.ac.il/Student/Grades.aspx";
    public static final String LoginUrl = "https://mazak.jct.ac.il/Login/Login.aspx";
    public static final String GradeURL2 = "https://mazak.jct.ac.il/Login/Login.aspx?ReturnUrl=%2fStudent%2fGrades.aspx";
    public static final String afterLoginGrades = "https://mazak.jct.ac.il/Student/Grades.aspx?AcademicYearID=&SemesterID=";
    public static final String USERNAMEID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_LoginControl_UserName";
    public static final String PASSWORDID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_LoginControl_Password";
    public static final String BUTTONID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_LoginControl_LoginButton";
    public static final String GradesTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_grdGrades_itemPlaceholderContainer";
    public static final String imageId = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_grdGrades_ctrl3_btnInformation";
    public static final String IngredientsTableId = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_dlgCoursePartGrades_tmpl_StudentCoursePartGradeUserControl_grdPartGrades_itemPlaceholderContainer";
    public static final String IrurURL = "https://mazak.jct.ac.il/Student/Appeals.aspx";
    public static final String IrurTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_ucOpenAppealsGrid_grdAppeals_itemPlaceholderContainer";
    public static final String IrurTableHeadID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_OpenSecondAppealsGridUserControl_grdSecondAppeals_ctrl0_Table1";
    public static final String IrurTableHeadID2 = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_OpenSecondAppealsGridUserControl_grdSecondAppeals_Table2";
    public static final String StatisticsTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_GradesTable";
    public static final String LastAppealsTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_ucPreviousAppealsGrid_grdAppeals_itemPlaceholderContainer";
    public static final String StatsNumOfStudntsID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_lSumOfStudents";
    public static final String StatsMeanID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_lTotalCourseAverage";
    public static final String StatsMedianID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_lPassedCourseMadian";

    public static ArrayList<NameValuePair> getMazakCredentials(final String username, final String pass){
        ArrayList<NameValuePair> mazakCredentials = new ArrayList<NameValuePair>()
        {
            {
                add(new NameValuePair("__EVENTTARGET", ""));
                add(new NameValuePair("__EVENTARGUMENT", ""));
                add(new NameValuePair("__VIEWSTATE", "/wEPDwUJODQ1NTExNDM3ZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAgU4Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblN0YXR1c0NvbnRyb2wkY3RsMDEFOGN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkTG9naW5TdGF0dXNDb250cm9sJGN0bDAz9StvzTms9RNhJpUJEIvLE/fgmvI="));
                add(new NameValuePair("__VIEWSTATEGENERATOR", "1806D926"));
                add(new NameValuePair("__SCROLLPOSITIONX", "0"));
                add(new NameValuePair("__SCROLLPOSITIONY", "0"));
                add(new NameValuePair("__EVENTVALIDATION", "/wEdAAftMc5YJCCVXYNVVn0in7zwyuUwJ3LGjJskskkgL1MJ/Kct0ueWAA0YL7V6O1AochnANEsgx9K9r/2lixUqPdVYOVB//zNqTaUBR/8dOG4iyLATo2l1y7ugf3AadYP1/o1HSZucqxgKJG9VFVk9FehcP636nVhtOI+3n6LJDgDKOCk9wZk="));
                add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$LoginControl$UserName", username));
                add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$LoginControl$Password", pass));
                add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$LoginControl$LoginButton", "כניסה"));
            }};
        return mazakCredentials;
    }
    public static ArrayList<NameValuePair> getStatsPostArguments(){
        ArrayList<NameValuePair> PostArguments = new ArrayList<NameValuePair>()
        {
            {
                add(new NameValuePair("__EVENTTARGET", ""));
                add(new NameValuePair("__EVENTARGUMENT", ""));
                add(new NameValuePair("__VIEWSTATE", "/wEPDwULLTEwNzYwNzUwMTVkGAIFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBThjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luU3RhdHVzQ29udHJvbCRjdGwwMQU4Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblN0YXR1c0NvbnRyb2wkY3RsMDMFKWN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkTG9naW5WaWV3Dw9kAgFkbKN8xmPIukNpwsOamqNDOAoWb2U="));
                add(new NameValuePair("__VIEWSTATEGENERATOR", "81B55E32"));
                add(new NameValuePair("__EVENTVALIDATION", "/wEdAAOLfCW1Trrz/9YmNs93KchGCW8vdzVzmzgmuoYuqTP+fq+VOWnHd5OHW92WOFnTCmQh+t43dMXHHAJdxTTU5onsAr54ug=="));
                add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$btnShowStudentCourseStatistics", "הצג סטטיסטיקה של קורס זה"));
            }};
        return PostArguments;
    }

}
