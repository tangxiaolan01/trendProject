package com.how2j.trend.pojo;

public class Profit {

    String date;
    float value;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Profit{" +
                "date='" + date + '\'' +
                ", value=" + value +
                '}';
    }
}