package project.android.com.android5777_9254_6826.model.datasource;

import java.net.URL;
import java.util.ArrayList;

import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Business;

/**
 * Created by Yair on 2016-11-21.
 */

public class BusinessListDB implements IBusinessDatabase {

    private static ArrayList<Business> businessList = new ArrayList<Business>();
    @Override
    public boolean addNewBusiness(String ID, String Name, Address address, String Email, URL Website) {
        businessList.add(new Business(ID,Name,address,Email,Website));
        return true;
    }

    @Override
    public ArrayList<Business> getBusinessList() {
        return businessList;
    }
}
