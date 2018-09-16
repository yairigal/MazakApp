package project.android.com.mazak.Model.Web;

import java.util.ArrayList;

import project.android.com.mazak.Model.Entities.NameValuePair;

/**
 * Created by Yair on 2017-02-14.
 * This class holds all the web data strings.
 */

public class ConnectionData {
    public static final String GradesURL = "https://mazak.jct.ac.il/Student/Grades.aspx";
    public static final String GradesURL2 = "https://mazak.jct.ac.il/Student/Grades.aspx?AcademicYearID=&SemesterID=";
    public static final String LoginUrl = "https://mazak.jct.ac.il/api/home/login.ashx?action=TryLogin";
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
    public static final String StatsNumOfStudntsID = "lSumOfStudents";
    public static final String StatsMeanID = "lTotalCourseAverage";
    public static final String StatsMedianID = "lPassedCourseMadian";
    public static final String ScheduleTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_ScheduleView";
    public static final String ScheduleURL = "https://mazak.jct.ac.il/Student/ScheduleList.aspx";
    public static final String ScheduleYearCBID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_cmbAcademicYears";
    public static final String ScheduleSemesterCBID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_cmbSemesters";
    public static final String ScheduleListTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_grdCoursesList_itemPlaceholderContainer";
    public static final String TfilaTimesURL = "https://midrash.jct.ac.il/%D7%96%D7%9E%D7%A0%D7%99-%D7%94%D7%99%D7%95%D7%9D";
    public static final String TestsURL = "https://mazak.jct.ac.il/Student/Tests.aspx";
    public static final String TestsTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_grdStudentTests_itemPlaceholderContainer";
    public static final String GradeDetailsTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_dlgCoursePartGrades_tmpl_StudentCoursePartGradeUserControl_grdPartGrades_itemPlaceholderContainer";
    public static final String NotebookURL = "https://mazak.jct.ac.il/Student/TestNotebooks.aspx";
    public static String NotebookTableID = "ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_grdStudentNotebooks_itemPlaceholderContainer";

    /**
     * returns the POST arguments for the mazak.jct.ac.il login
     *
     * @param username
     * @param pass
     * @return
     */
    public static ArrayList<NameValuePair> getMazakCredentials(final String username, final String pass) {
        return new ArrayList<NameValuePair>() {
            {
                add(new NameValuePair("__EVENTTARGET", "ctl00$content$btnLogin"));
                add(new NameValuePair("__EVENTARGUMENT", ""));
                add(new NameValuePair("__VIEWSTATE", "/wEPDwUKMTg3MzMwNzY5OGRkHCaF7BZkLQl9GefpQwdxxmezt/A="));
                add(new NameValuePair("__VIEWSTATEGENERATOR", "1806D926"));
                add(new NameValuePair("__EVENTVALIDATION", "/wEdAATgx8kKgJBELIOcXbH3Q6m+j5/t2OOZ/RHdF/YKwp5dYwJDrok5fYudB9Cp78gLDON8M/fB2YTrHoJF2xiTjDS4j9B92IbktVzaSHGo3WwFwg3YiCU="));
                add(new NameValuePair("ctl00$content$Username", username));
                add(new NameValuePair("ctl00$content$Password", pass));
            }
        };
    }

    public static String getMazakCredentialsNew(final String username, final String pass) {
        return
                "{" +
                        "\"password\":" +
                        "\"" + pass + "\"" +
                        "," +
                        "\"username\":" +
                        "\"" + username + "\"" +
                        "," +
                        "\"action\":" +
                        "\"" + "Login" + "\"" +
                        "}";
    }

    /**
     * returns the POST arguments for the mazak statistics.
     *
     * @return
     */
    public static ArrayList<NameValuePair> getStatsPostArguments() {
        return new ArrayList<NameValuePair>() {
            {
                add(new NameValuePair("__EVENTTARGET", ""));
                add(new NameValuePair("__EVENTARGUMENT", ""));
                add(new NameValuePair("__VIEWSTATE", "/wEPDwULLTEwNzYwNzUwMTVkGAIFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBThjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luU3RhdHVzQ29udHJvbCRjdGwwMQU4Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblN0YXR1c0NvbnRyb2wkY3RsMDMFKWN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkTG9naW5WaWV3Dw9kAgFkbKN8xmPIukNpwsOamqNDOAoWb2U="));
                add(new NameValuePair("__VIEWSTATEGENERATOR", "81B55E32"));
                add(new NameValuePair("__EVENTVALIDATION", "/wEdAAOLfCW1Trrz/9YmNs93KchGCW8vdzVzmzgmuoYuqTP+fq+VOWnHd5OHW92WOFnTCmQh+t43dMXHHAJdxTTU5onsAr54ug=="));
                add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$btnShowStudentCourseStatistics", "הצג סטטיסטיקה של קורס זה"));
            }
        };
    }

    public static ArrayList<NameValuePair> getGradeDetailsPostArguments(String btn) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new NameValuePair("__EVENTTARGET", ""));
        params.add(new NameValuePair("__EVENTARGUMENT", ""));
        params.add(new NameValuePair("__VIEWSTATE", "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HwVkHwYFBTIyMzMzFgQfBWQfBgUFMjkyMTIWAgIDAgNkZAUqQ29tcHV0ZXJpemVkUXVlc3Rpb25uYWlyZUFuc3dlcnNEYXRhU291cmNlDw9kDxAWAmYCARYCFgQfBWQfBgUFMjIzMzMWBB8FZB8GBQUyOTIxMhYCAgMCA2RkBRFBcHBlYWxzRGF0YVNvdXJjZQ8PZA8QFgJmAgEWAhYEHwVkHwYFBTIyMzMzFgQfBWQfBgUFMjkyMTIWAgIDAgNkZAIBDxQrAAQUKwACZGQWAh4BYQUITWluaW1pemUWBB8HBQhNYXhpbWl6ZR4BdmcWAh8HBQVDbG9zZWQYCAU9Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcw8UKwAOZGRkZGRkZDwrADkAAjlkZGRmAv////8PZAV/Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGRsZ0NvdXJzZVBhcnRHcmFkZXMkdG1wbCRTdHVkZW50Q291cnNlUGFydEdyYWRlVXNlckNvbnRyb2wkZ3JkVGVzdE5vdGVib29rcw88KwAOAwhmDGYNAv////8PZAV8Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGRsZ0NvdXJzZVBhcnRHcmFkZXMkdG1wbCRTdHVkZW50Q291cnNlUGFydEdyYWRlVXNlckNvbnRyb2wkZ3JkUGFydEdyYWRlcw8UKwAOZGRkZGRkZBQrAAJkZAICZGRkZgL/////D2QFkgFjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZGxnQ291cnNlUGFydEdyYWRlcyR0bXBsJFN0dWRlbnRDb3Vyc2VQYXJ0R3JhZGVVc2VyQ29udHJvbCRncmRDb21wdXRlcml6ZWRRdWVzdGlvbm5haXJlQW5zd2Vycw88KwAOAwhmDGYNAv////8PZAUpY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblZpZXcPD2QCAWQFeWN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRkbGdDb3Vyc2VQYXJ0R3JhZGVzJHRtcGwkU3R1ZGVudENvdXJzZVBhcnRHcmFkZVVzZXJDb250cm9sJGdyZEFwcGVhbHMPFCsADmRkZGRkZGQ8KwAIAAIIZGRkZgL/////D2QFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxY/BThjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luU3RhdHVzQ29udHJvbCRjdGwwMQU4Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblN0YXR1c0NvbnRyb2wkY3RsMDMFSWN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSR1Y0NvbW1hbmRTaG93JGJ0blNob3cFVmN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRDb3Vyc2VDb2xvclJlYWRlclVzZXJDb250cm9sJGN0bDAwBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwwJGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwxJGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwyJGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwzJGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw0JGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw1JGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw2JGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw3JGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw4JGJ0bkluZm9ybWF0aW9uBVJjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw5JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwxMCRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMTEkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDEyJGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwxMyRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMTQkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDE1JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwxNiRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMTckYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDE4JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwxOSRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMjAkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDIxJGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwyMiRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMjMkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDI0JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwyNSRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMjYkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDI3JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwyOCRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMjkkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDMwJGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwzMSRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMzIkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDMzJGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwzNCRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMzUkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDM2JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmwzNyRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsMzgkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDM5JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw0MCRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsNDEkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDQyJGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw0MyRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsNDQkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDQ1JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw0NiRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsNDckYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDQ4JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw0OSRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsNTAkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDUxJGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw1MiRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsNTMkYnRuSW5mb3JtYXRpb24FU2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRHcmFkZXMkY3RybDU0JGJ0bkluZm9ybWF0aW9uBVNjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJENvbnRlbnRQbGFjZUhvbGRlcjEkZ3JkR3JhZGVzJGN0cmw1NSRidG5JbmZvcm1hdGlvbgVTY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGdyZEdyYWRlcyRjdHJsNTYkYnRuSW5mb3JtYXRpb24FV2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRDb3Vyc2VDb2xvclJlYWRlclVzZXJDb250cm9sMiRjdGwwMAVHY3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRDb250ZW50UGxhY2VIb2xkZXIxJGRsZ0NvdXJzZVBhcnRHcmFkZXMFeGN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRkbGdDb3Vyc2VQYXJ0R3JhZGVzJHRtcGwkU3R1ZGVudENvdXJzZVBhcnRHcmFkZVVzZXJDb250cm9sJGZybUNvdXJzZQ8UKwAHZGRkZGQWAAIBZGU+nqC06rrBkhqvIqHAHay3gvbI"));
        params.add(new NameValuePair("__VIEWSTATEGENERATOR", "2D83CBBC"));
        params.add(new NameValuePair("__SCROLLPOSITIONX", "0"));
        params.add(new NameValuePair("__SCROLLPOSITIONY", "2051"));
        params.add(new NameValuePair("__EVENTVALIDATION", "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"));
        params.add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$cmbAcademicYears", ""));
        params.add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$cmbSemsters", ""));
        btn = btn.replace("_", "$");
        params.add(new NameValuePair(btn + ".x", "10"));
        params.add(new NameValuePair(btn + ".y", "11"));
        params.add(new NameValuePair("ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_dlgCoursePartGrades_clientState", "|0|3,0,191px,213px,500px,500px,0||[[[[null,0,\"42px\",\"557px\",\"500px\",\"500px\",1,null,1,1,null,3]],[[[[[null,null,null]],[[[[[]],[],[]],[{},[]],null],[[[[]],[],[]],[{},[]],null],[[[[]],[],[]],[{},[]],null]],[]],[{},[]],null],[[[[null,null,null,null]],[],[]],[{},[]],null]],[]],[{\"2\":[2,\"191px\"],\"3\":[3,\"213px\"],\"4\":[1,3],\"5\":[11,0]},[]],\"3,0,191px,213px,500px,500px,0\"]"));
        params.add(new NameValuePair("ctl00$ctl00$_IG_CSS_LINKS_", "~/App_Themes/DefaultTheme/main.css?t=636361796912346512|~/App_Themes/DefaultTheme/StyleSheet.css?t=636361796912346512|../ig_res/Default/ig_dialogwindow.css|../ig_res/Default/ig_shared.css"));
        return params;
    }

    public static ArrayList<NameValuePair> getNoteBookPostArguments(String notebook) {
        String notebook2 = notebook.replace("%", "$").replace("_", "$");
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new NameValuePair("__EVENTTARGET", notebook));
        params.add(new NameValuePair("__EVENTARGUMENT", ""));
        params.add(new NameValuePair("__LASTFOCUS", ""));
        params.add(new NameValuePair("__VIEWSTATE", "/wEPDwUKMTgzMTcxMjgzN2QYAQUhY3RsMDAkY29udGVudCRncmRTdHVkZW50Tm90ZWJvb2tzDxQrAA5kZGRkZGRkPCsAMwACM2RkZGYC/////w9kw536WaoS0bk56S4n8tUWB8zeg58="));
        params.add(new NameValuePair("__VIEWSTATEGENERATOR", "860DB63F"));
        params.add(new NameValuePair("__EVENTVALIDATION", "/wEdAEQUITHckR5BvH1aiVyG9tlbbLI1CDD4ct7b9x2VWjrv8dXNO6LsxDTLxLhKtZURpVCtt3EiV4cQ2QXRq583ZTa4r7jYvJe3MkjwSRGl09Ex3Mu+KyJyTgYNECw7zOQOLoV51MMKMPh1WzsXabYEBCZBYrkWHI7IvaQdyhpH6espbL5avCd9fJ1xxUmmSPrLku0KqYoCNz9pE0bNpF9EkpEvAKOmddXHn70K0LIsJ4OoXcrgUdxB6Y2NwUSq5YuHRjuJH4ORoNdXlxOLzP9+WVAHDiBriw484snAvB8YmODmTQeMa26zR5aqdOqaryae2gx9Z8X9VKEpqQV1gjOF1pNDMDyNboi394kYGo6IGd6K7ioKjZnzE6w51p3ZO+4iqOW/pwL+RM98EXbL40CDsI0R2RBjfDhtSmFAc2ufrDIIWxrJC/ajSui7n3ike9qgOFiEKKHlkMEwJ/Scfgp7CeTbeWqhljlAATOrD6xU0hmzhb8bSHUvdCll086gY1y4xHcDgjhNDSrhY3D7Sha1KLoW2Rs1GyRgg6S74P1zNbtLGJ0LbtXJ2pT2wsUGichTYOgHEqNJImzmMGL/br3xoQlMkme9RT5Q+K+P8WU81qnK9NuyeKheLuynbIIsobZVQzaHEKRo0jE98uz2ZtmVGQg5h0rUy7BxVXTDjyPjkg4xB5nqTT8ViSW8tb+16BWCI/9BZ65PDbpVkozCwME3gsGFaQLlH1GrGtznerRvE9oT2Au+4XD8rAjj2nWLd9gfT3Lg07QCzj/bGUvBc9OVVJkjSG1RRr7aSjxtBXnkzTZm5QNXmaWO3/ikS4Eg1vLuTsJJeXDiH9dLJZY3o+2lxOdYkzFO/VTImBrSvQw915yV0PWZyrY/l2xwBf/+34kRRiGPJnN0s075B+kNCMgSvSf9zf5CJHZDVFmCWANsYG4yRsD1tYg//vn84JvmHCMd0myPrt72xasHHB0jSxse+Zhtko8hRwxyTYxy8qz1AP8VNQbmYvq8m35KeqTzEunBuDYhMSB3MFfVa4+0JfCiu0cQVdbxyGVFiCZF5Mxtzf/5CB/Gx7kdmsf6ztLiCo7VlQTFh9iGeJSF8MxCHgak1w+lstz2fXF+kqMk9pc6jBzQULv4HtRHiNJ4XbOY55gJZcIIWvmyyfu3c8osxsxT8WxEG4cLeERUeCyxWvK+wdwiV2WcRjet5cJrZB1VYc6Ihu0EgSuwrut3RNfjHZEqPe+jWL8ON2EyWYQ1Hg+QbCreEpdmvR8A0Tj4n5Ji/Qs4+VkTFe83u/hQ11qBD0bbjeD4B89WA6fmcjwyWN5biaVxn+xG2jy1ywSv5Yd8XNu5m5qFCGQOCzptMGnXn4lBTUzfoKhQsiB47mUh1Fr6zGwoTe07LunjfkNLQ5zOXJRjq0DmqeqqLxV1DSil99qUI/+BgsoCBAwrEKJ4QKAJl+f1EFXu6kjRWw30NL2+SasE458U6kcu"));
        params.add(new NameValuePair("ctl00$content$hPaidRevacha", "true"));
        params.add(new NameValuePair("ctl00$content$cmbAcademicYears", ""));
        params.add(new NameValuePair("ctl00$content$cmbSemesters", ""));
        params.add(new NameValuePair("ctl00$content$cmbTestTimeTypes", ""));
        return params;
    }

    public static String JsonValues(ArrayList<NameValuePair> values) {
        String ans = "{";
        for (NameValuePair v : values) {
            ans += "\"" + v.key + "\":";
            try {
                Integer.parseInt(v.val);
            } catch (Exception ex) {
                ans += "\"" + v.val + "\"";
                if (values.get(values.size() - 1) != v)
                    ans += ",";
                continue;
            }
            ans += v.val;

            if (values.get(values.size() - 1) != v)
                ans += ",";
        }
        ans += "}";
        return ans;
    }

}
