package com.tesseract.taxisharing.model;

/**
 * Created by hhson on 9/18/2016.
 */
public class SingleToneTripHistory {

    public static SingleToneTripHistory mInstance = null;
    public String strFrom = "";
    public String strTo = "";
    public String strTime = "";
    public String strShare = "";
    public String strPerson = "";
    public String strPersonEmail = "";
    public String strDriver = "";
    public String strDriverEmail = "";
    public String strCarName = "";
    public String strUserName = "";

    protected SingleToneTripHistory() {
    }

    public static synchronized SingleToneTripHistory getInstance() {
        if (null == mInstance) {
            mInstance = new SingleToneTripHistory();
        }
        return mInstance;
    }
}
