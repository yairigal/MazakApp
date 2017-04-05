package project.android.com.mazak.Model.Entities;

import android.icu.util.RangeValueIterator;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yair on 2017-04-04.
 */

public class ClassEvent implements Serializable {
    public String startTime;
    public String endTime;
    public String name;
    public String lecturer;
    public String Class;
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

    public static ClassEvent ParseToClassEvent(Elements root){
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
}
