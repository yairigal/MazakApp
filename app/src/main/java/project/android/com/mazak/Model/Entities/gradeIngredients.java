package project.android.com.mazak.Model.Entities;

import java.io.Serializable;

/**
 * Created by Yair on 2017-02-19.
 */

public class gradeIngredients implements Serializable {
    public String type;
    public String minGrade;
    public String weight;
    public String moedA;
    public String moedB;
    public String moedSpecial;
    public String moedC;

    public gradeIngredients(String type, String minGrade, String weight, String moedA, String moedB, String moedSpecial, String moedC) {
        this.type = type;
        this.minGrade = minGrade;
        this.weight = weight;
        this.moedA = moedA;
        this.moedB = moedB;
        this.moedSpecial = moedSpecial;
        this.moedC = moedC;
    }
    public gradeIngredients(){}
}
