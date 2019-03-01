package com.linone.wb_demo;

import com.google.gson.annotations.SerializedName;

public class jsonObject {
    @SerializedName("created_at")
    private String created_at;

    @SerializedName("entry_id")
    private String entry_id;

    @SerializedName("field1")
    private String Watertemp;

    @SerializedName("field2")
    private String Temp;

    @SerializedName("field3")
    private String Humi;

    @SerializedName("field4")
    private String PH;

    public jsonObject(String created_at, String entry_id, String watertemp, String temp, String humi,String ph) {
        this.created_at = created_at;
        this.entry_id = entry_id;
        Watertemp = watertemp;
        Temp = temp;
        Humi = humi;
        PH = ph;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(String entry_id) {
        this.entry_id = entry_id;
    }

    public String getWatertemp() {
        return Watertemp;
    }

    public void setWatertemp(String watertemp) {
        Watertemp = watertemp;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getHumi() {
        return Humi;
    }

    public void setHumi(String humi) {
        Humi = humi;
    }

    public String getPH() {
        return PH;
    }

    public void setPH(String PH) {
        this.PH = PH;
    }
}
