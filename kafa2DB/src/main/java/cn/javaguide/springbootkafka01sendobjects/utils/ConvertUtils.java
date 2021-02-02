package cn.javaguide.springbootkafka01sendobjects.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author Administrator
 */
public class ConvertUtils {


    /**
     * Update操作，io.debezium.time.Date 类型转换
     *
     * 根据debezium的Date类型返回数据规则：当前时间 减去 ‘1970-1-1’ = 天数
     * @param columnName
     * @param columnValue
     * @param sqlBuilder
     * @param isCondition
     */
    public static void convertUpdateDate(String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        Calendar date = Calendar.getInstance();
        date.set(1970, 0, 1);
        date.add(Calendar.DATE, (Integer) columnValue);
        if (isCondition) {
            sqlBuilder.append(" and ").append(columnName).append(" = ")
                    .append("to_date('").append(DateUtils.getStringDate(date.getTime(), DateUtils.SHORT_DATE_FORMAT))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("') ");
        } else {
            sqlBuilder.append(columnName).append(" = ");
            sqlBuilder.append("to_date('").append(DateUtils.getStringDate(date.getTime(), DateUtils.SHORT_DATE_FORMAT))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("'), ");
        }
    }

    /**
     * Update操作，转 MicroTimestamp
     * @param columnName
     * @param columnValue
     * @param sqlBuilder
     * @param isCondition
     */
    public static void convertUpdateMicroTimestamp(String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        if (columnValue == null) {
            return;
        }
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(((Long) columnValue) / 1000);
        if (isCondition) {
            sqlBuilder.append(" and ").append(columnName).append(" = ")
                    .append("to_date('").append(DateUtils.getStringDate(date.getTime(), DateUtils.LONG_DATE_FORMAT))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
        } else {
            sqlBuilder.append(columnName).append(" = ");
            sqlBuilder.append("to_date('").append(DateUtils.getStringDate(date.getTime(), DateUtils.LONG_DATE_FORMAT))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("'),");
        }
    }

    /**
     * Insert操作，转Date
     * @param columnValue
     * @param sqlBuilder
     */
    public static void convertInsertDate(Object columnValue, StringBuilder sqlBuilder) {
        if (columnValue == null) {
            sqlBuilder.append("null").append(",");
        } else {
            Calendar date = Calendar.getInstance();
            date.set(1970, 0, 1);
            date.add(Calendar.DATE, (Integer) columnValue);
            sqlBuilder.append("to_date('").append(DateUtils.getStringDate(date.getTime(), DateUtils.SHORT_DATE_FORMAT))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')").append(",");
        }
    }

    /**
     * insert操作 转化MicroTimestamp
     * @param columnValue
     * @param sqlBuilder
     */
    public static void convertInsertMicroTimestamp(Object columnValue, StringBuilder sqlBuilder) {
        if (columnValue == null) {
            sqlBuilder.append("null").append(",");
        } else {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(((Long) columnValue) / 1000);
            sqlBuilder.append("to_date('").append(DateUtils.getStringDate(date.getTime(), DateUtils.LONG_DATE_FORMAT))
                    .append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')").append(",");
        }
    }

    /**
     *
     * @param columnName
     * @param columnValue
     * @param sqlBuilder
     * @param isCondition
     */
    public static void convertZonedTimestamp(String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        String format = ConvertUtils.convertZonedTimestamp(columnValue);
        sqlBuilder.append(columnName).append(" = ")
                .append("to_date('").append(format).append("', '").append(DateUtils.LONG_DATE_SQL_FORMAT).append("')");
    }

    public static String convertDate(Object columnValue) {
        Calendar date = Calendar.getInstance();
        date.set(1970, 0, 1);
        date.add(Calendar.DATE, (Integer) columnValue);

        return DateUtils.getStringDate(date.getTime(), DateUtils.SHORT_DATE_FORMAT);
    }


    public static String convertMicroTimestamp(Object columnValue) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis((Long) columnValue / 1000);
        return DateUtils.getStringDate(date.getTime(), DateUtils.LONG_DATE_FORMAT);
    }

    public static String convertZonedTimestamp(Object columnValue) {
        ZonedDateTime expectedTimestamp = ZonedDateTime.of(
                LocalDateTime.parse(String.valueOf(columnValue)),
                ZoneId.of("Asia/Shanghai")
        ).withZoneSameInstant(ZoneOffset.UTC);

        return expectedTimestamp.format(DateTimeFormatter.BASIC_ISO_DATE);
    }

}
