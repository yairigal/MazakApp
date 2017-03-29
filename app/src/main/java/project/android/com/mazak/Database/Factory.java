package project.android.com.mazak.Database;

import android.content.Context;

/**
 * Created by Yair on 2017-02-17.
 */
public class Factory {
    private static Database ourInstance;

    public static Database getInstance() throws Exception {
        if(ourInstance == null)
            throw new Exception("No Instance found");
        return ourInstance;
    }

    public static Database getInstance(String username, String password, Context ctx) throws Exception {
        if(ourInstance == null)
            ourInstance = new InternalDatabase(username,password,ctx);
        else
            ourInstance.changeUsernameAndPassword(username,password);
        return ourInstance;
    }

    private Factory() {}
}
