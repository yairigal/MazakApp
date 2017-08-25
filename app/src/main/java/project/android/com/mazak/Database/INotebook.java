package project.android.com.mazak.Database;

import project.android.com.mazak.Model.Entities.NotebookList;
import project.android.com.mazak.Model.Entities.getOptions;

/**
 * Created by Yair on 2017-08-03.
 */

public interface INotebook {
    NotebookList getNotebooks(getOptions op) throws Exception;
}
