package com.example.fdsystem;

public class LogData {
    String DeviceID, DeviceArea, Level, up_down;
    public String getDeviceID() {
        return DeviceID;
    }

    public LogData() {
    }

    public LogData(String deviceID, String deviceArea, String level, String up_down) {
        DeviceID = deviceID;
        DeviceArea = deviceArea;
        Level = level;
        this.up_down = up_down;
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
}
