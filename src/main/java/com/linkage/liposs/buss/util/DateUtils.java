package com.linkage.liposs.buss.util;

import java.util.Calendar;

public class DateUtils {
    /**
     * @param seconds long
     * @param seprator String
     * @param alignZero boolean
     * @return String
     */
    public static String getYearMonthDayString(long seconds, String seprator, boolean alignZero) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(seconds * 1000);
        String retstr = "";
        retstr += cal.get(Calendar.YEAR) + seprator;
        int mon = cal.get(Calendar.MONTH) + 1;
        if ((alignZero) && (mon < 10)) {
            retstr += "0";
        }
        retstr += mon + seprator;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if ((alignZero) && (day < 10)) {
            retstr += "0";
        }
        retstr += day;
        return retstr;
    }

    public static String getYearMonthString(long seconds, String seprator, boolean alignZero) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(seconds * 1000);
        String retstr = "";
        retstr += cal.get(Calendar.YEAR) + seprator;
        int mon = cal.get(Calendar.MONTH) + 1;
        if ((alignZero) && (mon < 10)) {
            retstr += "0";
        }
        retstr += mon;
        return retstr;
    }

    public static long getDayBegin(long thisTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(thisTime * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }


    public static long getWeekBegin(long thisTime) {
        return thisTime - 7 * 24 * 3600;
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(thisTime * 1000);
//        int days = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
//        cal.setTimeInMillis((thisTime - days * 24 * 3600) * 1000);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTimeInMillis() / 1000;
    }

    public static long getMonthBegin(long thisTime) {
        return thisTime - 31 * 24 * 3600;
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(thisTime * 1000);
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTimeInMillis() / 1000;
    }


    public static long getDayEnd(long thisTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(thisTime * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis() / 1000;
    }

    public static long getWeekEnd(long thisTime) {
        return thisTime;
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(thisTime * 1000);
//        int days = Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK);
//        cal.setTimeInMillis((thisTime + days * 24 * 3600) * 1000);
//        cal.set(Calendar.HOUR_OF_DAY, 23);
//        cal.set(Calendar.MINUTE, 59);
//        cal.set(Calendar.SECOND, 59);
//        cal.set(Calendar.MILLISECOND, 999);
//        return cal.getTimeInMillis() / 1000;
    }

    public static long getMonthEnd(long thisTime) {
        return thisTime;
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(thisTime * 1000);
//        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        cal.set(Calendar.DAY_OF_MONTH, lastDay);
//        cal.set(Calendar.HOUR_OF_DAY, 23);
//        cal.set(Calendar.MINUTE, 59);
//        cal.set(Calendar.SECOND, 59);
//        cal.set(Calendar.MILLISECOND, 999);
//        return cal.getTimeInMillis() / 1000;
    }

    public static boolean duringSameMonth(long beginTime, long endTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(beginTime * 1000);
        int month1 = cal1.get(Calendar.MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(endTime * 1000);
        int month2 = cal2.get(Calendar.MONTH);

        return (endTime - beginTime < 32 * 24 * 3600) && (month2 == month1);
    }

//    public static String get_sgw_conn_day_table_names(long beginTime, long endTime) {
//        if(duringSameMonth(beginTime,endTime)) {
//            return "sgw_conn_day_" + getYearMonthString(endTime, "_", true);
//        }else{
//            String tStart = "sgw_conn_day_" + getYearMonthString(beginTime, "_", true);
//        }
//    }

}
