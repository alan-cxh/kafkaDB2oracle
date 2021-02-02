package cn.javaguide.springbootkafka01sendobjects.entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月01日 9:46
 */
public class FieldObj implements Serializable {
    private String type;
    private Boolean optional;
    private List<Field> fields;
    private String name;
    private String field;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
