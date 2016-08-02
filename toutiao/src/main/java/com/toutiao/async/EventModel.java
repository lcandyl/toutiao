package com.toutiao.async;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by admin on 16-7-29.
 */
public class EventModel {
    private EventType type;//发生的事情
    private int actorId;//事件触发者
    private int entityType;
    private int entityId;//触发的对象
    private int entityOwnerId;//对象的拥有者是谁
    private Map<String,String> exts = new HashMap<String,String>();//扩展事件，触发的时候又有啥数据要保存的呢

    public  String getExt(String key){
        return exts.get(key);
    }
    public  EventModel setExt(String key,String value){
         exts.put(key,value);
        return this;
    }

    public EventModel (EventType type){
        this.type = type;
    }

    public EventModel (){

    }
    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
