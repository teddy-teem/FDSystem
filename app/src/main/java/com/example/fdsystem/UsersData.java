package com.example.fdsystem;

public class UsersData {
    String email, name, password, mobile, status;

    public UsersData() {
    }

    public UsersData(String email, String name, String password, String mobile, String status) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}