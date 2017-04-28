package project.android.com.mazak.Model.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yair on 2017-04-28.
 */

public class TestList extends ObjectList<Test> {
    public TestList(List<Test> clone, boolean reversed) {
        super(clone,reversed);
    }

    public TestList() {
        super();
    }

    @Override
    public TestList clone() {
        return new TestList((List<Test>)((ArrayList<Test>)list).clone(),reversed);
    }
}
