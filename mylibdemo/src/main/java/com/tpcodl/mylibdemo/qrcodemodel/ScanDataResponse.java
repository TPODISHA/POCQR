package com.tpcodl.mylibdemo.qrcodemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanDataResponse {
    @SerializedName("STATUS")
    @Expose
    private String status;
    @SerializedName("a3")
    @Expose
    private String iv;
    @SerializedName("a1")
    @Expose
    private String saltKey;
    @SerializedName("a2")
    @Expose
    private String versionKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;
    @SerializedName("Data")
    @Expose
    private List<DatumResponse> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSaltKey() {
        return saltKey;
    }

    public void setSaltKey(String saltKey) {
        this.saltKey = saltKey;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public void setVersionKey(String versionKey) {
        this.versionKey = versionKey;
    }


    public List<DatumResponse> getData() {
        return data;
    }

    public void setData(List<DatumResponse> data) {
        this.data = data;
    }
}
