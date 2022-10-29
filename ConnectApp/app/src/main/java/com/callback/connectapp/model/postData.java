package com.callback.connectapp.model;

import java.util.ArrayList;

public class postData {
<<<<<<< HEAD
=======
    private String name;
    private String communityName;
    private String time;
    private String postText;
    private String likeCount;
    private String dislikeCount;
    private String commentCount;
    private String imgUrl;
    private String profileUrl;
    private String postShareLink;

    public postData() {
    }

>>>>>>> 83c68ca19382036d383dcb8fe4fea3f972cdff35
    public postData (String name , String communityName , String time , String postText , String likeCount , String dislikeCount , String commentCount , String imgUrl , String profileUrl , String postShareLink) {
        this.name = name;
        this.communityName = communityName;
        this.time = time;
        this.postText = postText;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.imgUrl = imgUrl;
        this.profileUrl = profileUrl;
        this.postShareLink = postShareLink;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
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

    public String getPostText () {
        return postText;
    }

    public void setPostText (String postText) {
        this.postText = postText;
    }

    public String getLikeCount () {
        return likeCount;
    }

    public void setLikeCount (String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount () {
        return dislikeCount;
    }

    public void setDislikeCount (String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getCommentCount () {
        return commentCount;
    }

    public void setCommentCount (String commentCount) {
        this.commentCount = commentCount;
    }

    public String getImgUrl () {
        return imgUrl;
    }

    public void setImgUrl (String imgUrl) {
        this.imgUrl = imgUrl;
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
