package com.tesseract.taxisharing.model;

import android.location.Location;

/**
 * Created by BlackFlag on 9/9/2016.
 */
public class CurrentLocation {
    private Double latitude;
    private Double longitude;
    private String username;
    private String DateTime;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public CurrentLocation(Double latitude, Double longitude, String username, String dateTime) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        DateTime = dateTime;
    }

    public CurrentLocation() {

    }
}
