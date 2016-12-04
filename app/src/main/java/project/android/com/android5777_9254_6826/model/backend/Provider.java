package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class Provider extends ContentProvider {
    String currentUri = "content://"+"project.android.com.android5777_9254_6826.model.backend.Provider/";
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
        switch (match){
            case ATTRACTIONS_ID:
                rowID = db.removeAttraction(selectionArgs[0]);
                break;
            case BUSINESS_ID:
                break;
            case ACCOUNTS_ID:
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
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
