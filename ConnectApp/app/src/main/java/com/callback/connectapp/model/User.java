package com.callback.connectapp.model;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String dob;
    private String location;
    private String _id;
    private String token;
    private String phone;
    private String regNo;
    private List <String> Saved;
    private String branch;
    private String imageUrl;

    public List <String> getSaved () {
        return Saved;
    }

    public void setSaved (List <String> saved) {
        Saved = saved;
    }


    public User() {
    }

    public User(String name, String email, String regNo, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.regNo = regNo;
    }

    public User(String email, String password, String token,boolean random) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public User(String email, String gender, String dob, String phone,  String branch, String imageUrl) {
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.branch = branch;
        this.imageUrl = imageUrl;
    }

    public User(String Name, String email,String gender, String dob, String phone,  String branch, String imageUrl) {
         this.name=Name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.branch = branch;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
