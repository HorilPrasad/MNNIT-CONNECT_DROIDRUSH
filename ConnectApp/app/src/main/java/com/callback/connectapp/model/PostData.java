package com.callback.connectapp.model;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostData {
    private String _id;

    private String userId;
    private String communityName;
    private String communityId;
    private String time;
    private String info;
    private List <String> likes;
    private List <String> dislikes;
    private List <Comment> comments;
    private List <String> saved;
    private String image;
    private String profileUrl;
    private String postShareLink;

    public PostData() {
    }

    public PostData(String userId , String info , String image, String communityId, String time) {
        this.userId = userId;
        this.info = info;
        this.communityId=communityId;
        this.image = image;
        this.time=time;
    }

    public PostData(String name , String communityName , String time , String postText , List <String>  likeCount , List <String> dislikeCount , List<Comment>  commentCount , String imgUrl , String profileUrl , String postShareLink) {
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


    public List<String> getSaved() {
        return saved;
    }

    public void setSaved(List<String> saved) {
        this.saved = saved;
    }

    public String get_id () {
        return _id;
    }

    public void set_id (String _id) {
        this._id = _id;
    }

    public String getCommunityId () {
        return communityId;
    }

    public void setCommunityId (String communityId) {
        this.communityId = communityId;
    }

   public int getLikeCount(List<String> t){

        return t.size();
   }
    public int getCommenntCount(List<Comment> t){

        return t.size();
    }
    public int getDislikeCount(List<String> t){

        return t.size();
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

    public List <String>  getLikes () {
        return likes;
    }

    public void setLikes (List <String>  likes) {
        this.likes = likes;
    }

    public List <String> getDislikes () {
        return dislikes;
    }

    public void setDislikes (List <String> dislikes) {
        this.dislikes = dislikes;
    }

    public List<Comment>  getComments () {
        return comments;
    }

    public void setComments (List<Comment>  comments) {
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

    // getRelativeTimeAgo("2020-07-07T16:07:26.465Z");

    public String getTimeIn() {

        return getRelativeTimeAgo(time);
    }

    // getRelativeTimeAgo("2020-07-07T16:07:26.465Z");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String parseFormat = "EEE MMM dd hh:mm:ss zzz yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(parseFormat, Locale.ENGLISH);

        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();

            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE ).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return relativeDate;
    }
    public static String getSimpleDate(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String newFormat = "MMMM dd, yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.applyPattern(newFormat);
        String newDate = sf.format(new Date(rawJsonDate));
        Log.i("Tweet", "newDate: " + newDate);
        return newDate;
    }
}
