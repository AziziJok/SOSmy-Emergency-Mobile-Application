package com.psm.sosmyv3;


public class Dispatch {
    private String dispatchID;
    private String password;
    private String cpassword;
    private String name;
    private String ICNo;

    public Dispatch() {
    }

    public Dispatch(String dispatchID, String password, String cpassword, String name, String ICNo) {
        this.dispatchID = dispatchID;
        this.password = password;
        this.cpassword = cpassword;
        this.name = name;
        this.ICNo = ICNo;
    }

    public String getDispatchID() {
        return dispatchID;
    }

    public void setDispatchID(String dispatchID) {
        this.dispatchID = dispatchID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getICNo() {
        return ICNo;
    }

    public void setICNo(String ICNo) {
        this.ICNo = ICNo;
    }
}
