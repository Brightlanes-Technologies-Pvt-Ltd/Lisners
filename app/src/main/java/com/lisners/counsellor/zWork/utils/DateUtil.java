package com.lisners.counsellor.zWork.utils;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class DateUtil {

    public static String dateFormatter(String date, String targetFormat) {
        try {
            return new SimpleDateFormat(targetFormat,Locale.getDefault()).format(new SimpleDateFormat().parse(date));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Date From String date");
            sb.append(e.getMessage());
            sb.append(e.getCause());
            //Timber.m519e(sb.toString(), new Object[0]);
            return "";
        }
    }

    public static String dateFormatter(String date, String formatGiven, String targetFormat) {
        try {
            return new SimpleDateFormat(targetFormat,Locale.getDefault()).format(new SimpleDateFormat(formatGiven,Locale.getDefault()).parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("Date From String date");
            sb.append(e.getMessage());
            sb.append(e.getCause());
            //Timber.m519e(sb.toString(), new Object[0]);
            return "";
        }
    }

    public static String getDateInStringFromDate(long timeInMillis, String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            return new SimpleDateFormat(format,Locale.getDefault()).format(calendar.getTime());
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Date Exception");
            sb.append(e.getCause());
            sb.append(e.getMessage());
            Timber.d(sb.toString());
            return "";
        }
    }


    public static Date getDateFromString(String date, String formatGiven) {
        try {

            return new SimpleDateFormat(formatGiven,Locale.getDefault()).parse(date);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Date Exception");
            sb.append(e.getCause());
            sb.append(e.getMessage());
            Timber.d(sb.toString());
            return null;
        }

    }


    public static String changeRelativeTime(String date, String givenDateFormat) {
        String time = "";
        SimpleDateFormat sdf = new SimpleDateFormat(givenDateFormat, Locale.getDefault());
        try {
            Date timeStampDate = sdf.parse(date);
            long timeStampSeconds = timeStampDate.getTime();
            long currentTimeSeconds = Calendar.getInstance().getTimeInMillis();
            time = (String) DateUtils.
                    getRelativeTimeSpanString(timeStampSeconds,
                            currentTimeSeconds, DateUtils.SECOND_IN_MILLIS, FORMAT_ABBREV_RELATIVE);

        } catch (Exception e) {
            e.printStackTrace();
            time = date;
        }

        return time;
    }
}

