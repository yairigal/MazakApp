package project.android.com.mazak.Database;

import android.content.Context;

/**
 * Created by Yair on 2017-02-17.
 */
public class Factory {
    private static Database ourInstance;

    public static Database getInstance() throws Exception {
        if(ourInstance == null) {
            throw new Exception("No instance found");
        }
        return ourInstance;
    }

    public static Database getInstance(Context ctx){
        if(ourInstance == null)
            ourInstance = new InternalDatabase(ctx);
        return ourInstance;
    }

    private Factory() {}
}
