package cn.javaguide.springbootkafka01sendobjects.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月01日 9:42
 */
public class Payload implements Serializable {
    private Map<String, Object> before;
    private Map<String, Object> after;
    private Source source;
    private String op;
    private Long ts_ms;
    private String transaction;

    public Map<String, Object> getBefore() {
        return before;
    }

    public void setBefore(Map<String, Object> before) {
        this.before = before;
    }

    public Map<String, Object> getAfter() {
        return after;
    }

    public void setAfter(Map<String, Object> after) {
        this.after = after;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Long getTs_ms() {
        return ts_ms;
    }

    public void setTs_ms(Long ts_ms) {
        this.ts_ms = ts_ms;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}
