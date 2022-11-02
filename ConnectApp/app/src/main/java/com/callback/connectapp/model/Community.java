package com.callback.connectapp.model;

import java.util.List;

public class Community {
    private String _id;
    private String userId;
    private String name;
    private String about;
    private String tag;
    private String rules;
    private String image;
    private List<String> admins;

    public Community() {
    }

    public Community(String userId, String name, String about, String tag, String rules) {
        this.userId = userId;
        this.name = name;
        this.about = about;
        this.tag = tag;
        this.rules = rules;
    }

    public Community(String userId, String name, String about, String tag, String rules, List<String> admins) {
        this.userId = userId;
        this.name = name;
        this.about = about;
        this.tag = tag;
        this.rules = rules;
        this.admins = admins;
    }

    public Community(String userId, String name, String about, String tag, String rules, String image, List<String> admins) {
        this.userId = userId;
        this.name = name;
        this.about = about;
        this.tag = tag;
        this.rules = rules;
        this.image = image;
        this.admins = admins;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
}
