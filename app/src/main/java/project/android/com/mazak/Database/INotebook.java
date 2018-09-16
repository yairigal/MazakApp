package project.android.com.mazak.Database;

import java.io.File;

import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.Notebook;
import project.android.com.mazak.Model.Entities.NotebookList;

/**
 * Created by Yair on 2017-08-03.
 */

public interface INotebook {
    NotebookList getNotebooks(Grade course) throws Exception;

    File downloadPDF(String url, Notebook currentPressed) throws Exception;
}
