package com.toutiao.dao;

import com.toutiao.model.Comment;
import com.toutiao.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by admin on 16-7-19.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = " from_id, to_id, content,has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, "+ INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ",SELECT_FIELDS, " from ",TABLE_NAME, " where conversation_id = #{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit );

    // (select * from toutiao.message where from_id = 12 or to_id = 12 order by id desc) tt group by conversation_id order by id desc;
    @Select({"select ",SELECT_FIELDS, " ,count(id) as count from ( select * from ",TABLE_NAME, " where from_id = #{userId} or to_id = #{userId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id = #{userId} and conversation_id = #{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    @Update({"update ", TABLE_NAME, " set has_read = 1 where to_id = #{userId} and conversation_id = #{conversationId}" })
    int changeHasRead(@Param("userId") int userId,@Param("conversationId") String conversationId);
}
