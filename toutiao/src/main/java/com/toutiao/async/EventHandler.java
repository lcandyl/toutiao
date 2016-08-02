package com.toutiao.async;

import java.util.List;

/**
 * Created by admin on 16-7-29.
 */
public interface EventHandler {
    //处理不同的事情
    void doHandle(EventModel model);//处理这个Model
    List<EventType> getSupportEventTypes();//对这些EventType进行处理
}
