package project.android.com.mazak.Database;

/**
 * Created by Yair on 2017-02-17.
 */

public interface Database extends IGrades,IIrurs,IStats,ISchedule {
    String username = null,password = null;
    void clearDatabase();
    void changeUsernameAndPassword(String username, String password) throws Exception;
}
