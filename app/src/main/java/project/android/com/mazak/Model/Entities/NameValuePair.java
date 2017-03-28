package project.android.com.mazak.Model.Entities;

/**
 * Created by Yair on 2017-02-13.
 */
public class NameValuePair {
    public String key;
    public String val;
    public NameValuePair(String key,String val)
    {
        this.key = key;
        this.val = val;
    }

    public String getName() {
        return key;
    }

    public String getValue() {
        return val;
    }
}
