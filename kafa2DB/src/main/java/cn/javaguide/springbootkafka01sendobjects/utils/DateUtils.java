package cn.javaguide.springbootkafka01sendobjects.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 */
public class DateUtils {

    /**
     * 数据库时间格式
     */
    public static final String LONG_DATE_SQL_FORMAT = "yyyy-MM-dd HH24:mi:ss";

    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";


    public static String getStringDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
