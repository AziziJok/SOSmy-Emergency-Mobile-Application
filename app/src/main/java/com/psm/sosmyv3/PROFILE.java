package com.psm.sosmyv3;

public class PROFILE {

    private String name;
    private String telNo;
    private String address;
    private String trustedTelNo;
    private String ICNo;

    public PROFILE() {
    }


    public PROFILE(String name, String telNo, String address, String trustedTelNo, String ICNo) {
        this.name = name;
        this.telNo = telNo;
        this.address = address;
        this.trustedTelNo = trustedTelNo;
        this.ICNo = ICNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTrustedTelNo() {
        return trustedTelNo;
    }

    public void setTrustedTelNo(String trustedTelNo) {
        this.trustedTelNo = trustedTelNo;
    }

    public String getICNo() {
        return ICNo;
    }

    public void setICNo(String ICNo) {
        this.ICNo = ICNo;
    }

}
