package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentValues;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

/**
 * Created by Yair on 2016-12-05.
 */

public class ContentValuesSerializer {
    public static Account contentValuesToAccount(ContentValues values){
        Account toReturn = new Account(values.getAsLong("accountnumber"),values.getAsString("username"),
                values.getAsString("password"));
        return toReturn;
    }
    public static ContentValues accountToContentValues(Account account){
        ContentValues values = new ContentValues();
        values.put("username",account.getUserName());
        values.put("password",account.getPassword());
        values.put("accountnumber",account.getAccountNumber());
        return values;
    }

    public static Attraction contentValuesToAttraction(ContentValues values){
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date dates = null,datee = null;
        try {
             dates = (Date) format.parse(values.getAsString("startdate"));
             datee = (Date) format.parse(values.getAsString("startdate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Attraction toReturn = new Attraction(
                values.getAsString("attractionid"),
                (Properties.AttractionType.valueOf(values.getAsString("type"))),
                values.getAsString("country"),
                dates,
                datee,
                values.getAsFloat("price"),
                values.getAsString("description"),
                values.getAsString("businessid"));
        return toReturn;
    }
    public static ContentValues attractionToContentValues(Attraction attraction){
        ContentValues values = new ContentValues();
        values.put("attractionid",attraction.getAttractionID());
        //saving the enum as String
        values.put("type",attraction.getType().toString());
        values.put("country",attraction.getCountry());
        values.put("startdate",attraction.getStartDate().toString());
        values.put("enddate",attraction.getEndDate().toString());
        values.put("price",attraction.getPrice());
        values.put("description",attraction.getDescription());
        values.put("businessid",attraction.getBusinessID());
        return values;
    }

    public static Business contentValuesToBusiness(ContentValues values) throws MalformedURLException {
        Address adr = new Address();
        adr.setCity(values.getAsString("city"));
        adr.setCountry(values.getAsString("country"));
        adr.setStreet(values.getAsString("street"));

        URL url = new URL(values.getAsString("url"));

        Business toReturn = new Business(
                values.getAsString("id"),
                values.getAsString("name"),
                adr,
                values.getAsString("email"),
                url);
        return toReturn;
    }
    public static ContentValues businessToContentValues(Business business){
        ContentValues values = new ContentValues();
        values.put("id",business.getBusinessID());
        values.put("name",business.getBusinessName());
        values.put("country",business.getBusinessAddress().getCountry());
        values.put("city",business.getBusinessAddress().getCity());
        values.put("street",business.getBusinessAddress().getStreet());
        values.put("email",business.getEmail());
        values.put("url",business.getWebsite().toString());
        return values;
    }
}
