package com.tesseract.taxisharing.model;

/**
 * Created by BlackFlag on 9/17/2016.
 */
public class DriverLocation {
    String email;
    String name;

    public DriverLocation(String email, String name, String latitude, String longitude, String time) {
        this.email = email;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String latitude;
    String longitude;
    String time;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DriverLocation(String email, String latitude, String longitude, String time) {

        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public DriverLocation() {

    }
}
