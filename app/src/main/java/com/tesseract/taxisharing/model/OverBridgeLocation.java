package com.tesseract.taxisharing.model;

/**
 * Created by BlackFlag on 11/11/2016.
 */

public class OverBridgeLocation {
    public Double latitude;
    public Double longitude;
    public  String placeName;
    public  String area;
    public  String bridgeType;

    public OverBridgeLocation() {
    }

    public OverBridgeLocation(Double latitude, Double longitude, String placeName, String area, String bridgeType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
        this.area = area;
        this.bridgeType = bridgeType;
    }

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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
    }
}
