package cn.javaguide.springbootkafka01sendobjects.entity;

import java.io.Serializable;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月01日 9:46
 */
public class Field implements Serializable {
    private String type;
    private Boolean optional;
    private String field;
    private String name;
    private Integer version;
    private Object parameters;

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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
}
