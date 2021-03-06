package com.toutiao.dao;

import com.toutiao.model.News;
import com.toutiao.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by admin on 16-7-7.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;
    @Insert({"insert into",TABLE_NAME,"(", INSERT_FIELDS,") values (#{title},#{link},#{image},#{likeCount}," +
            "#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);
    //offset 是Limit数量 要用的？？？(分页)；
    //前边数插入 后边是读取， 读取采用xml @Param
    List<News> selectByUserIdAndOffset(@Param("userId")int useId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
