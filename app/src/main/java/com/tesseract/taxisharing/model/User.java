package com.tesseract.taxisharing.model;

/**
 * Created by BlackFlag on 9/11/2016.
 */
public class User {
    private String givenName;
    private String username;
    private String password;
    private String userID;
    private String sex;
    private String image_link;

    public User(String givenName, String username, String password, String userID, String sex, String image_link) {
        this.givenName = givenName;
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.sex = sex;
        this.image_link = image_link;
    }

    public User() {
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
