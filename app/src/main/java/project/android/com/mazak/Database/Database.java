package project.android.com.mazak.Database;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yair on 2017-02-17.
 */

public interface Database extends IGrades,IIrurs,IStats,ISchedule,ITests {
    String username = null,password = null;
    void clearDatabase();
    void changeUsernameAndPassword(String username, String password) throws Exception;
    String getUpdateTime(String key);
}
