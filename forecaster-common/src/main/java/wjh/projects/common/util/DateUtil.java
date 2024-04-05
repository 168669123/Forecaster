package wjh.projects.common.util;

import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

public class DateUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static Date utcStringToDate(String utcTime, String pattern) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(utc);
        try {
            return sdf.parse(utcTime);
        } catch (ParseException e) {
            Logger logger = Logger.getLogger(DateUtil.class.getName());
            logger.warning("UTC时间解析错误：" + e.getMessage());
            return null;
        }
    }

    public static Date stringToDate(String string, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            logger.warn("无法将字符串\"{} \"转换为 Date 类型，当前格式：{}，\n异常原因：{}", string, pattern, e.getMessage());
            return null;
        }
    }

    public static Date stringToDate(String string, String pattern, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            logger.warn("无法将字符串\"{} \"转换为 Date 类型，当前格式：{} {}，\n异常原因：{}", string, pattern, locale, e.getMessage());
            return null;
        }
    }

    public static String dateToString(Date time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(time);
    }

    /**
     * 获取 date 加上 seconds 秒后的时间
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}
