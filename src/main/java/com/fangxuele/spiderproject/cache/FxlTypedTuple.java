package com.fangxuele.spiderproject.cache;

import java.io.Serializable;

/**
 * fxl自定义的TypedTuple，用于redis zset结果的序列化传输
 * Created by zhouy on 2017/5/11.
 */
public class FxlTypedTuple implements Serializable {

    private static final long serialVersionUID = 1L;

    public FxlTypedTuple(){}

    private Double score;

    private String value;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
