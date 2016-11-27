package project.android.com.android5777_9254_6826.model.backend;

import project.android.com.android5777_9254_6826.model.datasource.IDatabase;

/**
 * Created by Yair on 2016-11-27.
 */

public class FactoryDatabase {

    private static Backend instance = null;

    public static Backend getDatabase(){
        return getListDatabase();
    }
    private static Backend getListDatabase(){
        if (instance == null)
            instance = new ListDatabase();
        return instance;
    }

}
