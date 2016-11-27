package project.android.com.android5777_9254_6826.model.backend;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import project.android.com.android5777_9254_6826.model.datasource.IAccountDatabase;
import project.android.com.android5777_9254_6826.model.datasource.IAttractionDatabase;
import project.android.com.android5777_9254_6826.model.datasource.IBusinessDatabase;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

/**
 * Created by Yair on 2016-11-27.
 */

public class ListDatabase implements Backend {
    //TODO copy from AccountListDB and AttractionListDB and BusinessListDB to here
    /**
     * @param UserName
     * @param Password
     * @return true - if succeeded , else false;
     */
    @Override
    public boolean addNewAccount(String UserName, String Password) {
        return false;
    }

    @Override
    public ArrayList<Account> getAccountList() {
        return null;
    }

    @Override
    public Account getAccount(long id) throws Exception {
        return null;
    }

    @Override
    public Account getAccount(String username) throws Exception {
        return null;
    }

    @Override
    public boolean isRegistered(String userName) {
        return false;
    }

    /**
     * @param Type
     * @param Country
     * @param StartDate
     * @param EndDate
     * @param Price
     * @param Description
     * @param BusinessID
     * @return returns true if succeeded , else false;
     */
    @Override
    public boolean addNewAttraction(Properties.AttractionType Type, String Country, Date StartDate, Date EndDate, float Price, String Description, String BusinessID) {
        return false;
    }

    /**
     * @return list of the attraction in the database
     */
    @Override
    public ArrayList<Attraction> getAttractionList() {
        return null;
    }

    @Override
    public boolean ifNewAttractionAdded() {
        return false;
    }

    @Override
    public boolean addNewBusiness(String ID, String Name, Address address, String Email, URL Website) {
        return false;
    }

    @Override
    public ArrayList<Business> getBusinessList() {
        return null;
    }

    @Override
    public boolean ifNewBusinessAdded() {
        return false;
    }
}
