package project.android.com.mazak.Model.Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yair on 2017-03-17.
 */

public class CourseStatistics implements Serializable{
    String courseName;
    ArrayList<Integer> freqs;
    int numOfStudentsWithGrade;
    float mean;
    float median;

    public float getPassedMean() {
        return passedMean;
    }

    public void setPassedMean(float passedMedian) {
        this.passedMean = passedMedian;
    }

    public float getNumOfPassedStudentsWithGrade() {
        return numOfPassedStudentsWithGrade;
    }

    public void setNumOfPassedStudentsWithGrade(float numOfPassedStudentsWithGrade) {
        this.numOfPassedStudentsWithGrade = numOfPassedStudentsWithGrade;
    }

    float passedMean;
    float numOfPassedStudentsWithGrade;

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getMedian() {
        return median;
    }

    public void setMedian(float median) {
        this.median = median;
    }

    public int getNumOfStudentsWithGrade() {
        return numOfStudentsWithGrade;
    }

    public void setNumOfStudentsWithGrade(int numOfStudentsWithGrade) {
        this.numOfStudentsWithGrade = numOfStudentsWithGrade;
    }

    public CourseStatistics() {
        this.courseName = "";
        this.freqs = new ArrayList<>();
        this.numOfStudentsWithGrade = 0;
        this.mean = 0;
        this.median = 0;
    }

    public ArrayList<Integer> getFreqs() {
        return freqs;

    }

    public void setFreqs(ArrayList<Integer> freqs) {
        this.freqs = freqs;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CourseStatistics(String courseName, ArrayList<Integer> freqs) {
        this.courseName = courseName;
        this.freqs = freqs;
    }
}
