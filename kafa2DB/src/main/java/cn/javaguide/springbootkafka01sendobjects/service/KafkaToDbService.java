package cn.javaguide.springbootkafka01sendobjects.service;

import cn.javaguide.springbootkafka01sendobjects.entity.TopicEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;


public interface KafkaToDbService {
    void consumeMessage(ConsumerRecord<String, String> consumerRecord);

    void consumeMessage(List<ConsumerRecord<String, String>> list);

    void analyzeDataToDb(Map<String, Object> kafkaKey, TopicEntity topicEntity);
}
