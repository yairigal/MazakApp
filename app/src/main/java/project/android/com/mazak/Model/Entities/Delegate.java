package project.android.com.mazak.Model.Entities;

import java.io.Serializable;

/**
 * Created by Yair on 2017-02-14.
 */

public interface Delegate extends Serializable {
    void function(Object obj);
}
