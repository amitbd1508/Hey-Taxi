package com.tesseract.taxisharing.util;

import com.tesseract.taxisharing.model.DatabseTripHistory;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by BlackFlag on 9/16/2016.
 */
public class App {
    public  static String CURRENT_USER_EMAIL= "amit";
    public  final static String ACCOUNT = "user";

    public final static String TAXI_REQUST_YES="yes";
    public final static String TAXI_REQUST_NO="no";
    public final static String TAXI_REQUST_HIRED="hired";

    //shared preferances string
    public final static String heyTaxiUserLogIn="heyTaxiUserLogIn";
    public final static String heyTaxiUserEmail="heyTaxiUserEmail";
    public final static String heyTaxiUserFName="heyTaxiUserFName";

    public final static String TAXI_DRIVER_REQUST="driver_say_yes";


    public static String account="100";

    public  static DatabseTripHistory payment;

    public final static String userlocations="userlocations";
    public final static String taxirequest="taxirequest";
    public final static String taxidriverlocation="taxidriverlocation";



    public final static String dLat="21.0";
    public final static String dlon="90.0";

    public static String dateTimeNow()
    {
        return DateFormat.getTimeInstance().format(new Date());
    }


}
