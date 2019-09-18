package com.nichetech.smartonsite.benchmark.Common;

import android.util.Log;
import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * CONECT(com.collabera.conect) <br />
 * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>  <br />
 * on 06/09/16.
 *
 * @author Suthar Rohit
 */
public class DateTimeUtils {

    public static final String SERVER_FORMAT_DATE = "MM/dd/yyyy";
    public static final String SERVER_MONTH_YEAR_FORMAT = "MM/yyyy";

    public static final String SERVER_FORMAT_DATE_TIME_T = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SERVER_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String SERVER_FORMAT_DATE_NEW = "yyyy-MM-dd";
    public static final String TIMESHEET_REQUEST_DATE = "MM-dd-yyyy";
    public static final String OUTPUT_FORMAT = "MM/dd/yyyy";
    public static final String OUTPUT_FORMAT_DATE_TIME = "dd MMM, yyyy hh:mm a";
    public static final String REDEPLOY_ME_END_DATE = "dd MMM yyyy";
    public static final String HIRED_DATE_TIME_FORMAT = "MMM, hh:mm a";
    public static final String TIMESHEET_OUTPUT_FORMAT_DATE_TIME = "EEE, dd MMM yyyy";

    public static final String TIME_FORMAT = "HH:mm";
    public static final String SERVER_DATE_TIME_FORMAT = "HH:mm";

    public static String changeTimezone(@NonNull String dateTime, @NonNull String format) {
        return changeTimezone(dateTime, format, format);
    }

    public static String changeTimezone(@NonNull String dateTime,
                                        @NonNull String fromFormat, @NonNull String toFormat) {
        try {
            DateFormat fromSDF = new SimpleDateFormat(fromFormat, Locale.getDefault());
            fromSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = fromSDF.parse(dateTime);
            DateFormat toSDF = new SimpleDateFormat(toFormat, Locale.getDefault());
            toSDF.setTimeZone(TimeZone.getDefault());
            return toSDF.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String changeTimezoneToUTC(@NonNull String dateTime, @NonNull String format) {
        try {
            DateFormat fromSDF = new SimpleDateFormat(format, Locale.getDefault());
            fromSDF.setTimeZone(TimeZone.getDefault());
            Date date = fromSDF.parse(dateTime);
            DateFormat toFormat = new SimpleDateFormat(format, Locale.getDefault());
            toFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDateTimeMix() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            return sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return "0000-00-00 00:00:00";
        }
    }

    public static String getCurrentDateTime() {
        try {
            Locale l = Locale.getDefault();
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_FORMAT_DATE_TIME, l);
            return sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return "0000-00-00 00:00:00";
        }
    }

    public static String changeDateTimeFormat(@NonNull String dateTime,
                                              @NonNull String fromFormat) {
        try {
            SimpleDateFormat sdfFrom = new SimpleDateFormat(fromFormat, Locale.US);
            Date date = sdfFrom.parse(dateTime);
            SimpleDateFormat sdfTo = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return sdfTo.format(date);
        } catch (Exception e) {
            try {
                SimpleDateFormat sdfFrom = new SimpleDateFormat(fromFormat, Locale.getDefault());
                Date date = sdfFrom.parse(dateTime);
                SimpleDateFormat sdfTo = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return sdfTo.format(date);
            } catch (Exception e1) {
                Log.e("SimpleDateFormat", "Error while parsing " + dateTime + " with " + fromFormat + " to " + "dd/MM/yyyy");
                return dateTime;
            }
        }
    }

    public static String changeTime(@NonNull String dateTime,
                                              @NonNull String fromFormat) {
        try {
            SimpleDateFormat sdfFrom = new SimpleDateFormat(fromFormat, Locale.US);
            Date date = sdfFrom.parse(dateTime);
            SimpleDateFormat sdfTo = new SimpleDateFormat("HH:mm", Locale.US);
            return sdfTo.format(date);
        } catch (Exception e) {
            try {
                SimpleDateFormat sdfFrom = new SimpleDateFormat(fromFormat, Locale.getDefault());
                Date date = sdfFrom.parse(dateTime);
                SimpleDateFormat sdfTo = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return sdfTo.format(date);
            } catch (Exception e1) {
                Log.e("SimpleDateFormat", "Error while parsing " + dateTime + " with " + fromFormat + " to " + "HH:mm");
                return dateTime;
            }
        }
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int myYear = c.get(Calendar.YEAR);
        int myMonth = c.get(Calendar.MONTH);
        int myDay = c.get(Calendar.DAY_OF_MONTH);
        String moy1;
        String dom1;

        if (myMonth < 9) {
            moy1 = "0" + String.valueOf(myMonth + 1);
        } else {
            moy1 = String.valueOf(myMonth + 1);
        }
        if (myDay < 9) {
            dom1 = "0" + String.valueOf(myDay);
        } else {
            dom1 = String.valueOf(myDay);
        }
        return String.valueOf(myYear) + "-" + moy1 + "-" + dom1;
    }

    public static String getCurrentDateWithFormat(String format) {
        try {
            Locale l = Locale.getDefault();
            SimpleDateFormat sdf = new SimpleDateFormat(format, l);
            return sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return "0000-00-00 00:00:00";
        }
    }


    public static String getDayOldDateTime(int old_day) {
        try {
            Locale l = Locale.getDefault();
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_FORMAT_DATE_TIME, l);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH, old_day);

            Date date = sdf.parse(now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-"
                    + now.get(Calendar.DATE) + " " + now.get(Calendar.HOUR_OF_DAY) + ":"
                    + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));

            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "0000-00-00 00:00:00";
        }
    }

    public static Date stringToDate(String dateTime) {
        return stringToDate(dateTime, SERVER_FORMAT_DATE_TIME);
    }

    public static Date stringToDate(String dateTime, String fromFormat) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, Locale.US);
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, Locale.getDefault());
                date = sdf.parse(dateTime);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return date;
    }

    public static String getFormatedDate(String hireDate) {
        String mDateTime = DateTimeUtils.changeTimezone(hireDate, DateTimeUtils.SERVER_FORMAT_DATE_TIME);
//        String mDate= DateTimeUtils.changeDateTimeFormat(mDateTime,DateTimeUtils.SERVER_FORMAT_DATE_TIME,HIRED_DATE_TIME_FORMAT);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SERVER_FORMAT_DATE_TIME, Locale.getDefault());
        try {
            cal.setTime(simpleDateFormat.parse(mDateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("DATE", cal.getTime() + "");
        String dayNumberSuffix = getDayNumberSuffix(cal.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' " + HIRED_DATE_TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(cal.getTime());
    }

    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup>th</sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup>st</sup>";
            case 2:
                return "<sup>nd</sup>";
            case 3:
                return "<sup>rd</sup>";
            default:
                return "<sup>th</sup>";
        }
    }

    public static Calendar stringToCalenderdate(String selectedDateTime) {
        String mDateTime = DateTimeUtils.changeTimezone(selectedDateTime, DateTimeUtils.SERVER_FORMAT_DATE_TIME);
//        String mDate= DateTimeUtils.changeDateTimeFormat(mDateTime,DateTimeUtils.SERVER_FORMAT_DATE_TIME,HIRED_DATE_TIME_FORMAT);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SERVER_FORMAT_DATE_TIME, Locale.getDefault());
        try {
            cal.setTime(simpleDateFormat.parse(mDateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;

    }

    public static String getWeekPeriod(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//                calendar.set(Calendar.DAY_OF_MONTH, 1);

        // "calculate" the start date of the week
        Calendar first = (Calendar) calendar.clone();
        first.add(Calendar.DAY_OF_WEEK,
                first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

        // and add six days to the end date
        Calendar last = (Calendar) first.clone();
        last.add(Calendar.DAY_OF_YEAR, 6);

        // print the result
        SimpleDateFormat df = new SimpleDateFormat(SERVER_FORMAT_DATE, Locale.getDefault());
        String weekPeriod = df.format(first.getTime()) + " ---> " +
                df.format(last.getTime());

        System.out.println(weekPeriod);

        return weekPeriod;
    }


    public static Date getEndDayOfCurrentWeek(Date date) {
        Date newDate;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            // "calculate" the start date of the week
            Calendar first = (Calendar) calendar.clone();
            first.setTime(date);
            first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

            // and add six days to the end date
            Calendar last = (Calendar) first.clone();
            last.setFirstDayOfWeek(Calendar.MONDAY);
            last.add(Calendar.DAY_OF_WEEK, 6);

            // print the result
            SimpleDateFormat df = new SimpleDateFormat(SERVER_FORMAT_DATE, Locale.getDefault());
            String weekPeriod = df.format(first.getTime()) + " ---> " +
                    df.format(last.getTime());

            System.out.println(weekPeriod);

            newDate = last.getTime();
        } else {
            newDate = date;
        }
        return getRoundFigureDate(newDate);
    }

    public static Date getRoundFigureDate(Date newDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            newDate = sdf.parse(sdf.format(newDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }


}
