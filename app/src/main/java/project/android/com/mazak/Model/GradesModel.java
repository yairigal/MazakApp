package project.android.com.mazak.Model;

import java.util.HashMap;

import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;

/**
 * Created by Yair on 2017-02-14.
 */

public class GradesModel {
    private GradesList grades;
    private static String LimodeyKodesh = "לימודי קודש";

    public GradesModel(GradesList grades) {
        this.grades = grades;
        this.grades.reverse();
    }

    /**
     * sorts the grades by years.
     * @param grades
     * @return hash map starting with 1 - first year 2 - second year...
     */
    public static HashMap<Integer, GradesList> sortByYears(GradesList grades) {
        if (grades.isReversed())
            grades.reverse();
        HashMap<Integer, GradesList> hm = new HashMap<>();
        int year = 1;
        int lastSem = 0;
        int curretSem = 0;
        GradesList list = new GradesList();
        for (int i = 0; i < grades.size(); i++) {
            Grade d = grades.get(i);
            curretSem = semsterToInt(d.semester);
            if (curretSem >= lastSem) {
                // we are in the same year
                list.add(d);
                lastSem = curretSem;
            } else { // we changed year.
                hm.put(year, list);
                list = new GradesList();
                year++;
                lastSem = curretSem;
                list.add(d);
            }
        }
        if (list.size() > 0)
            hm.put(year, list);
        return hm;
    }

    /**
     * sorts the grades by semester and year
     * @param grades
     * @return
     */
    public static HashMap<Integer, HashMap<Integer, GradesList>> sortBySemesterAndYear(GradesList grades) {
        HashMap<Integer, GradesList> det = sortByYears(grades);
        HashMap<Integer, HashMap<Integer, GradesList>> toret = new HashMap<>();
        GradesList sem0, sem1, sem2;
        for (int i = 0; i < det.size(); i++) {
            GradesList curr = det.get(i + 1);
            HashMap<Integer, GradesList> year = new HashMap<>();
            sem0 = new GradesList();
            sem1 = new GradesList();
            sem2 = new GradesList();
            for (int j = 0; j < curr.size(); j++) {
                Grade gr = curr.get(j);
                switch (semsterToInt(gr.semester)) {
                    case 0:
                        sem0.add(gr);
                        break;
                    case 1:
                        sem1.add(gr);
                        break;
                    default:
                        sem2.add(gr);
                        break;
                }
            }
            //if (sem0.size() != 0)
                year.put(0, sem0);
            //if (sem1.size() != 0)
                year.put(1, sem1);
            //if (sem2.size() != 0)
                year.put(2, sem2);
            toret.put(i + 1, year);
        }
        return toret;
    }

    /**
     * sorts the grades by semester
     * @param grades
     * @return
     */
    public static HashMap<Integer,GradesList> sortBySemester(GradesList grades){
        HashMap<Integer,GradesList> map = new HashMap<>();
        GradesList sem0 = new GradesList();
        GradesList sem1 = new GradesList();
        GradesList sem2 = new GradesList();
        for (Grade g:grades.getList()) {
            switch (semsterToInt(g.semester)){
                case 0:
                    sem0.add(g);
                    break;
                case 1:
                    sem1.add(g);
                    break;
                default:
                    sem2.add(g);
                    break;
            }
        }
        map.put(0,sem0);
        map.put(1,sem1);
        map.put(2,sem2);
        return map;
    }

    /**
     * returns the int value of the current semester
     * @param sem
     * @return
     */
    public static int semsterToInt(String sem) {
        if (sem.equals("אלול"))
            return 0;
        if (sem.equals("א"))
            return 1;
        if (sem.equals("ב"))
            return 2;
        if (sem.equals("שנתי"))
            return 2;
        return 0;
    }

    public static String intToSemster(int i){
        switch (i){
            case 0:
                return "אלול";
            case 1:
                return "א";
            default:
                return "ב";
        }
    }

    /**
     * calcultes average and num of Nz.
     * @param grades the grades to calculate the avg on.
     * @return Hash map with the key 'avg' for the avg float
     *         and the key 'nz' for the Nz points.
     *
     */
    public static HashMap<String,Float> getAvg(GradesList grades){
        float sumOfNz = 0;
        float sumOfGrades = 0;
        float kodesh = 0;
        float numOfKodesh = 0;
        float Nz , grade;
        float mingrade;
        float baseOfCalcNZ = 0;

        grades = removeDuplicatesOfGrades(grades);
        for (Grade g:grades.getList()) {
            try {
                //replaceing grades like 36נ to 36.
                g.finalGrade = g.finalGrade.replace("נ","");

                mingrade = Float.parseFloat(g.minGrade);
                Nz = Float.parseFloat(g.points);
                switch (g.finalGrade){
                    case "לא נבחן":
                        baseOfCalcNZ += Nz;
                        sumOfGrades += 0;
                        break;
                    case "לא השלים":
                        break;
                    case "טרם":
                        break;
                    case "נכשל":
                        baseOfCalcNZ += Nz;
                        sumOfGrades += 0;
                        break;
                    case "עבר":
                        sumOfNz += Nz;
                        break;
                    case "פטור":
                        break;
                    case "זיכוי":
                        sumOfNz += Nz;
                        break;
                }
                grade = Float.parseFloat(g.finalGrade);
                if(grade < mingrade){
                    sumOfGrades += grade * Nz;
                    baseOfCalcNZ += Nz;
                }else if (grade >= mingrade){
                    sumOfNz += Nz;
                    baseOfCalcNZ += Nz;
                    sumOfGrades += grade * Nz;
                }

                if(g.name.equals(LimodeyKodesh)){
                    kodesh += grade;
                    numOfKodesh++;
                }
            }catch (Exception ignored){

            }
        }
        HashMap<String,Float> map = new HashMap<>();
        map.put("kodesh",kodesh/numOfKodesh);
        map.put("avg",sumOfGrades/baseOfCalcNZ);
        map.put("nz",sumOfNz);
        return map;
    }

    /**
     * removes duplicates of grades from the list.
     * @param grades
     * @return
     */
    private static GradesList removeDuplicatesOfGrades(GradesList grades) {
        HashMap<String,Grade> map = new HashMap<>();
        GradesList toRet = new GradesList();
        for (Grade g :grades.getList()) {
            if (g.name.equals(LimodeyKodesh))
                toRet.add(g);
            else
                map.put(g.name, g);
        }
        for (Grade g:map.values())
            toRet.add(g);
        return toRet;
    }
}
