package com.toutiao.async.handler;

import com.toutiao.async.EventHandler;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventType;
import com.toutiao.model.Message;
import com.toutiao.model.User;
import com.toutiao.service.MessageService;
import com.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 16-7-29.
 */
@Component
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(3);//系统发出的 随便写的3
        //message.setToId(model.getEntityOwnerId());
        message.setToId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你得资讯，http://127.0.0.1:7070/news/"+model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    //只关心点赞
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
