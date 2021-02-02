package cn.javaguide.springbootkafka01sendobjects.utils;

/**
 * @author Administrator
 */
public enum DebeziumFieldEnum {

    date("date", "io.debezium.time.Date"),
    zoned_timestamp("zoned_timestamp", "io.debezium.time.ZonedTimestamp"),
    micro_timestamp("micro_timestamp", "io.debezium.time.MicroTimestamp")
    ;
    DebeziumFieldEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 根据Key得到枚举的Value
     *
     * @param key
     * @return
     */
    public static String getEnumValue(String key) {
        DebeziumFieldEnum[] alarmGrades = DebeziumFieldEnum.values();
        for (DebeziumFieldEnum alarmGrade : alarmGrades) {
            if (alarmGrade.getCode().equals(key)) {
                return alarmGrade.getValue();
            }
        }
        return null;
    }

    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
