package com.example.fdsystem;

public class Data {
    private String DeviceID, DeviceName, wLevel, UpDown;

    public Data(String deviceID, String deviceName, String wLevel, String upDown) {
        DeviceID = deviceID;
        DeviceName = deviceName;
        this.wLevel = wLevel;
        UpDown = upDown;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public String getwLevel() {
        return wLevel;
    }

    public String getUpDown() {
        return UpDown;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public void setwLevel(String wLevel) {
        this.wLevel = wLevel;
    }

    public void setUpDown(String upDown) {
        UpDown = upDown;
    }
}
