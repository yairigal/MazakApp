package project.android.com.mazak.Model.Entities;

/**
 * Created by Yair on 2017-04-24.
 */

public class TfilaTime {
    public String name;
    public String time;
    public boolean isSelected;

    public TfilaTime(String name, String time, boolean isSelected) {
        this.name = name;
        this.time = time;
        this.isSelected = isSelected;
    }
}