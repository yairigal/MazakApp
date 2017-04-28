package project.android.com.mazak.Database;

import project.android.com.mazak.Model.Entities.TestList;
import project.android.com.mazak.Model.Entities.getOptions;

/**
 * Created by Yair on 2017-04-28.
 */

public interface ITests {
    TestList getTests(getOptions option) throws Exception;
}
