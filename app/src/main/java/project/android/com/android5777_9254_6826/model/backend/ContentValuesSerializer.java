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
        values.put("username",account.UserName);
        values.put("password",account.Password);
        values.put("accountnumber",account.AccountNumber);
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
        values.put("attractionid",attraction.AttractionID);
        //saving the enum as String
        values.put("type",attraction.Type.toString());
        values.put("country",attraction.Country);
        values.put("startdate",attraction.StartDate.toString());
        values.put("enddate",attraction.EndDate.toString());
        values.put("price",attraction.Price);
        values.put("description",attraction.Description);
        values.put("businessid",attraction.BusinessID);
        return values;
    }

    public static Business contentValuesToBusiness(ContentValues values) throws MalformedURLException {
        Address adr = new Address();
        adr.City = values.getAsString("city");
        adr.Country = values.getAsString("country");
        adr.Street = values.getAsString("street");

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
        values.put("id",business.BusinessID);
        values.put("name",business.BusinessName);
        values.put("country",business.BusinessAddress.Country);
        values.put("city",business.BusinessAddress.City);
        values.put("street",business.BusinessAddress.Street);
        values.put("email",business.Email);
        values.put("url",business.Website.toString());
        return values;
    }
}
