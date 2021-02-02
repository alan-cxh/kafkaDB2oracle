package cn.javaguide.springbootkafka01sendobjects.service.impl;

import cn.javaguide.springbootkafka01sendobjects.entity.Field;
import cn.javaguide.springbootkafka01sendobjects.entity.FieldObj;
import cn.javaguide.springbootkafka01sendobjects.entity.Payload;
import cn.javaguide.springbootkafka01sendobjects.entity.TopicEntity;
import cn.javaguide.springbootkafka01sendobjects.service.KafkaToDbService;
import cn.javaguide.springbootkafka01sendobjects.utils.ConvertUtils;
import cn.javaguide.springbootkafka01sendobjects.utils.DateUtils;
import cn.javaguide.springbootkafka01sendobjects.utils.DebeziumFieldEnum;
import cn.javaguide.springbootkafka01sendobjects.utils.JsonUtils;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;


@Service
public class KafkaToDbServiceImpl implements KafkaToDbService {
    private final Logger logger = LoggerFactory.getLogger(KafkaToDbServiceImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void consumeMessage(ConsumerRecord<String, String> consumerRecord) {
        if (consumerRecord.value() == null) {
            logger.info("there is no data for currently");
        } else {
            try {
                logger.info(consumerRecord.value());
                TopicEntity topicEntity = JsonUtils.toObject(consumerRecord.value(), TopicEntity.class);
                Map<String, Object> kafkaKey = null;
                if (consumerRecord.key() != null) {
                    kafkaKey = JSON.parseObject(consumerRecord.key(), Map.class);
                    kafkaKey = (Map<String, Object>) kafkaKey.get("payload");
                }
                analyzeDataToDb(kafkaKey, topicEntity);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    @KafkaListener(topicPattern = "${kafka.topic.pattern}", groupId = "${kafka.group-id}")
    public void consumeMessage(List<ConsumerRecord<String, String>> list) {
        logger.info("batch execute consume <{}> pieces of data", list.size());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        list.forEach(this::consumeMessage);
        stopWatch.stop();
        logger.info("total spend time = <{}> ms for the batch", stopWatch.getTotalTimeMillis());
    }

    @Override
    public void analyzeDataToDb(Map<String, Object> kafkaKey, TopicEntity topicEntity) {
        Payload payload = topicEntity.getPayload();
        FieldObj fieldObj = topicEntity.getSchema().getField("before");

        Map<String, Object> beforeMap = payload.getBefore();
        Map<String, Object> afterMap = payload.getAfter();
        String op = payload.getOp();
        String table = payload.getSource().getTable();

        String sql = null;
        switch (op) {
            case "u":
                logger.info("update operate for data");
                sql = this.parseUpdateSql(kafkaKey, fieldObj, afterMap, table);
                break;
            case "d":
                logger.info("delete operate for data");
                sql = this.parseDeleteSql(kafkaKey, beforeMap, table);
                break;
            case "c":
            case "r":
                logger.info("insert operate for data");
                sql = this.parseInsertSql(fieldObj, afterMap, table);
                break;
            default:
                throw new RuntimeException("operator choose error, pls confirm...");
        }
        logger.info(">>> {}", sql);
        jdbcTemplate.execute(sql);
    }

    private String parseUpdateSql(Map<String, Object> kafkaKey, FieldObj fieldObj, Map<String, Object> afterData, String table) {
        for (String key : kafkaKey.keySet()) {
            afterData.remove(key);
        }
        StringBuilder builder = this.parseUpdateDll(fieldObj, kafkaKey, afterData, table);
        return builder.toString();
    }

    private StringBuilder parseUpdateDll(FieldObj fieldObj, Map<String, Object> kafkaKey, Map<String, Object> updateDataMap, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" update ").append(table).append(" set ");
        for (Map.Entry<String, Object> updateData : updateDataMap.entrySet()) {
            if (updateData.getValue() == null) {
                sqlBuilder.append(updateData.getKey()).append(" = null").append(",");
            } else {
                if (!this.handleField(fieldObj, updateData.getKey(), updateData.getValue(), sqlBuilder, false)) {
                    sqlBuilder.append(updateData.getKey()).append(" = ").append("'").append(updateData.getValue()).append("'").append(",");
                }
            }
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);

        sqlBuilder.append(" where ");
        for (Map.Entry<String, Object> map : kafkaKey.entrySet()) {
            if (map.getValue() != null) {
                sqlBuilder.append(map.getKey()).append(" = ").append("'").append(map.getValue()).append("' and ");
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - 5, sqlBuilder.length());
        return sqlBuilder;
    }

    /**
     * 处理特定字段类型
     */
    private boolean handleField(FieldObj fieldObj, String columnName, Object columnValue, StringBuilder sqlBuilder, boolean isCondition) {
        List<Field> beforeFieldList = fieldObj.getFields();

        boolean flag = false;
        String name;
        for (Field field : beforeFieldList) {
            if (field.getField().equals(columnName) && field.getName() != null) {
                name = field.getName();
                if (name.equals(DebeziumFieldEnum.date.getValue())) {
                    ConvertUtils.convertUpdateDate(columnName, columnValue, sqlBuilder, isCondition);
                    return true;
                } else if (name.equals(DebeziumFieldEnum.zoned_timestamp.getValue())) {
                    ConvertUtils.convertZonedTimestamp(columnName, columnValue, sqlBuilder, isCondition);
                    return true;
                } else if (name.equals(DebeziumFieldEnum.micro_timestamp.getValue())) {
                    ConvertUtils.convertUpdateMicroTimestamp(columnName, columnValue, sqlBuilder, isCondition);
                    return true;
                }
            }
        }
        return flag;
    }

    /**
     * 转添加Sql
     *
     * @param afterData
     * @param table
     * @return
     */
    private String parseInsertSql(FieldObj fieldObj, Map<String, Object> afterData, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into ").append(table).append(" ( ");

        for (String key : afterData.keySet()) {
            sqlBuilder.append(key).append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(" ) values (");

        for (Map.Entry<String, Object> entry : afterData.entrySet()) {
            if (entry.getValue() == null) {
                sqlBuilder.append("null").append(",");
            } else {
                sqlBuilder.append(handleInsertField(fieldObj, entry.getKey(), entry.getValue())).append(",");
            }
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(" ) ");

        return sqlBuilder.toString();
    }

    private String handleInsertField(FieldObj fieldObj, String columnName, Object columnValue) {
        List<Field> beforeFieldList = fieldObj.getFields();
        String name;
        for (Field field : beforeFieldList) {
            if (field.getField().equals(columnName) && field.getName() != null) {
                name = field.getName();
                if (name.equals(DebeziumFieldEnum.date.getValue())) {
                    return "to_date('" + ConvertUtils.convertDate(columnValue) + "', '" + DateUtils.SHORT_DATE_FORMAT + "')";
                } else if (name.contains(DebeziumFieldEnum.micro_timestamp.getValue())) {
                    return "to_date('" + ConvertUtils.convertMicroTimestamp(columnValue) + "', '" + DateUtils.LONG_DATE_SQL_FORMAT + "')";
                } else if (name.contains(DebeziumFieldEnum.zoned_timestamp.getValue())) {
                    return "to_date('" + ConvertUtils.convertZonedTimestamp(columnValue) + "', '" + DateUtils.LONG_DATE_SQL_FORMAT + "')";
                }
            }
        }
        return "'" + columnValue + "'";
    }

    /**
     * 转删除sql
     *
     * @param beforeData
     * @param table
     * @return
     */
    private String parseDeleteSql(Map<String, Object> kafkaKey, Map<String, Object> beforeData, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("delete from ").append(table).append(" where ");
        for (Map.Entry<String, Object> map : kafkaKey.entrySet()) {
            if (map.getValue() != null) {
                sqlBuilder.append(map.getKey()).append(" = ").append("'").append(map.getValue()).append("' and ");
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - 5, sqlBuilder.length());
        return sqlBuilder.toString();
    }
}
