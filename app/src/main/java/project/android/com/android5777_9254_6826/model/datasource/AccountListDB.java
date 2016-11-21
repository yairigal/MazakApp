package project.android.com.android5777_9254_6826.model.datasource;

import android.widget.Toast;

import java.util.ArrayList;

import project.android.com.android5777_9254_6826.model.entities.Account;

/**
 * Created by Yair on 2016-11-21.
 */

public class AccountListDB implements IAccountDatabase {
    private static ArrayList<Account> accountList = new ArrayList<Account>();
    private static long AccountNumber = 0;

    //Implementing Singleton
    private static AccountListDB instance = null;
    public static AccountListDB getDB(){
        if(instance == null)
            instance = new AccountListDB();
        return instance;
    }

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
}
