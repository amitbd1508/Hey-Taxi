package com.tesseract.taxisharing.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhson on 9/18/2016.
 */
public class TripHistory {

    public static TripHistory mInstance = null;
    public String strFrom = " ";
    public String strTo = " ";
    public String strTime = " ";
    public String strShare = " ";
    public String strPerson = " ";
    public String strPersonEmail = " ";
    public String strDriver = " ";
    public String strDriverEmail = " ";
    public String strCarName = " ";
    public String strUserName = " ";

    protected TripHistory() {
    }

    public static synchronized TripHistory getInstance() {
        if (null == mInstance) {
            mInstance = new TripHistory();
        }
        return mInstance;
    }
}
