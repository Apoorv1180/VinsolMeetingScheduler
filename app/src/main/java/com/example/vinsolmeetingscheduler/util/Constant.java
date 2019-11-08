package com.example.vinsolmeetingscheduler.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Constant {

    public static String BASE_URL = "http://fathomless-shelf-5846.herokuapp.com/api/";


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }


    public static  String convertDateToString(Date date){
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");

        String dateString = formatter.format(date);
        Log.e("date",dateString);

        return dateString;
    }

    public static Date convertStringToDate(String dateString){

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date incerementAdateByOneDay(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();

        return date;
    }
    public static Date decrementAdataByOneDay(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        date = c.getTime();

        return date;
    }

    public static void saveDate(Context context,String dateStr){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("date", dateStr); // Storing string date
        editor.commit();

    }

    public static String getDate(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        return  pref.getString("date", null); // getting String date
    }

    public static long getTimeInMilliseconds(Calendar date,String strTime){
        String hourmin[] = strTime.split(":");
        String name = hourmin[0];
        String pass = hourmin[1];

        date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),Integer.valueOf(name),Integer.valueOf(pass));
        long milliseconds = date.getTimeInMillis();
        Log.e("Mill",String.valueOf(milliseconds));
        return milliseconds ;
    }
}
