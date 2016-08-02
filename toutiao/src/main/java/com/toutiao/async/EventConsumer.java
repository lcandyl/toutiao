package com.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 16-7-29.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //把事件都组织起来
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    //事件处理
    //ApplicationContextAware 记录上下文的
    private ApplicationContext applicationContext;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        //当前Webapplication里面所有的实现EventHandler接口的类 全部找出来
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                //看它注册的哪一个事件 登记进来
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());//把自己注册进去
                }
            }
        }

        //开个线程完全处理这个
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //取数据
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    //这是一个阻塞的队列
                    List<String> messages = jedisAdapter.brpop(0, key);
                    for (String message : messages) {
                        if (message.equals(key)) {
                            continue;
                        }
                        //取出来
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);

                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }

                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
