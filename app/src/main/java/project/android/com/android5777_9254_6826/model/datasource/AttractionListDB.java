package project.android.com.android5777_9254_6826.model.datasource;

import java.util.ArrayList;
import java.util.Date;

import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Properties;

/**
 * Created by Yair on 2016-11-21.
 */

public class AttractionListDB implements IAttractionDatabase {

    private static ArrayList<Attraction>  attractionsList = new ArrayList<Attraction>();

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
        return true;
    }

    /**
     * @return list of the attraction in the database
     */
    @Override
    public ArrayList<Attraction> getAttractionList() {
        return attractionsList;
    }
}
