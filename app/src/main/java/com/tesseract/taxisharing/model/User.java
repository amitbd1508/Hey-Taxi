package com.tesseract.taxisharing.model;

/**
 * Created by BlackFlag on 9/11/2016.
 */
public class User {
    private String fullname;
    private String username;
    private String password;
    private String phone;
    private String sex;
    private String image_link;
    private String email;

    public User() {
    }

    public User(String fullname, String username, String password, String phone, String sex, String image_link, String email) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.sex = sex;
        this.image_link = image_link;
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
