package project.android.com.mazak.Model.Entities;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yair on 2017-04-04.
 */

public class ClassEvent implements Serializable,Comparable<ClassEvent> {
    public String startTime;
    public String endTime;
    public String name;
    public String lecturer;
    public String Class;


    public String getclass(){
        return Class;
    }
    public void setclass(String Class){
        this.Class = Class;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        if (startTime != null)
            this.startTime = startTime;
        else
            this.startTime = "12:00";
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        if (endTime != null)
            this.endTime = endTime;
        else
            this.endTime = "12:00";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String Type;
    public int day;

    public ClassEvent(String startTime, String endTime, String name, String lecturer, String aClass,String type,int day) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.lecturer = lecturer;
        Class = aClass;
        this.Type = type;
        this.day = day;
    }

    public ClassEvent(){

    }

    public static ClassEvent ParseToClassEvent1(Elements root){
        Elements title = root.get(0).getElementsByAttribute("title");
        Elements dayRoot = root.get(0).getElementsByAttribute("day");
        String dayAttr = dayRoot.attr("day");
        String att = title.attr("title");
        Pattern p = Pattern.compile("([\\d:]+)\\s*-\\s*([\\d:]+)\\s*([.\\d]+)\\s*([\\s\\w+\\p{InHebrew}]*)\\s-\\s([\\p{InHebrew}]*)\\s*\\(([\\d\\-'\\s\\p{InHebrew}]+)\\)");
        Matcher m = p.matcher(att.trim());
        if(m.find()){
            String start = m.group(1);
            String end = m.group(2);
            String Code = m.group(3);
            String name = m.group(4);
            String Type = m.group(5);
            String Class = m.group(6);
            int day;
            if(dayAttr == null || dayAttr.equals(""))
                day = 0;
            else
                day = Integer.parseInt(dayAttr);

            return new ClassEvent(start,end,name,"",Class,Type,day);
        }

        return new ClassEvent();
    }

    public static ClassEvent ParseToClassEvent(Element root){
        Elements childs = root.children();
        ClassEvent toRet = new ClassEvent();
        toRet.name = childs.get(2).child(0).text();
        toRet.Type = childs.get(3).text();
        toRet.lecturer = childs.get(4).text();
        String TimeAndPlace = childs.get(6).text();
        Pattern p = Pattern.compile("([\\p{InHebrew}\\s]+):\\s*([\\d:]+)\\s*-\\s*([\\d:]+),\\s*([\\d\\-'\\s\\p{InHebrew}]+)");
        Matcher m = p.matcher(TimeAndPlace.trim());
        if(m.find()) {
            toRet.setDay(getDay(m.group(1)));
            toRet.setStartTime(m.group(2));
            toRet.setStartTime(m.group(2));
            toRet.setEndTime(m.group(3));
            toRet.setclass(m.group(4));
        }else { // in case there is no class in the input
            p = Pattern.compile("([\\p{InHebrew}\\s]+):\\s*([\\d:]+)\\s*-\\s*([\\d:]+),\\s*");
            m = p.matcher(TimeAndPlace.trim());
            if(m.find()) {
                toRet.setDay(getDay(m.group(1)));
                toRet.setStartTime(m.group(2));
                toRet.setStartTime(m.group(2));
                toRet.setEndTime(m.group(3));
            }
        }
        return toRet;
    }

    private static int getDay(String group) {
        switch (group){
            case "יום א":
                return 0;
            case "יום ב":
                return 1;
            case "יום ג":
                return 2;
            case "יום ד":
                return 3;
            case "יום ה":
                return 4;
            default:
                return 5;
        }
    }

    @Override
    public int compareTo(@NonNull ClassEvent o) {
        String actual = startTime;
        String limit = o.startTime;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        String[] parts = actual.split(":");
        cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        parts = limit.split(":");
        cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

        if(cal1.before(cal2))
            return -1;
        else if(cal2.before(cal1))
            return 1;
        return 0;

    }
}
