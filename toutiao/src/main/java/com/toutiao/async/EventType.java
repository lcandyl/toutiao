package com.toutiao.async;

/**
 * Created by admin on 16-7-29.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private  int value;
    EventType (int value){this.value = value; }
    public int getValue(){ return value;}
}
