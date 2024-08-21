package com.tpcodl.mylibdemo.qrcodemodel;

public class GetSanDataValueModel {

    private String saltkey;
    private String versionkey;
    private String iv_dec;
    private String baseUrl;
    private String enctypeid;
    private String scanid;
    private String position;
    private String iv_enc;
    private String startDate;
    private String endDate;

    public GetSanDataValueModel(String saltkey, String versionkey, String iv_dec, String baseUrl, String enctypeid, String scanid, String position, String iv_enc, String startDate, String endDate) {
        this.saltkey = saltkey;
        this.versionkey = versionkey;
        this.iv_dec = iv_dec;
        this.baseUrl = baseUrl;
        this.enctypeid = enctypeid;
        this.scanid = scanid;
        this.position = position;
        this.iv_enc = iv_enc;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public GetSanDataValueModel() {

    }


    public String getSaltkey() {
        return saltkey;
    }

    public void setSaltkey(String saltkey) {
        this.saltkey = saltkey;
    }

    public String getVersionkey() {
        return versionkey;
    }

    public void setVersionkey(String versionkey) {
        this.versionkey = versionkey;
    }

    public String getIv_dec() {
        return iv_dec;
    }

    public void setIv_dec(String iv_dec) {
        this.iv_dec = iv_dec;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getEnctypeid() {
        return enctypeid;
    }

    public void setEnctypeid(String enctypeid) {
        this.enctypeid = enctypeid;
    }

    public String getScanid() {
        return scanid;
    }

    public void setScanid(String scanid) {
        this.scanid = scanid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIv_enc() {
        return iv_enc;
    }

    public void setIv_enc(String iv_enc) {
        this.iv_enc = iv_enc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }




}
