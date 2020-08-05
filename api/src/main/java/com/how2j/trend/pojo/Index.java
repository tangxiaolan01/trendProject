package com.how2j.trend.pojo;

import java.io.Serializable;

public class Index implements Serializable {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "com.how2j.trend.com.test.pojo.Index{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
