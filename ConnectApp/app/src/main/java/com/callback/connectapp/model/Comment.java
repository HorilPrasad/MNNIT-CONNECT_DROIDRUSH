package com.callback.connectapp.model;

public class Comment {
  private String user;
  private String date;
  private String comment;

    public Comment (String userId , String date , String comment) {
        this.user = userId;
        this.date = date;
        this.comment = comment;
    }

    public String getUser () {
        return user;
    }

    public void setUser (String user) {
        this.user = user;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }
}
