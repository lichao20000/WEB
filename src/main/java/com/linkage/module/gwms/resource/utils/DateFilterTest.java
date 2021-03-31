package com.linkage.module.gwms.resource.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guankai (AILK No.300401)
 * @version 1.0
 * @category springbootdemo
 * @since 2020/6/12
 */
public class DateFilterTest {

    /** log */
    private static Logger logger = LoggerFactory.getLogger(DateFilterTest.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static boolean checkTime(List<Long> starttimes, List<Long> endtimes, long startdate, long endDate){
        if(starttimes.size()>0) {
            for (int i = 0; i < starttimes.size(); i++) {
                long begin = maxStart(starttimes.get(i), startdate);
                long end = minEnd(endtimes.get(i), endDate);
                long re = end - begin;
                if (re > 0) {
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }

    }

    public static long maxStart(Long start1, Long start2){
        if(start1>start2){
            return start1;
        }else{
            return start2;
        }
    }

    public static long minEnd(Long end1, Long end2){
        if(end1<end2){
            return end1;
        }else{
            return end2;
        }
    }

  /*  public static void main(String[] args) {
        List<Long> starttimes = new ArrayList<Long>();
        starttimes.add((long) 1591974000);//  2020/6/12 23:00:00
        starttimes.add((long) 1591891200); // 2020/6/12 00:00:00

        List<Long> endtimes = new ArrayList<Long>();
        endtimes.add((long) 1591891200);//  2020/6/12 00:00:00
        endtimes.add((long) 1591995600);// 2020/6/13 5:00:00

        boolean fa = checkTime(starttimes,endtimes,1591995550,1591999200);
        System.out.println(fa);
    }*/


    public static Map<String, Long> hasTime(String startTime) throws ParseException {
        logger.warn("[{}]hasTime",startTime);
        Date startDate = sdf.parse(startTime);
        Map<String, Long> resultMap = new HashMap<String, Long>();
        Long starttimeTemp = 0L;
        Long endtimeTemp = 0L;
        //23-24 当天23点，到第二天7点
        if(startDate.getHours()>=23){
            startDate.setHours(23);
            startDate.setMinutes(0);
            startDate.setSeconds(0);
            starttimeTemp = startDate.getTime();
            startDate.setDate(startDate.getDate()+1);
            startDate.setHours(7);
            startDate.setMinutes(0);
            startDate.setSeconds(0);
            endtimeTemp = startDate.getTime();

        }else{//0-7
            startDate.setHours(7);
            startDate.setMinutes(0);
            startDate.setSeconds(0);
            endtimeTemp = startDate.getTime();
            startDate.setDate(startDate.getDate()-1);
            startDate.setHours(23);
            startDate.setMinutes(0);
            startDate.setSeconds(0);
            starttimeTemp = startDate.getTime();

        }
        resultMap.put("starttimeTemp",starttimeTemp/1000);
        resultMap.put("endtimeTemp",endtimeTemp/1000);
        logger.warn("hasTime:{}",resultMap);
        return resultMap;
    }

    public static void main(String[] args) throws ParseException {
        Map<String, Long> resultMap = hasTime("2020-06-14 5:00:00");
        stampToDate(resultMap.get("starttimeTemp"));
        stampToDate(resultMap.get("endtimeTemp"));
    }
    /*
     * 将时间戳转换为时间
     */
    public static void stampToDate(Long s){
        String res;
        Date date = new Date(s);
        res = sdf.format(date);
        System.out.println(res);
    }

    /*
     * 将时间转换为时间戳
     */
    public static Long dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        return ts;
    }

}
