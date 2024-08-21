package com.tpcodl.mylibdemo.qrcodemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumResponse {

    @SerializedName("scanId")
    @Expose
    private String scanId;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("ENC_TYPE_ID")
    @Expose
    private String ENC_TYPE_ID;
    @SerializedName("t1")
    @Expose
    private String t1;// iv for encryption
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("endDate")
    @Expose
    private String endDate;

    public String getENC_TYPE_ID() {
        return ENC_TYPE_ID;
    }

    public void setENC_TYPE_ID(String ENC_TYPE_ID) {
        this.ENC_TYPE_ID = ENC_TYPE_ID;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}

