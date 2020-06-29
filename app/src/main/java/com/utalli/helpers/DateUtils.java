package com.utalli.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getTime(String milliSeconds){
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return formatter.format(calendar.getTime());
    }

    public static String getTimeDate(String milliSeconds){
        String time=getTime(milliSeconds);
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        String dateStr="";
        Date date=calendar.getTime();
        Date dateCurent=new Date();
//        if(date.compareTo(dateCurent)>0)
//            dateStr="Yesterday";
//        else
        if(date.compareTo(dateCurent)==0)
            dateStr="Today";
        else
            dateStr=formatter.format(date);

        return  time+", " +dateStr;
    }


}
