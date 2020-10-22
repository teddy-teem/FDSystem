package com.example.fdsystem;

public class bData {
    String DeviceID, DeviceArea, up_down,Unit_t, Unit_h;
    Double Level, maxHeight;
    public bData(String deviceID, String deviceArea, Double level, String Up_down, Double MaxHeight, String unit_t, String unit_h) {
        DeviceID = deviceID;
        DeviceArea = deviceArea;
        Level=level;
        up_down=Up_down;
        maxHeight=MaxHeight;
        Unit_h = unit_h;
        Unit_t = unit_t;
    }

    public bData() {
    }

    public Double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Double getLevel() {
        return Level;
    }

    public void setLevel(Double level) {
        Level = level;
    }

    public String getUp_down() {
        return up_down;
    }

    public void setUp_down(String up_down) {
        this.up_down = up_down;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getDeviceArea() {
        return DeviceArea;
    }

    public void setDeviceArea(String deviceArea) {
        DeviceArea = deviceArea;
    }

    public String getUnit_t() {
        return Unit_t;
    }

    public void setUnit_t(String unit_t) {
        Unit_t = unit_t;
    }

    public String getUnit_h() {
        return Unit_h;
    }

    public void setUnit_h(String unit_h) {
        Unit_h = unit_h;
    }
}
