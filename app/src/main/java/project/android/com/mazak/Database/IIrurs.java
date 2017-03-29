package project.android.com.mazak.Database;

import project.android.com.mazak.Model.Entities.IrurList;
import project.android.com.mazak.Model.getOptions;

/**
 * Created by Yair on 2017-03-11.
 */

public interface IIrurs {
    IrurList getIrurs(getOptions options) throws Exception;
    void deleteIrurs();
    void saveIrurs(IrurList irurs);

    boolean ifNewIrurArrived() throws Exception;
}
