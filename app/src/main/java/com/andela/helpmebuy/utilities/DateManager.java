package com.andela.helpmebuy.utilities;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static java.text.DateFormat.getDateInstance;

/**
 * Created by andeladev on 20/05/2016.
 */
public class DateManager {
    public final long SECS = 59;
    public final long MINS = (60 * 60) - 1;
    public final long HOUR = (60 * 60 * 24) - 1;

    public long currentTime(){
        Date date = new Date();
        return date.getTime();
    }

    public static String getDate (Date date) {
        return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US).format(date);
    }

    public boolean compareDates(long date1){
        Date d1 = new Date(date1);
        Date d2 = new Date();
        if(getDate(d1).substring(8,11).equals(getDate(d2).substring(8,11))){
            return true;
        }
        return false;
    }

    public String formatTime(long time){
        String answer = "";
        long diff = (currentTime() - time) / 1000;
        if(diff <= SECS){
            answer = "just now";
        }
        if(diff > SECS && diff <= MINS){
            diff = diff /(SECS + 1) ;
            if(diff > 1)
                return String.valueOf(diff) + " minutes ago";
            else
                return " A minute ago";
        }

        if(diff > MINS && diff <= HOUR){
            diff = diff /(MINS + 1) ;
            if(diff > 1)
                return String.valueOf(diff) + " Hours ago";
            else
                return "An Hour ago";
        }
        if(diff > HOUR){
            if(compareDates(time)){
                return new Date(time).toString().substring(0, 10);
            }
            return (getDateInstance(DateFormat.DEFAULT, Locale.US).format(new Date(time)).toString());
        }

        return answer;
    }
}
