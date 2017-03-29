package project.android.com.mazak.Model;

/**
 * Created by Yair on 2017-02-19.
 */

public interface ISearch extends IRefresh {
    void Filter(String query);
    void clearFilter();

}
