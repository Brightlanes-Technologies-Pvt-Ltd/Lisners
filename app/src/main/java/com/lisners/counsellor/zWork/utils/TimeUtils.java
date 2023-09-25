package com.lisners.counsellor.zWork.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class TimeUtils {

    private static final Calendar cal = Calendar.getInstance();

    public static String currentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(cal.getTime());

    }

    public static String currentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(cal.getTime());
    }

    public static String changeTimeFormat(String time, String formatGiven, String formatTarget) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Date testDate = null;
        try {
            testDate = sdf.parse(time);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formatTarget);
        String newFormat = formatter.format(testDate);

        System.out.println(".....Date..." + newFormat);

        return newFormat;
    }

    public static String changeDateFormat(String date, String formatGiven, String targetFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }

        SimpleDateFormat formatter = new SimpleDateFormat(targetFormat);
        String newFormat = formatter.format(testDate);

        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }

    public static boolean isCurrentTimeBeforeGivenTime(String time, String formatGiven) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 2);
        String currentTime = sdf.format(cal.getTime());
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(currentTime);
            eDate = sdf.parse(time);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.before(eDate);

    }

    public static boolean isGivenTimeBeforeCurrentTime(String time, String formatGiven) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        String currentTime = sdf.format(cal.getTime());
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(time);
            eDate = sdf.parse(currentTime);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.before(eDate);

    }


    public static boolean isGivenTimeBeforeCurrentTimeWithout15Min(String time, String formatGiven) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.MINUTE, 15);
        String currentTime = sdf.format(cal.getTime());
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(time);
            eDate = sdf.parse(currentTime);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.before(eDate);

    }

    public static boolean is1TimeBefore2Time(String _1_time,String _2_time, String formatGiven) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 2);
        String currentTime = sdf.format(cal.getTime());
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(_1_time);
            eDate = sdf.parse(_2_time);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.before(eDate);

    }

    public static boolean isCurrentDateBeforeGivenDate(String date,String formatGiven){
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Calendar cal = Calendar.getInstance();
        String currentDate = sdf.format(cal.getTime());
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(currentDate);
            eDate = sdf.parse(date);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.before(eDate);
    }



    public static boolean isCurrentDateAndGivenDateSame(String date,String formatGiven){
        SimpleDateFormat sdf = new SimpleDateFormat(formatGiven);
        Calendar cal = Calendar.getInstance();
        String currentDate = sdf.format(cal.getTime());
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(currentDate);
            eDate = sdf.parse(date);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.compareTo(eDate)==0;
    }

    public static boolean isCurrentDateAndGivenDateSame(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String currentDate = sdf.format(cal.getTime());
        String endDate = sdf.format(date);
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = sdf.parse(currentDate);
            eDate = sdf.parse(endDate);



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Date... " + ex);
        }


        Log.e("sDate", "==>" + sDate);
        Log.e("eDate", "==>" + eDate);

        return sDate.compareTo(eDate)==0;
    }




    public static int getTimeDifferenceInMinutes(String givenFormat, String startTime, String endTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(givenFormat);

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(startTime);
            date2 = simpleDateFormat.parse(endTime);

            long difference = date2.getTime() - date1.getTime();

            int hours = (int) (difference / (1000 * 60 * 60));
            int mins = (int) ((difference / (1000 * 60)));

            //String diff = hours + ":" + mins;

            /*int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);*/
            /*hours = (hours < 0 ? -hours : hours);*/
            Log.e("======= date2", " :: " + date2.getTime());
            Log.e("======= date1", " :: " + date1.getTime());
            Log.e("======= min", " :: " + mins);
            Log.e("======= difference", " :: " + difference);
            return mins;

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


    }


    public static int getTimeDifferenceInSecondsForCall(long startTime, long endTime) {



        long difference = endTime - startTime;

        int hours = (int) (difference / (1000 * 60 * 60));
        int mins = (int) ((difference / (1000 * 60)));
        int second = (int) ((difference / (1000)));

        //String diff = hours + ":" + mins;

            /*int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);*/
        /*hours = (hours < 0 ? -hours : hours);*/
        Log.e("======= date2", " :: " + startTime);
        Log.e("======= date1", " :: " + endTime);
        Log.e("======= second", " :: " + second);
        Log.e("======= difference", " :: " + difference);
        return second;

    }


    public static long getTimeDifferenceInMiliSecondsForCall(long startTime, long endTime) {



        long difference = endTime - startTime;

        int hours = (int) (difference / (1000 * 60 * 60));
        int mins = (int) ((difference / (1000 * 60)));
        int second = (int) ((difference / (1000)));

        //String diff = hours + ":" + mins;

            /*int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);*/
        /*hours = (hours < 0 ? -hours : hours);*/
        Log.e("======= date2", " :: " + startTime);
        Log.e("======= date1", " :: " + endTime);
        Log.e("======= second", " :: " + second);
        Log.e("======= difference", " :: " + difference);
        return difference;

    }

}
