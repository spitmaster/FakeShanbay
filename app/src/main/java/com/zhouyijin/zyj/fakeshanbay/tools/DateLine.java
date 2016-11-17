package com.zhouyijin.zyj.fakeshanbay.tools;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouyijin on 2016/10/23.
 * <p>
 * 这个类中指定的dateline形式是YYYY-MM-DD
 */

public class DateLine {


    public static String getTodayDateLine() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //太坑爹了!一月是0
        int month = 1 + calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return setDateLine(year, month, day);
    }

    public static boolean isTodayDateLine(String dateLine) {
        if (dateLine == null) {
            return false;
        }
        return getTodayDateLine().equals(dateLine);
    }

    public static String setDateLine(int year, int month, int day) {
        String dateLine;
        if (month < 10) {
            dateLine = year + "-0" + month + "-" + day;
        } else {
            dateLine = year + "-" + month + "-" + day;
        }
        return dateLine;
    }

    public static int getDayOfDateLine(String DateLine) {
        int result = -1;
        if (DateLine!=null){
            String regex = "\\d{4}[-]\\d{2}[-]\\d{2}";
            if (DateLine.matches(regex)){
                result = Integer.valueOf(DateLine.substring(DateLine.length()-2,DateLine.length()));
            }
        }
        return result;
    }
    public static int getMonthOfDateLine(String DateLine) {
        int result = -1;
        if (DateLine!=null){
            String regex = "\\d{4}[-]\\d{2}[-]\\d{2}";
            if (DateLine.matches(regex)){
                result = Integer.valueOf(DateLine.substring(5,7));
            }
        }
        return result;
    }

    public static int getYearOfDateLine(String DateLine) {
        int result = -1;
        if (DateLine!=null){
            String regex = "\\d{4}[-]\\d{2}[-]\\d{2}";
            if (DateLine.matches(regex)){
                result = Integer.valueOf(DateLine.substring(0,4));
            }
        }
        return result;
    }

    public static String getTodayDatelineInSelectForm() {
        return "'" + getTodayDateLine() + "'";
    }

}
