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
//     * @param UserName
//     * @param Password
//     * @return true - if succeeded , else false;
//     */
//    @Override
//    public boolean addNewAccount(String UserName, String Password) {
//        return false;
//    }
//
//    @Override
//    public ArrayList<Account> getAccountList() {
//        return null;
//    }
//
//    @Override
//    public Account getAccount(long id) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Account getAccount(String username) throws Exception {
//        return null;
//    }
//
//    @Override
//    public boolean isRegistered(String userName) {
//        return false;
//    }
//
//    /**
//     * @param Type
//     * @param Country
//     * @param StartDate
//     * @param EndDate
//     * @param Price
//     * @param Description
//     * @param BusinessID
//     * @return returns true if succeeded , else false;
//     */
//    @Override
//    public boolean addNewAttraction(Properties.AttractionType Type, String Country, Date StartDate, Date EndDate, float Price, String Description, String BusinessID) {
//        return false;
//    }
//
//    /**
//     * @return list of the attraction in the database
//     */
//    @Override
//    public ArrayList<Attraction> getAttractionList() {
//        return null;
//    }
//
//    @Override
//    public boolean ifNewAttractionAdded() {
//        return false;
//    }
//
//    @Override
//    public boolean addNewBusiness(String ID, String Name, Address address, String Email, URL Website) {
//        return false;
//    }
//
//    @Override
//    public ArrayList<Business> getBusinessList() {
//        return null;
//    }
//
//    @Override
//    public boolean ifNewBusinessAdded() {
//        return false;
//    }
//

    /////////////////////////////////////////////
    //Implementing Singleton
    private static ListDatabase  instance = null;
    public static ListDatabase getDB(){
        if(instance == null)
            instance = new ListDatabase();
        return instance;
    }
    /////////////////////////////////////////////

        private static ArrayList<Account> accountList = new ArrayList<Account>();
        private static long AccountNumber = 0;

        @Override
        public boolean addNewAccount(String UserName, String Password) {
            accountList.add(new Account(++AccountNumber,UserName,Password));
            return true;
        }

        @Override
        public ArrayList<Account> getAccountList() {
            return accountList;
        }

        @Override
        public Account getAccount(long id) throws Exception {
            Account curr = null;
            //running on the list trying to find.
            for (int i =0;i<accountList.size();i++){
                curr = accountList.get(i);
                if(curr.AccountNumber == id)
                    break;
            }
            if(curr == null)
                throw new Exception("Cannot find this Account");
            return curr;
        }

        @Override
        public Account getAccount(String username) throws Exception {
            Account curr = null;
            //running on the list trying to find.
            for (int i =0;i<accountList.size();i++){
                curr = accountList.get(i);
                if(curr.UserName.equals(username))
                    break;
            }
            if(curr == null)
                throw new Exception("Cannot find this Account");
            return curr;
        }

        @Override
        public boolean isRegistered(String userName) {
            Account curr = null;
            //running on the list trying to find.
            for (int i=0 ; i<accountList.size();i++){
                curr = accountList.get(i);
                if(curr.UserName.equals(userName))
                    return true;
            }
            return false;
        }



    /////////////////////////////////////////////
        private static ArrayList<Attraction>  attractionsList = new ArrayList<Attraction>();

        private boolean latelyAddedNewAttraction;

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
            attractionsList.add(new Attraction(Type,Country,StartDate,EndDate,Price,Description,BusinessID));
            latelyAddedNewAttraction = true;
            return true;
        }

        /**
         * @return list of the attraction in the database
         */
        @Override
        public ArrayList<Attraction> getAttractionList() {
            return attractionsList;
        }

    @Override
    public boolean ifNewAttractionAdded() {
        if(latelyAddedNewAttraction) {
            latelyAddedNewAttraction = false;
            return true;
        }
        return false;
    }

    /////////////////////////////////////////////
    private static ArrayList<Business> businessList = new ArrayList<Business>();

    private boolean latelyAddedNewBusiness;
        @Override
        public boolean addNewBusiness(String ID, String Name, Address address, String Email, URL Website) {
            businessList.add(new Business(ID,Name,address,Email,Website));
            latelyAddedNewBusiness = true;
            return true;
        }

        @Override
        public ArrayList<Business> getBusinessList() {
            return businessList;
        }

    /**
     * checks if lately a new Business has been added.
     * @return
     */
    @Override
    public boolean ifNewBusinessAdded() {
        if(latelyAddedNewBusiness) {
            latelyAddedNewBusiness = false;
            return true;
        }
        return false;
    }
    /////////////////////////////////////////////

}
