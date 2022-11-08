package com.callback.connectapp.model;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment {
  private String user;
  private String time;
  private String comment;

    public Comment (String userId , String date , String comment) {
        this.user = userId;
        this.time = date;
        this.comment = comment;
    }


    public String getUser () {
        return user;
    }




    public void setUser(String user) {

        this.user = user;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }


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
