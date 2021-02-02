package cn.javaguide.springbootkafka01sendobjects.entity;

import java.io.Serializable;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月01日 9:41
 */
public class TopicEntity implements Serializable{
    private Schema schema;
    private Payload payload;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
