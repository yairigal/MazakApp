package project.android.com.mazak.Model.Entities;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Yair on 2017-04-28.
 */

public class Test {
    public enum MoedB{
        NotSigned,
        Signed,
        CanSign
    }
    String code;
    String name;
    String moed;
    String date;
    String time;
    MoedB isMoedB;
    String MoedBLink;
    String LastRegtime;
    String LastCancelTime;

    public Test(String code, String name, String moed, String date, String time, MoedB isMoedB, String moedBLink, String lastRegtime, String lastCancelTime) {
        this.code = code;
        this.name = name;
        this.moed = moed;
        this.date = date;
        this.time = time;
        this.isMoedB = isMoedB;
        MoedBLink = moedBLink;
        LastRegtime = lastRegtime;
        LastCancelTime = lastCancelTime;
    }

    public Test(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoed() {
        return moed;
    }

    public void setMoed(String moed) {
        this.moed = moed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MoedB getIsMoedB() {
        return isMoedB;
    }

    public void setIsMoedB(MoedB isMoedB) {
        this.isMoedB = isMoedB;
    }

    public String getMoedBLink() {
        return MoedBLink;
    }

    public void setMoedBLink(String moedBLink) {
        MoedBLink = moedBLink;
    }

    public String getLastRegtime() {
        return LastRegtime;
    }

    public void setLastRegtime(String lastRegtime) {
        LastRegtime = lastRegtime;
    }

    public String getLastCancelTime() {
        return LastCancelTime;
    }

    public void setLastCancelTime(String lastCancelTime) {
        LastCancelTime = lastCancelTime;
    }

    public static Test ParseToTest(Element element) {
        Test t = new Test();
        Elements childs = element.children();
        t.setCode(childs.get(0).text());
        t.setName(childs.get(1).child(0).text());
        t.setMoed(childs.get(2).text());
        String[] DateAndTime = childs.get(3).text().trim().split(" ");
        t.setTime(DateAndTime[1]);
        t.setDate(DateAndTime[0]);
        if(!childs.get(4).hasAttr("href"))
            t.setIsMoedB(MoedB.NotSigned);
        else {
            String link = childs.get(4).child(0).attr("href");
            if (link != null) {
                t.setMoedBLink("https://levnet.jct.ac.il/Student/" + link);
                t.setIsMoedB(MoedB.CanSign);
            } else {
                t.setIsMoedB(MoedB.Signed);
            }
        }
        t.setLastRegtime(childs.get(5).text());
        t.setLastCancelTime(childs.get(6).text());
        return t;
    }
}
