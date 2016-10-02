package com.tesseract.taxisharing.model;

/**
 * Created by hhson on 9/18/2016.
 */
public class DatabseTripHistory {


    public boolean isReqest = false;
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

    public void init() {
        isReqest = false;
        strFrom = "";
        strTo = "";
        strTime = "";
        strShare = "";
        strPerson = "";
        strPersonEmail = "";
        strDriver = "";
        strDriverEmail = "";
        strCarName = "";
        strUserName = "";

    }

    public DatabseTripHistory() {

    }

}
