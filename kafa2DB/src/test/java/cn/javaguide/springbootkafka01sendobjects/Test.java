package cn.javaguide.springbootkafka01sendobjects;


import cn.javaguide.springbootkafka01sendobjects.utils.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Calendar date1 = Calendar.getInstance();
        date1.set(2021, 0, 31);
        Calendar date2 = Calendar.getInstance();
        date1.set(1970, 0, 1);
        date1.add(Calendar.DATE, 18658);
        System.out.println(daysBetween(date1.getTime(), date2.getTime()));
//18658
    }

    public static int daysBetween(Date date1, Date date2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    @org.junit.Test
    public void test() {
        ZonedDateTime expectedTimestamp = ZonedDateTime.of(
                LocalDateTime.parse("2014-09-08T17:51:04.780"),
//                LocalDateTime.parse("2021-01-29T13:00:000"),
                ZoneId.of("Asia/Shanghai")
        ).withZoneSameInstant(ZoneOffset.UTC);
        System.out.println();
    }

    @org.junit.Test
    public void test3() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis((1612193811000L));
        String stringDate = DateUtils.getStringDate(instance.getTime(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(stringDate);
    }
}
