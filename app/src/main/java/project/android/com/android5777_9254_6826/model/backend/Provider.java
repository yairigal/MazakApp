package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;

import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;

public class Provider extends ContentProvider {
    String currentUri = "content://"+"project.android.com.android5777_9254_6826.model.backend.Provider/";
    Uri thisUri = Uri.parse(currentUri);
    //this is a sample of a Uri to the database :
    //currentUri+"Business/1";
    //currentUri+"Attractions/1";
    //currentUri+"Accounts/1";
    UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    Backend db = FactoryDatabase.getDatabase();
    final int ACCOUNTS = 0,BUSINESS = 1,ATTRACTIONS = 2,ACCOUNTS_ID = 3,BUSINESS_ID = 4,ATTRACTIONS_ID = 5;

    public Provider() {
        initMatcher();
    }

    //TODO implement the content provider functions.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int match = matcher.match(uri);
        int rowID = 0;
        int rowNum;
        switch (match){
            case ATTRACTIONS_ID:
                rowNum = Integer.parseInt(uri.getLastPathSegment());
                rowID = db.removeAttraction(rowNum);
                break;
            case BUSINESS_ID:
                rowNum = Integer.parseInt(uri.getLastPathSegment());
                rowID = db.removeBusiness(rowNum);
                break;
            case ACCOUNTS_ID:
                rowNum = Integer.parseInt(uri.getLastPathSegment());
                rowID = db.removeAccount(rowNum);
                break;
            default:
                return -1;
        }
        return rowID;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int rowID = 0;
        switch (matcher.match(uri))
        {
            case ACCOUNTS:
                Account curr = ContentValuesSerializer.contentValuesToAccount(values);
                rowID = db.addNewAccount(curr);
                break;
            case BUSINESS:
                try {
                    Business buss = ContentValuesSerializer.contentValuesToBusiness(values);
                    rowID = db.addNewBusiness(buss);
                } catch (MalformedURLException e) {
                    return null;
                }
                break;
            case ATTRACTIONS:
                Attraction att = ContentValuesSerializer.contentValuesToAttraction(values);
                rowID = db.addNewAttraction(att);
                break;


            default:
                return null;
        }
        Uri toReturn = Uri.parse(uri.toString() +"/"+rowID);
        return toReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String token = uri.getLastPathSegment();
        switch (matcher.match(uri)) {
            case ACCOUNTS:
                return db.CgetAccountList();
            case BUSINESS:
                return db.CgetBusinessList();
            case ATTRACTIONS:
                return db.CgetAttractionList();
            case ACCOUNTS_ID:
                try {

                    Account acc = db.getAccount(token);
                    MatrixCursor acccursor = new MatrixCursor(new String[]{"AccountNumber", "UserName", "Password"});
                    acccursor.addRow(new Object[]{acc.getAccountNumber(), acc.getUserName(), acc.getPassword()});
                } catch (Exception e) {
                }
                break;
            case BUSINESS_ID:

                try {
                    Business bus = db.getBusiness(token);

                    MatrixCursor buscursor = new MatrixCursor(
                            new String[]{"BusinessID", "BusinessName", "BusinessAddress",
                                    "Email", "Website"});
                    buscursor.addRow(new Object[]{bus.getBusinessID(), bus.getBusinessName(),
                            bus.getBusinessAddress(), bus.getEmail(), bus.getWebsite()});
                    return buscursor;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ATTRACTIONS_ID:

                Attraction att = null;
                try {
                    att = db.getAttraction(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MatrixCursor attcursor = new MatrixCursor(
                        new String[]{"AttractionID", "Type", "Country",
                                "StartDate", "EndDate", "Price", "Description", "BusinessID"});
                attcursor.addRow(new Object[]{att.getAttractionID(), att.getType(),
                        att.getCountry(), att.getStartDate(), att.getEndDate(),
                        att.getPrice(), att.getDescription(), att.getBusinessID()});
                return attcursor;
            default:
                return null;
        }
        // TODO: implement it when sqlite is coming
        return null;
    }







    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        delete(uri,selection,selectionArgs);
        Uri newUri = Uri.parse(uri.getPath());
        Uri ret = insert(newUri,values);
        int row =  Integer.parseInt(ret.getLastPathSegment());
        return row;
    }
    private void initMatcher(){
        matcher.addURI(currentUri,"/Accounts",ACCOUNTS);
        matcher.addURI(currentUri,"/Business",BUSINESS);
        matcher.addURI(currentUri,"/Attractions",ATTRACTIONS);
        matcher.addURI(currentUri,"/Accounts/*",ACCOUNTS_ID);
        matcher.addURI(currentUri,"/Business/*",BUSINESS_ID);
        matcher.addURI(currentUri,"/Attractions/*",ATTRACTIONS_ID);
    }
}
