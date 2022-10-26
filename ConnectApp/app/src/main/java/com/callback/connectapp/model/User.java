package com.callback.connectapp.model;

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

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String token,boolean random) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public User(String name, String email, String password, String gender, String dob, String location, String _id, String token, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.location = location;
        this._id = _id;
        this.token = token;
        this.phone = phone;
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
