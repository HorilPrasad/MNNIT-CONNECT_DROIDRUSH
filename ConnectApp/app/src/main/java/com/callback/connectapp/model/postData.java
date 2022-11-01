package com.callback.connectapp.model;

public class postData {

    private String userId;
    private String communityName;
    private String communityId;
    private String time;
    private String info;
    private String likes;
    private String dislikes;
    private String comments;
    private String image;
    private String profileUrl;
    private String postShareLink;

    public postData() {
    }

    public postData (String userId , String info , String image) {
        this.userId = userId;
        this.info = info;
        this.image = image;
    }

    public postData (String name , String communityName , String time , String postText , String likeCount , String dislikeCount , String commentCount , String imgUrl , String profileUrl , String postShareLink) {
        this.userId = name;
        this.communityName = communityName;
        this.time = time;
        this.info = postText;
        this.likes = likeCount;
        this.dislikes = dislikeCount;
        this.comments = commentCount;
        this.image = imgUrl;
        this.profileUrl = profileUrl;
        this.postShareLink = postShareLink;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getCommunityName () {
        return communityName;
    }

    public void setCommunityName (String communityName) {
        this.communityName = communityName;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public String getInfo () {
        return info;
    }

    public void setInfo (String info) {
        this.info = info;
    }

    public String getLikes () {
        return likes;
    }

    public void setLikes (String likes) {
        this.likes = likes;
    }

    public String getDislikes () {
        return dislikes;
    }

    public void setDislikes (String dislikes) {
        this.dislikes = dislikes;
    }

    public String getComments () {
        return comments;
    }

    public void setComments (String comments) {
        this.comments = comments;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public String getProfileUrl () {
        return profileUrl;
    }

    public void setProfileUrl (String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPostShareLink () {
        return postShareLink;
    }

    public void setPostShareLink (String postShareLink) {
        this.postShareLink = postShareLink;
    }
}
