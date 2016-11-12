package com.tesseract.taxisharing.model;

/**
 * Created by BlackFlag on 11/12/2016.
 */

public class UserSC {

    public String fullName;
    public String email;
    public String nid;

    public UserSC() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDisibalestatus() {
        return disibalestatus;
    }

    public void setDisibalestatus(String disibalestatus) {
        this.disibalestatus = disibalestatus;
    }

    public String password;
    public String gender;
    public String disibalestatus;

}
