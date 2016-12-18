package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import project.android.com.android5777_9254_6826.model.datasource.IAccountDatabase;
import project.android.com.android5777_9254_6826.model.datasource.IAttractionDatabase;
import project.android.com.android5777_9254_6826.model.datasource.IBusinessDatabase;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

import static android.os.Build.ID;

/**
 * Created by Yair on 2016-11-27.
 */

public class ListDatabase implements Backend {

    private static ArrayList<Account> accountList = new ArrayList<Account>();
    private static ArrayList<Business> businessList = new ArrayList<Business>();
    private static ArrayList<Attraction> attractionsList = new ArrayList<Attraction>();
    private static long AccountNumber = 0;
    private static long BusinessNumber = 0;
    private static long AttractionNumber = 0;

    private boolean latelyAddedNewAttraction;
    private boolean latelyAddedNewBusiness;

    @Override
    public int addNewAccount(String UserName, String Password) {
        Account a = new Account(++AccountNumber, UserName, Password);
        accountList.add(a);
        return accountList.indexOf(a);
    }

    @Override
    public int addNewAccount(Account toInsert) {
        accountList.add(toInsert);
        return accountList.indexOf(toInsert);
    }

    @Override
    public ArrayList<Account> getAccountList() {return accountList;}

    public Cursor CgetAccountList() {
        Account acc;

        MatrixCursor accountCursor = new MatrixCursor(new String[]{"AccountNumber","UserName", "Password"});

        for (int i =0; i < accountList.size();i++){
            acc = accountList.get(i);
            accountCursor.addRow(new Object[]{acc.getAccountNumber(),acc.getUserName(),acc.getPassword()});
        }
        return accountCursor;
    }



    @Override
    public Account getAccount(long id) throws Exception {
        Account curr = null;
        //running on the list trying to find.
        for (int i = 0; i < accountList.size(); i++) {
            curr = accountList.get(i);
            if (curr.getAccountNumber() == id)
                break;
        }
        if (curr == null)
            throw new Exception("Cannot find this Account");
        return curr;
    }

    @Override
    public Account getAccount(String username) throws Exception {
        Account curr = null;
        //running on the list trying to find.
        for (int i = 0; i < accountList.size(); i++) {
            curr = accountList.get(i);
            if (curr.getUserName().equals(username))
                break;
            else
                curr = null;
        }
        if (curr == null)
            throw new Exception("Cannot find this Account");
        return curr;
    }

    @Override
    public boolean isRegistered(String userName) {
        Account curr = null;
        //running on the list trying to find.
        for (int i = 0; i < accountList.size(); i++) {
            curr = accountList.get(i);
            if (curr.getUserName().equals(userName))
                return true;
        }
        return false;
    }

    @Override
    public boolean verifyPassword(String userName, String passToCheck) throws Exception {
        Account curr = getAccount(userName);
        if (curr.getPassword().equals(passToCheck))
            return true;
        return false;
    }

    @Override
    public int removeAccount(String username) {
        return 0;
    }

    @Override
    public int removeAccount(int rowID) {
        try {
            accountList.remove(rowID);
            return rowID;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Uri insert(Account ac) {
        return null;
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
    public int addNewAttraction(Properties.AttractionType Type, String AttractionName, String Country, String StartDate, String EndDate, float Price, String Description, String BusinessID) {
        String id = Long.toString(++AccountNumber);
        Attraction a = new Attraction(id, Type,AttractionName, Country, StartDate, EndDate, Price, Description, BusinessID);
        attractionsList.add(a);
        latelyAddedNewAttraction = true;
        return attractionsList.indexOf(a);
    }

    @Override
    public int addNewAttraction(Attraction toInsert) {
        attractionsList.add(toInsert);
        return attractionsList.indexOf(toInsert);
    }

    /**
     * @return list of the attraction in the database
     */
    @Override
    public ArrayList<Attraction> getAttractionList() {
        return attractionsList;
    }

    @Override
    public ArrayList<Attraction> getAttractionList(String BusinessID) {
        ArrayList<Attraction> nw = new ArrayList<>();
        for(int i = 0;i<attractionsList.size();i++){
            if(attractionsList.get(i).getBusinessID().equals(BusinessID))
                nw.add(attractionsList.get(i));
        }
        return nw;
    }

    public Cursor CgetAttractionList() {
        Attraction att;

        MatrixCursor attractionCursor = new MatrixCursor(
                new String[]{"AttractionID","Type", "Country",
                "StartDate","EndDate","Price","Description","BusinessID"});
        for (int i =0; i < attractionsList.size();i++){
            att = attractionsList.get(i);
            attractionCursor.addRow(new Object[]{att.getAttractionID(),att.getType(),
            att.getCountry(),att.getStartDate(),att.getEndDate(),
            att.getPrice(),att.getDescription(),att.getBusinessID()});
        }
        return attractionCursor;
    }

    @Override
    public Attraction getAttraction(String attractionID) throws Exception {
        Attraction curr = null;
        //running on the list trying to find.
        for (int i = 0; i < attractionsList.size(); i++) {
            curr = attractionsList.get(i);
            if (curr.getBusinessID().equals(attractionID))
                break;
        }
        if (curr == null)
            throw new Exception("Cannot find this Account");
        return curr;

    }

    @Override
    public boolean ifNewAttractionAdded() {
        if (latelyAddedNewAttraction) {
            latelyAddedNewAttraction = false;
            return true;
        }
        return false;
    }

    @Override
    public int removeAttraction(String attractionID) {
        return 0;
    }

    @Override
    public int removeAttraction(int rowID) {
        try {
            attractionsList.remove(rowID);
            return rowID;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Uri insert(Attraction ac) {
        return null;
    }

    @Override
    public int addNewBusiness(String accountID,  String Name, Address address, String Email, URL Website) {
        Business a = new Business(accountID,Long.toString(++BusinessNumber), Name, address, Email, Website);
        businessList.add(a);
        latelyAddedNewBusiness = true;
        return businessList.indexOf(a);
    }

    @Override
    public int addNewBusiness(Business toInsert) {
        businessList.add(toInsert);
        return businessList.indexOf(toInsert);
    }

    @Override
    public ArrayList<Business> getBusinessList() {
        return businessList;
    }

    public Cursor CgetBusinessList() {
        Business bus;
        MatrixCursor businessCursor = new MatrixCursor(
                new String[]{"BusinessID","BusinessName", "BusinessAddress",
                        "Email","Website"});
        for (int i =0; i < businessList.size();i++){
            bus = businessList.get(i);
            businessCursor.addRow(new Object[]{bus.getBusinessID(),bus.getBusinessName(),
            bus.getBusinessAddress(), bus.getEmail(), bus.getWebsite()});
        }
        return businessCursor;
    }

    @Override
    public Business getBusiness(String businessID) throws Exception {
        Business curr = null;
        //running on the list trying to find.
        for (int i = 0; i < businessList.size(); i++) {
            curr = businessList.get(i);
            if (curr.getBusinessID().equals(businessID))
                break;
        }
        if (curr == null)
            throw new Exception("Cannot find this Account");
        return curr;

    }


    /**
     * checks if lately a new Business has been added.
     *
     * @return
     */
    @Override
    public boolean ifNewBusinessAdded() {
        if (latelyAddedNewBusiness) {
            latelyAddedNewBusiness = false;
            return true;
        }
        return false;
    }

    @Override
    public int removeBusiness(String businessID) {
        return 0;
    }

    @Override
    public int removeBusiness(int rowID) {
        try {
            businessList.remove(rowID);
            return rowID;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Uri insert(Business ac) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public ArrayList<Business> getBusinessList(String AccountID) {
        ArrayList<Business> toReturn = new ArrayList<Business>();
        for(int i=0;i<businessList.size();i++){
            if(AccountID.equals(businessList.get(i).getAccountID()))
                toReturn.add(businessList.get(i));
        }
        return toReturn;
    }

}
