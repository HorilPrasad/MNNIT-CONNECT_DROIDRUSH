package com.callback.connectapp.model;

public class Comment {
    private String userName;
    private String date;
    private String profileImg;
    private String commentText;

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getProfileImg () {
        return profileImg;
    }

    public void setProfileImg (String profileImg) {
        this.profileImg = profileImg;
    }

    public String getCommentText () {
        return commentText;
    }

    public void setCommentText (String commentText) {
        this.commentText = commentText;
    }
}
