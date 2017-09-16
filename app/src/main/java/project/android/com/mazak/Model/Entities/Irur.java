package project.android.com.mazak.Model.Entities;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Yair on 2017-03-11.
 */

public class Irur {
    private String courseNum;
    private String courseName;
    private String gradeDetail;
    private String irurType;
    private String moed;
    private String inChargeName;
    private String lecturer;
    private String date;
    private String status;
    private String linkToFull;


    public String getLinkToFull() {
        return linkToFull;
    }

    public void setLinkToFull(String linkToFull) {
        this.linkToFull = linkToFull;
    }

    public Irur(String courseName, String courseNum, String status, String gradeDetail, String irurType, String moed, String inChargeName, String lecturer, String date, String link) {
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.status = status;
        this.gradeDetail = gradeDetail;
        this.irurType = irurType;
        this.moed = moed;
        this.inChargeName = inChargeName;
        this.lecturer = lecturer;
        this.date = date;
        this.linkToFull = link;
    }

    public Irur() {
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGradeDetail() {
        return gradeDetail;
    }

    public void setGradeDetail(String gradeDetail) {
        this.gradeDetail = gradeDetail;
    }

    public String getIrurType() {
        return irurType;
    }

    public void setIrurType(String irurType) {
        this.irurType = irurType;
    }

    public String getMoed() {
        return moed;
    }

    public void setMoed(String moed) {
        this.moed = moed;
    }

    public String getInChargeName() {
        return inChargeName;
    }

    public void setInChargeName(String inChargeName) {
        this.inChargeName = inChargeName;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Irur ParseToIrur(Element root) {
        Elements el = root.children();
            Irur irur = new Irur();
            try {
                irur.linkToFull = "https://mazak.jct.ac.il/Student/" + el.get(0).child(0).attr("href");
                irur.courseNum = el.get(1).text();
                irur.gradeDetail = el.get(3).text();
                irur.irurType = el.get(4).text();
                irur.moed = el.get(5).text();
                irur.inChargeName = el.get(6).text();
                irur.lecturer = el.get(7).text();
                irur.date = el.get(10).text();
                irur.status = el.get(9).text();
                irur.courseName = el.get(2).child(0).text();
            } catch (Exception e) {
                irur.status = el.get(1).text();
                irur.date = el.get(2).text();
            }
            return irur;
    }

    @Override
    public boolean equals(Object obj) {
        Irur sec = (Irur) obj;
        if (courseNum == null && sec.courseNum == null || courseNum.equals(sec.courseNum))
            if (courseName == null && sec.courseName == null || courseName.equals(sec.courseName))
                if (gradeDetail == null && sec.gradeDetail == null || gradeDetail.equals(sec.gradeDetail))
                    if (irurType == null && sec.irurType == null || irurType.equals(sec.irurType))
                        if (moed == null && sec.moed == null || moed.equals(sec.moed))
                            if (inChargeName == null && sec.inChargeName == null || inChargeName.equals(sec.inChargeName))
                                if (lecturer == null && sec.lecturer == null || lecturer.equals(sec.lecturer))
                                    if (date == null && sec.date == null || date.equals(sec.date))
                                        if (status == null && sec.status == null || status.equals(sec.status))
                                            if (linkToFull == null && sec.linkToFull == null || linkToFull.equals(sec.linkToFull))
                                                return true;
        return false;


    }
}
