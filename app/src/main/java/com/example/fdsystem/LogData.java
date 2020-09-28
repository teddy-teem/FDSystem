package com.example.fdsystem;

public class LogData {
     String DeviceID, DeviceArea, up_down, Unit_h, Unit_t;
     double Level, maxHeight;


    public LogData() {
    }

    public LogData(String deviceID, String deviceArea, double level, String up_down, double MxHeight, String unit_h, String unit_t) {
        DeviceID = deviceID;
        DeviceArea = deviceArea;
        Level = level;
        this.up_down = up_down;
        maxHeight = MxHeight;
        Unit_h = unit_h;
        Unit_t = unit_t;
    }
    public String getDeviceID() {
        return DeviceID;
    }
    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
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

    public double getLevel() {
        return Level;
    }

    public String getUnit_h() {
        return Unit_h;
    }

    public void setUnit_h(String unit_h) {
        Unit_h = unit_h;
    }

    public String getUnit_t() {
        return Unit_t;
    }

    public void setUnit_t(String unit_t) {
        Unit_t = unit_t;
    }

    public void setLevel(double level) {
        Level = level;
    }

    public String getUp_down() {
        return up_down;
    }

    public void setUp_down(String up_down) {
        this.up_down = up_down;
    }
}
