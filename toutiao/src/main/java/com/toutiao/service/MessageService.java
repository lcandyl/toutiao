package com.toutiao.service;

import com.toutiao.dao.MessageDAO;
import com.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 16-7-19.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    public int addMessage (Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int  userId, int offset, int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int getConversationUnreadCount (int userId, String conversationId){
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }

    public int changeHasRead(int userId, String conversationId){
        return  messageDAO.changeHasRead(userId,conversationId);
    }
}
