package com.example.fdsystem;

public class bData {
    String DeviceID, DeviceArea, Level, up_down, maxHeight;

    public bData(String deviceID, String deviceArea, String level, String Up_down, String MaxHeight) {
        DeviceID = deviceID;
        DeviceArea = deviceArea;
        Level=level;
        up_down=Up_down;
        maxHeight=MaxHeight;
    }

    public bData() {
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
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
}
