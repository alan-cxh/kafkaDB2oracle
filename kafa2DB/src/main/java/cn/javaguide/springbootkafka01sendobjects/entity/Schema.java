package cn.javaguide.springbootkafka01sendobjects.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月01日 9:42
 */
public class Schema implements Serializable {
    private String type;
    private List<FieldObj> fields;
    private Boolean optional;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FieldObj getField(String field){
        return getFields().stream().filter(fieldObj -> fieldObj.getField().equalsIgnoreCase(field)).findFirst().get();
    }

    public List<FieldObj> getFields() {
        return fields;
    }

    public void setFields(List<FieldObj> fields) {
        this.fields = fields;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
