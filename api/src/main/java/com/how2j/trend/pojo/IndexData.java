package com.how2j.trend.pojo;

import java.io.Serializable;

public class IndexData implements Serializable {
    private String date;

    private float closePoint;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getClosePoint() {
        return closePoint;
    }

    public void setClosePoint(float closePoint) {
        this.closePoint = closePoint;
    }
}
