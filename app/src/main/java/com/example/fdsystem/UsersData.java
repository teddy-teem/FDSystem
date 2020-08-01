package com.example.fdsystem;

public class UsersData {
    String area, email, mobile, name, password, status, subArea,  unitH, unitT;

    public UsersData() {
    }

    public UsersData(String area, String email, String mobile, String name, String password, String status, String subArea, String unitH, String unitT) {
        this.area = area;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.password = password;
        this.status = status;
        this.subArea = subArea;
        this.unitH = unitH;
        this.unitT = unitT;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubArea() {
        return subArea;
    }

    public void setSubArea(String subArea) {
        this.subArea = subArea;
    }

    public String getUnitH() {
        return unitH;
    }

    public void setUnitH(String unitH) {
        this.unitH = unitH;
    }

    public String getUnitT() {
        return unitT;
    }

    public void setUnitT(String unitT) {
        this.unitT = unitT;
    }
}
