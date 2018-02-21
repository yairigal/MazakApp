package project.android.com.mazak.Database;

import java.io.IOException;
import java.util.ArrayList;

import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.Entities.getOptions;

/**
 * Created by Yair on 2017-04-05.
 */

public interface ISchedule {
    ScheduleList getScheudle(getOptions options) throws Exception;
    void saveScheudle(ScheduleList list);
    void clearScheudle();
}
