package com.tpcodl.mylibdemo.qrcodemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanDataRequest {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Discom")
    @Expose
    private String discom;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscom() {
        return discom;
    }

    public void setDiscom(String discom) {
        this.discom = discom;
    }

}
