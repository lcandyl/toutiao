package com.toutiao.dao;

import com.toutiao.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import static com.toutiao.dao.CommentDAO.SELECT_FIELDS;
import static com.toutiao.dao.CommentDAO.TABLE_NAME;

/**
 * Created by admin on 16-7-18.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " user_id, content, created_date,entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, "+ INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({" select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId,
                                 @Param("entityType") int entityType);

    @Select({"select count(id) from", TABLE_NAME,
            "where entity_id = #{entityId} and entity_type = #{entityType} order by id desc"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
