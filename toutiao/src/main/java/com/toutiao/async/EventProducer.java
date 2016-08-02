package com.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.toutiao.controller.LoginController;
import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 16-7-29.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class );
    @Autowired
    JedisAdapter jedisAdapter;//需要一个队列来发送一个Event

    public boolean fireEvent (EventModel model){
        try {
            String json = JSONObject.toJSONString(model);//序列化
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e){
            logger.error("队列序列化失败（事件推进去）"+ e.getMessage());
            return false;
        }
    }
}
