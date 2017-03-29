package project.android.com.mazak.Database;

import project.android.com.mazak.Model.Entities.CourseStatistics;

/**
 * Created by Yair on 2017-03-17.
 */

public interface IStats {
    CourseStatistics getStatsFromWeb(String link) throws Exception;
}
